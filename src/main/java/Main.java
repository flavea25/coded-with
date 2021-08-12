import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;
import services.repository.GithubTechnologyServiceImpl;
import services.repository.RepositoryTechnologyService;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    public static void main(String[] args) {
        runIntermediateMain();
    }

    //GitHub: https://github.com/flavea25/licenta OR /SVV
    private static void runComplexMain() {
        GithubTechnologyServiceImpl technologyService = (GithubTechnologyServiceImpl) injector.getInstance(RepositoryTechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/SVV")
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    private static void runBasicMain() {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven")
                .forEach((category, technologies) -> System.out.println(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    //GitHub: "https://github.com/flavea25/licenta" OR /SVV
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    private static void runIntermediateMain() {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/licenta/SVV")
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
