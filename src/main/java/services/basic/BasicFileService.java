package services.basic;

import technologies.Technology;

import java.util.List;

public interface BasicFileService {
    boolean isTextInFile(String root, String text);

    List<String> getFilePaths(String root);

    List<String> getFolderNames(String root);

    void findFilesAndFolders(String root, List<String> filePaths, List<String> files, List<String> folders);

    boolean foundTechnologyInPaths(List<String> filePaths, Technology t);

    void printAllFilesFromFolder(String s, String s1);

    void printFileLines(String s);
}
