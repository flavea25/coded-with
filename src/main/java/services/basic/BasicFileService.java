package services.basic;

import java.util.List;

public interface BasicFileService {
    boolean isTextInFile(String root, String text);

    void findFilesAndFolders(String root, List<String> filePaths, List<String> files);
}
