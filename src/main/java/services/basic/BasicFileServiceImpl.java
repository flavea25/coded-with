package services.basic;

import helpers.CodedWithConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class BasicFileServiceImpl implements BasicFileService{

    private static String PATH_TO_CLONE = "";

    @Override
    public boolean isAnyTextInFile(String root, List<String> texts) {
        try {
            return Files.readAllLines(Path.of(root)).stream()
                    .anyMatch(l -> texts.stream().anyMatch(l::contains));
        } catch (IOException e) {
            log.error("Exception occurred when reading lines from a file: " + root);
        }

        return false;
    }

    @Override
    public void findFilesAndFolders(String source, List<String> filePaths, List<String> fileNames){
        boolean isRepository = source.startsWith(CodedWithConstants.GITHUB_REPOSITORY_LINK_START);
        if(isRepository) {
            PATH_TO_CLONE += RandomStringUtils.randomAlphanumeric(16) + "/";    //a randomly generated Base64 String to avoid overwriting existing folders
            cloneRepositoryAtPath(source, PATH_TO_CLONE + source.substring(CodedWithConstants.GITHUB_REPOSITORY_LINK_START.length()) + "/");
            log.info("Finding files & folders...");
            findFilesAndFoldersLocally(PATH_TO_CLONE, filePaths, fileNames);
        }
        else {
            log.info("Finding files & folders...");
            findFilesAndFoldersLocally(source, filePaths, fileNames);
        }
    }

    private void findFilesAndFoldersLocally(String root, List<String> filePaths, List<String> fileNames) {
        File file = new File(root);

        if(!file.isHidden()) {
            fileNames.add(file.getName());
            if(file.isDirectory()) {
                for(File f: Objects.requireNonNull(file.listFiles())) {
                    findFilesAndFoldersLocally(root + "/" + f.getName(), filePaths, fileNames);
                }
            }
            else {
                filePaths.add(root);
            }
        }
    }

    private void cloneRepositoryAtPath(String repositoryUrl, String path) {
        log.info("Cloning repository...");
        try {
            Git git = Git.cloneRepository()
                         .setURI(repositoryUrl)
                         .setDirectory(Paths.get(path).toFile())
                         .call();
            git.close();
        } catch (GitAPIException e) {
            log.error("Exception occurred while cloning repository!");
        }
    }

    private void deleteDestinationIfExistent(String path) {
        File toDelete = new File(path);
        if(!"".equals(path) && toDelete.exists()) {
            log.info("Deleting local copy of repository...");
            try {
                FileUtils.delete(toDelete, 1);
            } catch (IOException e) {
                log.error("Exception occurred while deleting folder!");
            }
        }
    }

    @Override
    public void deleteClonedRepository() {
        if(!"".equals(PATH_TO_CLONE)) {
            deleteDestinationIfExistent(PATH_TO_CLONE);
        }
    }
}
