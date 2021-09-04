package helpers;

import com.google.inject.Inject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import services.TechnologyService;
import services.database.MongoService;
import technologies.Category;
import technologies.Technology;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CodedWithProgram implements MyHelper {

    @Inject
    TechnologyService technologyService;
    
    @Inject
    MongoService dbService;

    public void run(String[] args) {
        if(args.length != 2) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path/link to project OR csv file with a list of repositories, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            log.info("Program started...");
            String path = args[0];
            List<Technology> searchedTools = technologyService.getAllTechnologiesFromFile(args[1]);

            log.info("Connecting to DB...");
            dbService.createDefaultConnection();
            dbService.useDatabase(CodedWithConstants.DATABASE_NAME);

            if(path.startsWith(CodedWithConstants.GITHUB_REPOSITORY_LINK_START)) {
                saveRepositoryData(path, searchedTools);
            }
            else {
                if(path.endsWith(".csv")) {
                    analyseListOfRepositories(path, searchedTools);
                }
                else {
                    logTechnologiesByCategory(technologyService.getUsedTechnologiesByCategory(path, searchedTools));
                }
            }

            log.info("Disconnecting from DB...");
            dbService.closeConnection();
        }
    }

    private void saveRepositoryData(String repo, List<Technology> allTechnologies) {
        Document found = dbService.findDocumentFromCollection(Map.of("repository", repo), CodedWithConstants.REPOSITORY_DB);
        if (found != null && !found.isEmpty()) {
            System.out.println("Repository analysed at " + found.get("date") + "\nFound technologies: ");
            List<String> tools = (List<String>) found.get("usedTools");
            tools.forEach(t -> System.out.println("\t" + t));

            System.out.println("Do you want to re-analyse it? (y/n)");
            Scanner in = new Scanner(System.in);
            char option = in.next().charAt(0);
            if (option == 'y') {
                removeToolsFromDB(tools);
                replaceRepoInDB(repo, allTechnologies);
            }
        } else {
            addRepoInDB(repo, allTechnologies);
        }
    }

    private void analyseListOfRepositories(String repositories, List<Technology> allTools) {
        try (CSVReader reader = new CSVReader(new FileReader(repositories))) {
            List<String> csvLines = reader.readAll().stream()
                    .map(line -> Arrays.stream(line).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if(csvLines.get(0).contains("repository")) { //header
                csvLines.remove(0);
                csvLines.forEach(repo -> {
                    String path = CodedWithConstants.GITHUB_REPOSITORY_LINK_START + repo;
                    Document found = dbService.findDocumentFromCollection(Map.of("repository", path), CodedWithConstants.REPOSITORY_DB);
                    if(found == null || found.isEmpty()) {
                        addRepoInDB(path, allTools);
                    }
                });
            }
        } catch (IOException | CsvException e) {
            log.error("Exception occurred while parsing CSV file!");
        }
    }

    private void logTechnologiesByCategory(Map<Category, List<Technology>> foundTools) {
        foundTools.forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    private void addRepoInDB(String repo, List<Technology> searchedTools) {
        List<Technology> usedTools = technologyService.getUsedTechnologies(repo, searchedTools);

        dbService.addDocumentToCollection(Map.of("repository", repo, "usedTools", usedTools.size()), CodedWithConstants.ANALYSED_REPOSITORIES);
        dbService.addDocumentToCollection(Map.of("repository", repo, "date", LocalDate.now().toString(), "usedTools", usedTools.stream().map(Technology::getName).collect(Collectors.toList())), CodedWithConstants.REPOSITORY_DB);
        addToolsToDB(usedTools);

        logTechnologiesByCategory(technologyService.getUsedTechnologiesByCategory(usedTools));
    }

    private void replaceRepoInDB(String repo, List<Technology> searchedTools) {
        List<Technology> usedTools = technologyService.getUsedTechnologies(repo, searchedTools);

        dbService.updateDocumentInCollection(Map.of("repository", repo), Map.of("usedTools", usedTools.size()), CodedWithConstants.ANALYSED_REPOSITORIES);
        dbService.updateDocumentInCollection(Map.of("repository", repo), Map.of("date", LocalDate.now().toString(), "usedTools", usedTools.stream().map(Technology::getName).collect(Collectors.toList())), CodedWithConstants.REPOSITORY_DB);
        addToolsToDB(usedTools);

        logTechnologiesByCategory(technologyService.getUsedTechnologiesByCategory(usedTools));
    }

    private void addToolsToDB(List<Technology> usedTools) {
        usedTools.forEach(t -> {
            Document tool = dbService.findDocumentFromCollection(Map.of("name", t.getName()), CodedWithConstants.TOOLS_RANKING);
            if(tool != null && !tool.isEmpty()) {
                dbService.updateDocumentInCollection(tool, Map.of("timesUsed", (int) tool.get("timesUsed") + 1), CodedWithConstants.TOOLS_RANKING);
            }
            else {
                dbService.addDocumentToCollection(Map.of("name", t.getName(), "category", t.getCategory().toString(), "timesUsed", 1), CodedWithConstants.TOOLS_RANKING);
            }
        });
    }

    private void removeToolsFromDB(List<String> tools) {
        tools.forEach(t -> {
            Document tool = dbService.findDocumentFromCollection(Map.of("name", t), CodedWithConstants.TOOLS_RANKING);
            if (tool != null && !tool.isEmpty()) {
                dbService.updateDocumentInCollection(tool, Map.of("timesUsed", (int) tool.get("timesUsed") - 1), CodedWithConstants.TOOLS_RANKING);
            }
        });
    }
}
