import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import services.MyInjector;
import services.TechnologyService;
import services.database.MongoService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    private static final String DATABASE_NAME = "coded-with";

    private static final String COLLECTION_NAME = "tools";

    public static void main(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else {
            analyzeRepositoryAndUpdateDB(args);
        }
    }

    private static void analyzeRepositoryAndUpdateDB(String[] args) {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);
        MongoService dbService = injector.getInstance(MongoService.class);
        dbService.createDefaultConnection();
        dbService.useDatabase(DATABASE_NAME);

        technologyService.getUsedTechnologies(args[0], technologyService.getAllTechnologiesFromFile(args[1])).forEach(t -> {
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

        dbService.closeConnection();
    }

    private static void initDBForNewTechnologies(String pathToTechnologies) {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);
        MongoService dbService = injector.getInstance(MongoService.class);
        dbService.createDefaultConnection();
        dbService.useDatabase(DATABASE_NAME);

        technologyService.getAllTechnologiesFromFile(pathToTechnologies).forEach(t ->
                dbService.addDocumentToCollection(Map.of("category", t.getCategory().toString(),
                                                         "name", t.getName(),
                                                         "timesUsed", 0),
                                                  COLLECTION_NAME));

        dbService.closeConnection();
    }

    private static void printTechnologiesByCategory(String pathToProject, String pathToTechnologies) {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory(pathToProject, technologyService.getAllTechnologiesFromFile(pathToTechnologies))
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
