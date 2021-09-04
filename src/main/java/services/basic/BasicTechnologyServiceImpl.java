package services.basic;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import services.TechnologyService;
import technologies.BuildTools;
import technologies.Technology;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BasicTechnologyServiceImpl extends TechnologyService {

    @Inject
    BasicFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String path, List<Technology> allTechnologies) {
        if(allTechnologies == null || allTechnologies.isEmpty()) {
            log.info("Checking for zero technologies - please check your JSON file!");
            return new ArrayList<>();
        }

        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        fileService.findFilesAndFolders(path, filePaths, fileNames);
        List<Technology> searchedTechnologies = new ArrayList<>(allTechnologies);

        log.info("Searching for used technologies...");
        List<Technology> technologies = getTechnologiesFromDependencies(fileNames, filePaths, searchedTechnologies);
        searchedTechnologies.removeAll(technologies);

        searchedTechnologies.forEach(t -> {
            if(t.getRuleType() != null) {
                switch (t.getRuleType()) {
                    case FOLDER_NAME:
                    case FILE_NAME:
                        if (t.getContents() != null && t.getContents().stream().anyMatch(fileNames::contains) && foundTechnologyInPaths(filePaths, t)) {
                            technologies.add(t);
                        }
                        break;
                    case FILE_CONTENT:
                        if(foundTechnologyInPaths(filePaths, t)) {
                            technologies.add(t);
                        }
                        break;
                    default: break;
                }
            }
        });

        fileService.deleteClonedRepository();
        return technologies;
    }

    private List<Technology> getTechnologiesFromDependencies(List<String> fileNames, List<String> filePaths, List<Technology> allTechnologies) {
        List<Technology> foundTechnologies = new ArrayList<>();
        List<Technology> searchedTechnologies = allTechnologies.stream().filter(t -> t.getDependencies() != null && !t.getDependencies().isEmpty()).collect(Collectors.toList());

        Arrays.stream(BuildTools.values())
                .filter(bt -> fileNames.contains(bt.getFileName()))
                .forEach(bt -> filePaths.stream()
                        .filter(p -> p.endsWith(bt.getFileName()))
                        .forEach(p -> {
                            Set<Technology> foundNow = findDependenciesFromFile(p, searchedTechnologies);
                            foundTechnologies.addAll(foundNow);
                            searchedTechnologies.removeAll(foundNow);
                        }));

        return foundTechnologies;
    }

    private Set<Technology> findDependenciesFromFile(String path, List<Technology> searchedTechnologies) {
        Set<Technology> foundTechnologies = new HashSet<>();
        String fileContent = null;

        try {
            fileContent = Files.readString(Path.of(path));
        } catch (IOException e) {
            log.error("Exception occurred when reading lines from a file: " + path);
        }

        if(fileContent != null && !fileContent.isBlank()) {
            String finalFileContent = fileContent;
            searchedTechnologies.forEach(t -> t.getDependencies().forEach(d -> {
                if(finalFileContent.contains(d)) {
                    foundTechnologies.add(t);
                }
            }));
        }

        return foundTechnologies;
    }

    private boolean foundTechnologyInPaths(List<String> filePaths, Technology t) {
        if(t.getConditions() == null || t.getConditions().isEmpty()) {
            return true;
        }

        if("FILE_CONTENT".equals(t.getRuleType().toString())) {
            return filePaths.stream()
                    .filter(p -> t.getConditions().stream().anyMatch(p::endsWith))
                    .anyMatch(p -> fileService.isAnyTextInFile(p, t.getContents()));
        }
        else if("FILE_NAME".equals(t.getRuleType().toString())) {
            return filePaths.stream()
                    .anyMatch(p -> t.getConditions().stream().anyMatch(f -> t.getContents().stream().anyMatch(c -> p.endsWith(f + "/" + c))));
        }

        return true;
    }
}
