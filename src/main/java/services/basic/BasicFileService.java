package services.basic;

import java.util.List;

public interface BasicFileService {
    boolean isAnyTextInFile(String root, List<String> texts);

    void findFilesAndFolders(String root, List<String> filePaths, List<String> files);

    void deleteClonedRepository();
}
