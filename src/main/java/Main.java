import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import services.repository.GithubFileServiceImpl;

import java.io.IOException;

@Slf4j
public class Main {
    //ROOT: "C:/Users/flavi/IdeaProjects/firstmaven"
    //SVV: "C:/Users/flavi/git/SVV"
    //GitHub: https://github.com/flavea25/licenta SAU /SVV

    public static void main(String[] args) throws IOException, GitAPIException {
//        Injector injector = Guice.createInjector(new MyInjector());
//        RepositoryFileService fileService = injector.getInstance(RepositoryFileService.class);
        GithubFileServiceImpl fileService = new GithubFileServiceImpl();

        log.info("" + fileService.foundFileInRepository(".idea", "flavea25/SVV"));


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
