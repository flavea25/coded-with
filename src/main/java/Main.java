import com.google.inject.Guice;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;

@Slf4j
public class Main {
    //GitHub: "https://github.com/flavea25/licenta" OR /SVV
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    public static void main(String[] args) { //TODO take links/paths as arguments
        TechnologyService technologyService = Guice.createInjector(new MyInjector()).getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/SVV", technologyService.getAllTechnologiesFromFile("C:/Users/flavi/IdeaProjects/firstmaven/src/main/resources/technologies.json"))
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
