package services.repository;

import java.util.List;

public interface RepositoryFileService {
    boolean foundFileInRepository(String fileName, String repositoryName);

    List<String> getDirectoriesFromRepository(String repositoryName, String branchName);

    boolean isTextInFile(String repository, String root, String text);
}
