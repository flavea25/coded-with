package services.repository;

import com.google.inject.Inject;
import services.TechnologyService;
import technologies.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GithubTechnologyServiceImpl extends TechnologyService implements RepositoryTechnologyService {

    @Inject
    RepositoryFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String repository) {
        List<Technology> technologies = new ArrayList<>();

        List<Technology> allTechnologies = Arrays.asList(Technology.values());
        String repositoryName = fileService.getRepositoryNameFromUrl(repository);
        Set<String> folderNames = fileService.getDirectoriesFromRepository(repositoryName);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                    if (folderNames.contains(t.getCondition())) {
                        technologies.add(t);
                    }
                    break;
                case FILE_NAME:
                    if (fileService.foundFileInRepository(t.getCondition(), repositoryName)) {
                        technologies.add(t);
                    }
                    break;
                case FILE_CONTENT:
                    if(fileService.isTextInFile(repositoryName, t.getPathEnding(), t.getCondition())) {
                        technologies.add(t);
                    }
                    break;
                default: break;
            }
        });

        return technologies;
    }
}
