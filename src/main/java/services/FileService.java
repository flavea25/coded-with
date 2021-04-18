package services;

import java.util.List;
import java.util.Map;

public interface FileService {
    List<String> getFilePaths(String root);
    List<String> getFolderNames(String root);
    boolean isTextInFile(String root, String text);
    void findFilesAndFolders(String root, List<String> filePaths, List<String> folders);
}
