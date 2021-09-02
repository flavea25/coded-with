import com.google.inject.Guice;
import com.google.inject.Injector;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import services.MyInjector;
import services.TechnologyService;
import services.database.MongoService;
import technologies.Technology;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    private static final String DATABASE_NAME = "coded-with";
    private static final String GITHUB_REPOSITORY_LINK_START = "https://github.com/";
    private static final String ANALYSED_REPOSITORIES = "analysed-repositories";
    private static final String REPOSITORY_DB = "repoDB";
    private static final String TOOLS_RANKING = "top2000"; //TODO change to toolsRanking

    private static final TechnologyService technologyService = injector.getInstance(TechnologyService.class);

    private static final MongoService dbService = injector.getInstance(MongoService.class);

    public static void main(String[] args) {
        dbService.createDefaultConnection();
        dbService.useDatabase(DATABASE_NAME);

        analyzeTopRepositories(args);
//        saveRepositoryData(args);

        dbService.closeConnection();
    }

    private static void logTechnologiesByCategory(String[] args) {
        if(args.length != 2) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path/link to project, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            log.info("Program started...");
            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
        }
    }

    private static void saveRepositoryData(String[] args) {
        log.info("Program started...");
        if (args.length != 2) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path/link to project, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        } else {
            String repo = args[0];
            if (repo.startsWith(GITHUB_REPOSITORY_LINK_START)) {
                Document found = dbService.findDocumentFromCollection(Map.of("repository", repo), REPOSITORY_DB);
                if (found != null && !found.isEmpty()) {
                    System.out.println("Repository analysed at " + found.get("date") + "\nFound technologies: ");
                    List<String> tools = (List<String>) found.get("usedTools");
                    tools.forEach(t -> System.out.println("\t" + t));

                    System.out.println("Do you want to re-analyse it? (y/n)");
                    Scanner in = new Scanner(System.in);
                    char option = in.next().charAt(0);
                    if (option == 'y') {
                        tools.forEach(t -> {
                            Document tool = dbService.findDocumentFromCollection(Map.of("name", t), TOOLS_RANKING);
                            if (tool != null && !tool.isEmpty()) {
                                dbService.updateDocumentInCollection(tool, Map.of("timesUsed", (int) tool.get("timesUsed") - 1), TOOLS_RANKING);
                            }
                        });
                        List<Technology> usedTechs = technologyService.getUsedTechnologies(repo, technologyService.getAllTechnologiesFromFile(args[1]));
                        addRepoInDB(repo, usedTechs, TOOLS_RANKING);
                    }
                } else {
                    List<Technology> usedTools = technologyService.getUsedTechnologies(repo, technologyService.getAllTechnologiesFromFile(args[1]));
                    addRepoInDB(repo, usedTools, TOOLS_RANKING);
                }
            } else {
                logTechnologiesByCategory(args);
            }
        }
    }

    private static void analyzeTopRepositories(String[] args) {
        if(args.length != 2) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path to file containing top repositories, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            try (CSVReader reader = new CSVReader(new FileReader(args[0]))) {
                List<String[]> csvLines = reader.readAll();
                csvLines.forEach(csvLine -> {
                    String path = GITHUB_REPOSITORY_LINK_START + Arrays.stream(csvLine).toList().get(0);
                    var usedTechs = technologyService.getUsedTechnologies(path, technologyService.getAllTechnologiesFromFile(args[1]));
                    addRepoInDB(path, usedTechs, TOOLS_RANKING);
                });
            } catch (IOException | CsvException e) {
                log.error("Exception occurred while parsing CSV file!");
                e.printStackTrace();
            }
        }
    }

    private static List<String> findRepositoriesToAnalyse(String[] args) {
        List<String> repos = new ArrayList<>();
        if(args.length != 1) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path to CSV file with repositories.");
        }
        else {
            try (CSVReader reader = new CSVReader(new FileReader(args[0]))) {
                List<String[]> csvLines = reader.readAll();
                csvLines.forEach(csvLine -> {
                    String repo = GITHUB_REPOSITORY_LINK_START + Arrays.stream(csvLine).toList().get(0);
                    if(dbService.getNumberOfSpecificDocumentsFromCollection(Map.of("repository", repo), ANALYSED_REPOSITORIES) == 0) {
                        repos.add(Arrays.stream(csvLine).toList().get(0));
                    }
                });
            } catch (IOException | CsvException e) {
                log.error("Exception occurred while parsing CSV file!");
                e.printStackTrace();
            }
        }

        return repos;
    }

    private static void addRepoInDB(String repo, List<Technology> usedTools, String collection) {
        dbService.addDocumentToCollection(Map.of("repository", repo, "usedTools", usedTools.size()), ANALYSED_REPOSITORIES);
        dbService.addDocumentToCollection(Map.of("repository", repo, "date", LocalDate.now().toString(), "usedTools", usedTools.stream().map(Technology::getName).collect(Collectors.toList())), REPOSITORY_DB);
        usedTools.forEach(t -> {
            Document tool = dbService.findDocumentFromCollection(Map.of("name", t.getName()), collection);
            if(tool != null && !tool.isEmpty()) {
                dbService.updateDocumentInCollection(tool, Map.of("timesUsed", (int) tool.get("timesUsed") + 1), collection);
            }
            else {
                dbService.addDocumentToCollection(Map.of("name", t.getName(), "category", t.getCategory().toString(), "timesUsed", 1), collection);
            }
        });
    }
}
