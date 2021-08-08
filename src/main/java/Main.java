import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;
import services.TechnologyService;
import services.basic.BasicTechnologyService;
import services.basic.BasicTechnologyServiceImpl;

@Slf4j
public class Main {
    public static void main(String[] args) {
        runComplexMain();
    }

    //GitHub: https://github.com/flavea25/licenta SAU /SVV
    private static void runComplexMain() {
        Injector injector = Guice.createInjector(new MyInjector());
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);

        technologyService.getUsedTechnologiesByCategory("https://github.com/flavea25/SVV")
                .forEach((category, technologies) -> log.info(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }

    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    private static void runBasicMain() {
        Injector injector = Guice.createInjector(new MyInjector());
        BasicTechnologyService technologyService = injector.getInstance(BasicTechnologyService.class);

        ((BasicTechnologyServiceImpl)technologyService).getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven")
                .forEach((category, technologies) -> System.out.println(category.name() + ": " + ((BasicTechnologyServiceImpl)technologyService).getTechnologiesNames(technologies)));
    }
}
