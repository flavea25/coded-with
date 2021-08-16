package services.basic;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import services.TechnologyService;
import technologies.Technology;

import java.util.*;

@Slf4j
public class BasicTechnologyServiceImpl extends TechnologyService {

    @Inject
    BasicFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String path, List<Technology> allTechnologies) {
        if(allTechnologies == null || allTechnologies.isEmpty()) {
            log.info("Checking for no technologies - please check your JSON file!");
            return new ArrayList<>();
        }

        List<Technology> technologies = new ArrayList<>();

        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        fileService.findFilesAndFolders(path, filePaths, fileNames);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                case FILE_NAME:
                    if (fileNames.contains(t.getContent()) && foundTechnologyInPaths(filePaths, t)) {
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
        });

        fileService.deleteClonedRepository();

        return technologies;
    }

    private boolean foundTechnologyInPaths(List<String> filePaths, Technology t) {
        if(t.getCondition() == null || t.getCondition().isBlank()) {
            return true;
        }

        if("FILE_CONTENT".equals(t.getRuleType().toString())) {
            return filePaths.stream()
                    .filter(p -> p.endsWith(t.getCondition()))
                    .anyMatch(p -> fileService.isTextInFile(p, t.getContent()));
        }
        else if("FILE_NAME".equals(t.getRuleType().toString())) {
            return filePaths.stream()
                    .anyMatch(p -> p.endsWith(t.getCondition() + "/" + t.getContent()));
        }

        return true;
    }
}
