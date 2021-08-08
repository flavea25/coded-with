package services.repository;

import java.util.Set;

public interface RepositoryFileService {
    boolean foundFileInRepository(String fileName, String repositoryName);

    Set<String> getDirectoriesFromRepository(String repositoryName);

    boolean isTextInFile(String repository, String path, String text);

    String getRepositoryNameFromUrl(String url);
}
