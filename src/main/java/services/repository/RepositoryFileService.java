package services.repository;

import java.util.List;
import java.util.Set;

public interface RepositoryFileService {
    boolean foundAnyFileInRepository(List<String> fileNames, List<String> conditions, String repositoryName);

    Set<String> getDirectoriesFromRepository(String repositoryName);

    boolean isAnyTextInFile(String repository, List<String> paths, List<String> texts);

    String getRepositoryNameFromUrl(String url);
}
