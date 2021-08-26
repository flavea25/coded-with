import com.google.inject.Guice;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Program started...");
        if(args.length < 2) {
            log.error("Not enough arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else if(args.length > 2) {
            log.error("Too many arguments!! - Please pass:\n\t 1. Path/link to analyzed project, and \n\t 2. Path to a JSON file containing the searched-for technologies!");
        }
        else {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase("test");
            db.createCollection("testy");
            mongoClient.getDatabaseNames().forEach(System.out::println);
            mongoClient.close();


//            TechnologyService technologyService = Guice.createInjector(new MyInjector()).getInstance(TechnologyService.class);
//
//            technologyService.getUsedTechnologiesByCategory(args[0], technologyService.getAllTechnologiesFromFile(args[1]))
//                    .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
        }
    }
}
