import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.client.MongoCursor;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    private static final String DATABASE_NAME = "coded-with";
    private static final String GITHUB_REPOSITORY_LINK_START = "https://github.com/";
    private static final String ANALYSED_REPOSITORIES = "analysed-repositories";
    private static final String REPOSITORY_DB = "repoDB";

    public static void main(String[] args) {
        logTechnologiesByCategory(args);
    }

    private static void logTechnologiesByCategory(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Path/link to project, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Path/link to project, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            TechnologyService technologyService = injector.getInstance(TechnologyService.class);

            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
        }
    }

    private static void saveRepositoryData(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. GitHub repository, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. GitHub repository, and \n\t 2. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            TechnologyService technologyService = injector.getInstance(TechnologyService.class);

            MongoService dbService = injector.getInstance(MongoService.class);
            dbService.createDefaultConnection();
            dbService.useDatabase(DATABASE_NAME);

//            if(dbService.findDocumentsFromCollection(Map.of("repository", args[0]), REPOSITORY_DB))   //TODO search for repository; if found "DO YOU WANT TO RE-ANALYSE?" else just analyse

            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));

            dbService.closeConnection();
        }
    }

    private static void findRepositoriesToAnalyse(String[] args) {
        if(args.length != 1) {
            log.error("Incorrect arguments!! - Please pass:\n\t 1. Path to CSV file with repositories.");
        }
        else {
            MongoService dbService = injector.getInstance(MongoService.class);
            dbService.createDefaultConnection();
            dbService.useDatabase(DATABASE_NAME);

            try (CSVReader reader = new CSVReader(new FileReader(args[0]))) {
                List<String[]> csvLines = reader.readAll();
                csvLines.forEach(csvLine -> {
                    String repo = GITHUB_REPOSITORY_LINK_START + Arrays.stream(csvLine).toList().get(0);
                    if(dbService.getNumberOfSpecificDocumentsFromCollection(Map.of("repository", repo), ANALYSED_REPOSITORIES) == 0) {
                        log.info(Arrays.stream(csvLine).toList().get(0));
                    }
                });
            } catch (IOException | CsvException e) {
                log.error("Exception occurred while parsing CSV file!");
                e.printStackTrace();
            }

            dbService.closeConnection();
        }
    }

    private static void analyzeTopRepositories(String[] args) {
        if(args.length < 3) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Collection name \n\t 2. Path to file containing top repositories, and \n\t 3. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else if(args.length > 3) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Collection name \n\t 2. Path to file containing top repositories, and \n\t 3. Path to a JSON file containing the searched-for technologies (or \"default\")!");
        }
        else {
            String collectionName = args[0];

            TechnologyService technologyService = injector.getInstance(TechnologyService.class);
            List<Technology> allTechnologies = technologyService.getAllTechnologiesFromFile(args[2]);

            MongoService dbService = injector.getInstance(MongoService.class);
            dbService.createDefaultConnection();
            dbService.useDatabase(DATABASE_NAME);

            try (CSVReader reader = new CSVReader(new FileReader(args[1]))) {
                List<String[]> csvLines = reader.readAll();
                csvLines.forEach(csvLine -> {
                    String path = GITHUB_REPOSITORY_LINK_START + Arrays.stream(csvLine).toList().get(0);

                    var usedTechs = technologyService.getUsedTechnologies(path, allTechnologies);
                    dbService.addDocumentToCollection(Map.of("repository", path, "usedTools", usedTechs.size()), ANALYSED_REPOSITORIES);
                    dbService.addDocumentToCollection(Map.of("repository", path, "usedTools", usedTechs.stream().map(Technology::getName).collect(Collectors.toList())), REPOSITORY_DB);

                    usedTechs.forEach(t -> {
                        Map<String, Object> dimensions = new HashMap<>();
                        dimensions.put("category", t.getCategory().toString());
                        dimensions.put("name", t.getName());

                        MongoCursor<Document> foundDocuments = dbService.findDocumentsFromCollection(dimensions, collectionName);
                        if(foundDocuments == null || !foundDocuments.hasNext()) {
                            dimensions.put("timesUsed", 1);
                            dbService.addDocumentToCollection(dimensions, collectionName);
                        }
                        else {
                            while(foundDocuments.hasNext()) {
                                Document toUpdate = foundDocuments.next();
                                Document incremented = new Document(dimensions);
                                incremented.put("timesUsed", (int)toUpdate.get("timesUsed") + 1);

                                dbService.updateDocumentInCollection(toUpdate, incremented, collectionName);
                            }
                        }
                    });
                });
            } catch (IOException | CsvException e) {
                log.error("Exception occurred while parsing CSV file!");
                e.printStackTrace();
            }

            dbService.closeConnection();
            log.info(LocalDateTime.now().toString());
        }
    }
}
