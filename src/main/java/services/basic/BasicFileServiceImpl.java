package services.basic;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class BasicFileServiceImpl implements BasicFileService{

    private static final String PATH_TO_CLONE = "Downloads"; //TODO find acceptable path??

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
    public void findFilesAndFolders(String path, List<String> filePaths, List<String> fileNames) {
        boolean isRepository = path.startsWith("https://");
        if(isRepository) {
            cloneRepositoryBranchAtPath(path, "master", PATH_TO_CLONE); //TODO get branch from link???
            findFilesAndFoldersLocally(PATH_TO_CLONE, filePaths, fileNames); //TODO check if it's really there
            deleteClonedRepository(PATH_TO_CLONE); //TODO check if it's really there
        }
        else {
            findFilesAndFoldersLocally(path, filePaths, fileNames);
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

    private void cloneRepositoryBranchAtPath(String repositoryUrl, String branch, String path) { //TODO return string of the actual path..?
        try {
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setBranch(branch)
                    .setDirectory(Paths.get(path).toFile())
                    .call();
        } catch (GitAPIException e) {
            log.error("Exception occurred while cloning repository!");
            e.printStackTrace();
        }
    }

    private void deleteClonedRepository(String path) {
        //TODO implement
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
