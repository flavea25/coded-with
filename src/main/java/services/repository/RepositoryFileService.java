package services.repository;

import services.FileService;

public interface RepositoryFileService extends FileService {
    boolean foundFileInRepository(String fileName, String repositoryUrl);

    String getOrgFromRepositoryUrl(String repositoryUrl);

    String getRepositoryNameFromUrl(String url);
}
