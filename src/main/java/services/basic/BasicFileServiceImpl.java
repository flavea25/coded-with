package services.basic;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class BasicFileServiceImpl implements BasicFileService{

    private static final String PATH_TO_CLONE = "cloned repositories/"; //TODO if I keep them : src/main/resources

    private static final String REPOSITORY_LINK_START = "https://github.com/";

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

    @Override
    public void findFilesAndFolders(String source, List<String> filePaths, List<String> fileNames) {
        boolean isRepository = source.startsWith(REPOSITORY_LINK_START);
        if(isRepository) {
            String path = PATH_TO_CLONE + source.substring(source.indexOf(REPOSITORY_LINK_START) + REPOSITORY_LINK_START.length());
            cloneRepositoryAtPath(source, path);
            findFilesAndFoldersLocally(path, filePaths, fileNames);
        }
        else {
            findFilesAndFoldersLocally(source, filePaths, fileNames);
        }
    }

    private void findFilesAndFoldersLocally(String root, List<String> filePaths, List<String> fileNames) {
        File file = new File(root);

        if(file.isDirectory()) {
            fileNames.add(file.getName());
            for(File f: Objects.requireNonNull(file.listFiles())) {
                findFilesAndFolders(root + "/" + f.getName(), filePaths, fileNames);
            }
        }
        else {
            fileNames.add(file.getName());
            filePaths.add(root);
        }
    }

    private void cloneRepositoryAtPath(String repositoryUrl, String path) {
        try { //TODO if exists -> delete
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(Paths.get(path).toFile())
                    .call();
        } catch (GitAPIException e) {
            log.error("Exception occurred while cloning repository!");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteClonedRepository() {
        try { //TODO check if folder exists!
            FileUtils.delete(new File(PATH_TO_CLONE), 1);
        } catch (IOException e) {
            log.error("Exception occurred while deleting folder!");
            e.printStackTrace();
        }
    }

    private void printBranches(String repositoryUrl) {
        try {
            Git.lsRemoteRepository()
                    .setRemote(repositoryUrl)
                    .call()
                    .forEach(c -> log.info(c.getName()));
        } catch (GitAPIException e) {
            log.error("Exception occurred while printing repository branches!");
            e.printStackTrace();
        }
    }

    private void printFileLines(String root) {
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
}
