package services;

import technologies.Category;
import technologies.Technology;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class TechnologyService {

    public abstract List<Technology> getUsedTechnologies(String path);

    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(String root) {
        List<Technology> technologies = getUsedTechnologies(root);
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
