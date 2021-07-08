import com.google.inject.Guice;
import com.google.inject.Injector;
import services.*;

import java.io.IOException;

public class Main {
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    //GitHub: https://github.com/flavea25/licenta SAU /SVV

    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new MyInjector());

        //TODO normal fileService
//        TechnologyService technologyService = injector.getInstance(BasicTechnologyServiceImpl.class);
//        BasicFileService fileService = injector.getInstance(BasicFileService.class);
//
//        fileService.printAllFilesFromFolder("C:/Users/flavi/git/SVV", "");
//        fileService.printFileLines("C:/Users/flavi/git/SVV/webserver/.project");
//
//        fileService.printAllFilesFromFolder("C:/Users/flavi/IdeaProjects/firstmaven/.git", "");
//        fileService.printFileLines("C:/Users/flavi/IdeaProjects/firstmaven/.git/config");
//
//        technologyService.getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven")
//                         .forEach((category, technologies) -> System.out.println(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
