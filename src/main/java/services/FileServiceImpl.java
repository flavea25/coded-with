package services;

import lombok.extern.slf4j.Slf4j;
import technologies.Technology;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
public class FileServiceImpl implements FileService{

    public void printFileLines(String root) {
        try {
            File file = new File(root);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                log.info("\t" + scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            log.error("Error when opening file!");
            e.printStackTrace();
        }
    }

    public void printAllFilesFromFolder(String root, String indents) {
        File file = new File(root);

        if(file.isDirectory()) {
            log.info(indents + file.getName() + ":");
            for(File f: Objects.requireNonNull(file.listFiles())) {
                printAllFilesFromFolder(root + "/" + f.getName(), indents + "\t");
            }
        }
        else {
            log.info(indents + file.getName());
        }
    }

    @Override
    public List<String> getFilePaths(String root) {
        List<String> filePaths = new ArrayList<>();
        File file = new File(root);

        if(file.isDirectory()) {
            for(File f: Objects.requireNonNull(file.listFiles())) {
                filePaths.addAll(getFilePaths(root + "/" + f.getName()));
            }
        }
        else {
            filePaths.add(root);
        }

        return filePaths;
    }

    @Override
    public List<String> getFolderNames(String root) {
        List<String> folderNames = new ArrayList<>();
        File file = new File(root);

        if(file.isDirectory()) {
            folderNames.add(file.getName());
            for(File f: Objects.requireNonNull(file.listFiles())) {
                folderNames.addAll(getFolderNames(root + "/" + f.getName()));
            }
        }

        return folderNames;
    }

    @Override
    public void findFilesAndFolders(String root, List<String> filePaths, List<String> files, List<String> folders) {
        File file = new File(root);

        if(file.isDirectory()) {
            folders.add(file.getName());
            for(File f: Objects.requireNonNull(file.listFiles())) {
                findFilesAndFolders(root + "/" + f.getName(), filePaths, files, folders);
            }
        }
        else {
            files.add(file.getName());
            filePaths.add(root);
        }
    }

    @Override
    public boolean foundTechnologyInPaths(List<String> filePaths, Technology t) {
        var oPath = filePaths.stream()
                                            .filter(p -> t.getPathEnding() == null || p.endsWith(t.getPathEnding()))
                                            .filter(p -> isTextInFile(p, t.getCondition()))
                                            .findAny();
        return oPath.isPresent();
    }

    @Override
    public boolean isTextInFile(String root, String text) {
        try {
            File file = new File(root);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if(scanner.nextLine().contains(text)) {
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            log.error("Error when parsing file!");
            e.printStackTrace();
        }

        return false;
    }
}
