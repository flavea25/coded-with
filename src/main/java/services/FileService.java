package services;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class FileService {

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
}
