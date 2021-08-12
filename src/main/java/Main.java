import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;
import services.repository.GithubTechnologyServiceImpl;
import services.repository.RepositoryTechnologyService;
import technologies.Technology;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {
    private static final Injector injector = Guice.createInjector(new MyInjector());

    public static void main(String[] args) {
        List<Technology> allTechnologies = new ArrayList<>();
        allTechnologies.add(new Technology("Github", "FILE_CONTENT", "IDE", "url = https://github.com/", ".git/config")); //TODO import from json file
        runIntermediateMain(allTechnologies);
    }

    //GitHub: https://github.com/flavea25/licenta OR /SVV
    private static void runComplexMain(List<Technology> allTechnologies) {
        GithubTechnologyServiceImpl technologyService = (GithubTechnologyServiceImpl) injector.getInstance(RepositoryTechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/SVV", allTechnologies)
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    private static void runBasicMain(List<Technology> allTechnologies) {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven", allTechnologies)
                .forEach((category, technologies) -> System.out.println(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    //GitHub: "https://github.com/flavea25/licenta" OR /SVV
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    private static void runIntermediateMain(List<Technology> allTechnologies) {
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/SVV", allTechnologies)
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
