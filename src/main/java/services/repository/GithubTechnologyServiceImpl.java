package services.repository;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import services.TechnologyService;
import technologies.Technology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class GithubTechnologyServiceImpl extends TechnologyService implements RepositoryTechnologyService {

    @Inject
    RepositoryFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String repository, List<Technology> allTechnologies) {
        if(allTechnologies == null) {
            log.info("Checking for no technologies - please check your JSON file!");
            return new ArrayList<>();
        }

        List<Technology> technologies = new ArrayList<>();

        String repositoryName = fileService.getRepositoryNameFromUrl(repository);
        Set<String> folderNames = fileService.getDirectoriesFromRepository(repositoryName);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                    if (t.getContents().stream().anyMatch(folderNames::contains)) {
                        technologies.add(t);
                    }
                    break;
                case FILE_NAME:
                    if (fileService.foundAnyFileInRepository(t.getContents(), t.getConditions(), repositoryName)) {
                        technologies.add(t);
                    }
                    break;
                case FILE_CONTENT:
                    if(fileService.isAnyTextInFile(repositoryName, t.getConditions(), t.getContents())) {
                        technologies.add(t);
                    }
                    break;
                default: break;
            }
        });

        return technologies;
    }
}
