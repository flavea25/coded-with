import com.google.inject.Guice;
import com.google.inject.Injector;
import services.*;

public class Main {
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MyInjector());
        TechnologyService technologyService = injector.getInstance(TechnologyService.class);
        FileService fileService = injector.getInstance(FileService.class);

//        fileService.printAllFilesFromFolder("C:/Users/flavi/git/SVV", "");
//        fileService.printFileLines("C:/Users/flavi/git/SVV/webserver/.project");

//        fileService.printAllFilesFromFolder("C:/Users/flavi/IdeaProjects/firstmaven/.git", "");
//        fileService.printFileLines("C:/Users/flavi/IdeaProjects/firstmaven/.git/config");

//        technologyService.getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven")
//                         .forEach((category, technologies) -> System.out.println(category.name() + ": " + technologyService.getTechnologiesNames(technologies)));
    }
}
