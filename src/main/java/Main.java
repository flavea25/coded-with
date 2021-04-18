import services.FileService;
import services.FileServiceImpl;
import services.TechnologyService;
import services.TechnologyServiceImpl;
import technologies.Technology;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static String getTechnologiesNames(List<Technology> technologies) { //TODO move method in technologyService?
        AtomicReference<String> result = new AtomicReference<>("");

        technologies.forEach(t -> result.getAndSet(t.getName() + ", "));

        return result.get();
    }

    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"

    public static void main(String[] args) {
        TechnologyService technologyService = new TechnologyServiceImpl(); //TODO inject
        FileServiceImpl fileService = new FileServiceImpl();

//        fileService.printAllFilesFromFolder("C:/Users/flavi/git/SVV", "");
//        fileService.printFileLines("C:/Users/flavi/git/SVV/webserver/.project");

//        fileService.printAllFilesFromFolder("C:/Users/flavi/IdeaProjects/firstmaven/.git", "");
//        fileService.printFileLines("C:/Users/flavi/IdeaProjects/firstmaven/.git/config");

//        technologyService.getUsedTechnologiesByCategory("C:/Users/flavi/IdeaProjects/firstmaven")
//                         .forEach((category, technologies) -> System.out.println(category.name() + ": " + getTechnologiesNames(technologies)));
    }
}
