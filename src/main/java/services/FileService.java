package services;

import technologies.Technology;

import java.util.List;

public interface FileService {
    List<String> getFilePaths(String root);
    List<String> getFolderNames(String root);
    boolean isTextInFile(String root, String text);
    void findFilesAndFolders(String root, List<String> filePaths, List<String> files, List<String> folders);

    boolean foundTechnologyInPaths(List<String> filePaths, Technology t);
    void printAllFilesFromFolder(String s, String s1);
}
