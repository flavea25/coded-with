package services;

import technologies.Category;
import technologies.Technology;

import java.util.*;

public class TechnologyServiceImpl implements TechnologyService{
    private final FileService fileService = new FileServiceImpl(); //TODO inject & remove final?

    @Override
    public List<Technology> getUsedTechnologies(String root) {
        List<Technology> technologies = new ArrayList<>();

        List<Technology> allTechnologies = Arrays.asList(Technology.values());
        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> folderNames = new ArrayList<>();
        fileService.findFilesAndFolders(root, filePaths, fileNames, folderNames);

        allTechnologies.forEach(t -> {
            switch (t.getRuleType()) {
                case FOLDER_NAME:
                    if (folderNames.contains(t.getCondition())) {
                        technologies.add(t);
                    }
                    break;
                case FILE_NAME:
                    if (fileNames.contains(t.getCondition())) {
                        technologies.add(t);
                    }
                    break;
                case FILE_CONTENT:
                    //TODO for each filePath: get file -> if it contains content => add technology; probs outside this switch
                    break;
                default: break;
            }
        });

        return technologies;
    }

    @Override
    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(String root) {
        List<Technology> technologies = getUsedTechnologies(root);
        Map<Category, List<Technology>> sortedTechnologies = new HashMap<>();

        Arrays.asList(Category.values()).forEach(c -> sortedTechnologies.put(c, new ArrayList<>()));

        technologies.forEach(t -> {
            List<Technology> existingTechnologies = sortedTechnologies.get(t.getCategory());
            existingTechnologies.add(t);
            sortedTechnologies.replace(t.getCategory(), existingTechnologies);
        });

        //TODO maybe remove categories with no values?

        return sortedTechnologies;
    }
}
