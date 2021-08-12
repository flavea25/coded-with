package services.repository;

import com.google.inject.Inject;
import services.TechnologyService;
import technologies.Technology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GithubTechnologyServiceImpl extends TechnologyService implements RepositoryTechnologyService {

    @Inject
    RepositoryFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String repository, List<Technology> allTechnologies) {
        List<Technology> technologies = new ArrayList<>();

        String repositoryName = fileService.getRepositoryNameFromUrl(repository);
        Set<String> folderNames = fileService.getDirectoriesFromRepository(repositoryName);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                    if (folderNames.contains(t.getContent())) {
                        technologies.add(t);
                    }
                    break;
                case FILE_NAME:
                    if (fileService.foundFileInRepository(t.getContent(), repositoryName)) {
                        technologies.add(t);
                    }
                    break;
                case FILE_CONTENT:
                    if(fileService.isTextInFile(repositoryName, t.getCondition(), t.getContent())) {
                        technologies.add(t);
                    }
                    break;
                default: break;
            }
        });

        return technologies;
    }
}
