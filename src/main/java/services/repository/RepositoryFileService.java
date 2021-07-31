package services.repository;

import services.FileService;

import java.util.List;

public interface RepositoryFileService extends FileService {
    boolean foundFileInRepository(String fileName, String repositoryName);

    List<String> getDirectoriesFromRepository(String repository);
}
