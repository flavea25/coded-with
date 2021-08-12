package services.basic;

import com.google.inject.Inject;
import services.TechnologyService;
import technologies.Technology;

import java.util.*;

public class BasicTechnologyServiceImpl extends TechnologyService {

    @Inject
    BasicFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String path) {
        List<Technology> technologies = new ArrayList<>();

        List<Technology> allTechnologies = Arrays.asList(Technology.values());
        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        fileService.findFilesAndFolders(path, filePaths, fileNames);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                case FILE_NAME:
                    if (fileNames.contains(t.getContent())) {
                        technologies.add(t);
                    }
                    break;
                case FILE_CONTENT:
                    if(fileService.foundTechnologyInPaths(filePaths, t)) {
                        technologies.add(t);
                    }
                    break;
                default: break;
            }
        });

        return technologies;
    }
}
