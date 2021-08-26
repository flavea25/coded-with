import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;
 import services.database.MongoService;

import java.util.Map;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    public static void main(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else {
            MongoService dbService = injector.getInstance(MongoService.class);
            dbService.createDefaultConnection();
            dbService.useDatabase("test2");

            var x = dbService.findDocumentsFromCollection(Map.of(), "testy");
            while(x.hasNext()) {
                System.out.println(x.next().toString());
            }

            dbService.updateDocumentInCollection(Map.of(),
                                                 Map.of("age", 10),
                                    "testy");

            dbService.closeConnection();

//            TechnologyService technologyService = Guice.createInjector(new MyInjector()).getInstance(TechnologyService.class);
//
//            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
//                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
        }
    }
}
