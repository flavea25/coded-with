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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    private static final String DATABASE_NAME = "coded-with";

    private static final String COLLECTION_NAME = "tools";

    private static final String GITHUB_REPOSITORY_LINK_START = "https://github.com/";

    public static void main(String[] args) {
        analyzeTopRepositories(args);
    }

    private static void analyzeTopRepositories(String[] args) {
        if(args.length < 3) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Collection name \n\t 2. Path to file containing top repositories, and \n\t 3. Path to a JSON file containing the searched-for technologies!");
        }
        else if(args.length > 3) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Collection name \n\t 2. Path to file containing top repositories, and \n\t 3. Path to a JSON file containing the searched-for technologies!");
        }
        else {
            String collectionName = args[0];

            TechnologyService technologyService = injector.getInstance(TechnologyService.class);
            List<Technology> allTechnologies = technologyService.getAllTechnologiesFromFile(args[2]);

            MongoService dbService = injector.getInstance(MongoService.class);
            dbService.createDefaultConnection();
            dbService.useDatabase(DATABASE_NAME);
            dbService.deleteCollection(collectionName);
            dbService.createCollection(collectionName);

            allTechnologies.forEach(t -> dbService.addDocumentToCollection(Map.of("category", t.getCategory().toString(),
                                                                                  "name", t.getName(),
                                                                                  "timesUsed", 0),
                                                                           collectionName));

            try (CSVReader reader = new CSVReader(new FileReader(args[1]))) {
                List<String[]> csvLines = reader.readAll();
                csvLines.forEach(csvLine -> {
                    String path = GITHUB_REPOSITORY_LINK_START + Arrays.stream(csvLine).toList().get(0);
                    System.out.println(path);
                    if(!path.endsWith("...")) {
                        technologyService.getUsedTechnologies(path, allTechnologies).forEach(t -> {
                            Map<String, Object> dimensions = new HashMap<>();
                            dimensions.put("category", t.getCategory().toString());
                            dimensions.put("name", t.getName());

                            MongoCursor<Document> foundDocuments = dbService.findDocumentsFromCollection(dimensions, COLLECTION_NAME);
                            if(foundDocuments == null || !foundDocuments.hasNext()) {
                                dimensions.put("timesUsed", 1);
                                dbService.addDocumentToCollection(dimensions, COLLECTION_NAME);
                            }
                            else {
                                while(foundDocuments.hasNext()) {
                                    Document toUpdate = foundDocuments.next();
                                    Document incremented = new Document(dimensions);
                                    incremented.put("timesUsed", (int)toUpdate.get("timesUsed") + 1);

                                    dbService.updateDocumentInCollection(toUpdate, incremented, COLLECTION_NAME);
                                }
                            }
                        });
                    }
                });
            } catch (IOException | CsvException e) {
                log.error("Exception occurred while parsing CSV file!");
                e.printStackTrace();
            }

            dbService.closeConnection();
        }
    }

    private static void logTechnologiesByCategory(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else {
            TechnologyService technologyService = injector.getInstance(TechnologyService.class);

            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
        }
    }
}
