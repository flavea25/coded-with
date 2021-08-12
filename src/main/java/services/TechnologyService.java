package services;

import com.google.inject.Inject;
import services.basic.BasicFileService;
import technologies.Category;
import technologies.Technology;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class TechnologyService {

    private static final String PATH_TO_CLONE = ""; //TODO add

    @Inject
    BasicFileService fileService;

    public abstract List<Technology> getUsedTechnologies(String path);

    private List<Technology> getUsedTechnologies(String root, boolean isRepository) {
        List<Technology> technologies;
        if(isRepository) {
            fileService.cloneRepositoryBranchAtPath(root, "master", PATH_TO_CLONE); //TODO get branch from link???
            technologies = getUsedTechnologies(PATH_TO_CLONE); //TODO check if it's really there
            fileService.deleteClonedRepository(PATH_TO_CLONE); //TODO check if it's really there
        }
        else {
            technologies = getUsedTechnologies(root);
        }
        return technologies;
    }

    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(String root) {
        List<Technology> technologies = getUsedTechnologies(root, false); //TODO analyze if it's a repository
        Map<Category, List<Technology>> sortedTechnologies = new HashMap<>();

        Arrays.asList(Category.values()).forEach(c -> sortedTechnologies.put(c, new ArrayList<>()));

        technologies.forEach(t -> {
            List<Technology> existingTechnologies = sortedTechnologies.get(t.getCategory());
            existingTechnologies.add(t);
            sortedTechnologies.replace(t.getCategory(), existingTechnologies);
        });

        Map<Category, List<Technology>> actualSortedTechnologies = new HashMap<>();
        sortedTechnologies.forEach((key, value) -> {
            if(!sortedTechnologies.get(key).isEmpty()) {
                actualSortedTechnologies.put(key, value);
            }
        });

        return actualSortedTechnologies;
    }

    public String getTechnologiesNames(List<Technology> technologies) {
        AtomicReference<String> result = new AtomicReference<>("");

        technologies.forEach(t -> result.getAndSet(t.getName() + ", "));

        return result.get();
    }
}
