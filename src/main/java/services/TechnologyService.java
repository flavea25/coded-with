package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helpers.CodedWithConstants;
import lombok.extern.slf4j.Slf4j;
import technologies.Category;
import technologies.Technology;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
public abstract class TechnologyService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public abstract List<Technology> getUsedTechnologies(String path, List<Technology> allTechnologies);

    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(String root, List<Technology> allTechnologies){
        return getUsedTechnologiesByCategory(getUsedTechnologies(root, allTechnologies));
    }

    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(List<Technology> usedTechnologies){
        Map<Category, List<Technology>> sortedTechnologies = new HashMap<>();

        log.info("Sorting used technologies...");
        Arrays.asList(Category.values()).forEach(c -> sortedTechnologies.put(c, new ArrayList<>()));

        usedTechnologies.forEach(t -> {
            Category c = t.getCategory() != null ? t.getCategory() : Category.OTHER;
            List<Technology> existingTechnologies = sortedTechnologies.get(c);
            existingTechnologies.add(t);
            sortedTechnologies.replace(c, existingTechnologies);
        });

        Map<Category, List<Technology>> actualSortedTechnologies = new HashMap<>();
        sortedTechnologies.forEach((key, value) -> {
            if(!sortedTechnologies.get(key).isEmpty()) {
                actualSortedTechnologies.put(key, value);
            }
        });

        return actualSortedTechnologies;
    }

    public Map<Category, List<Technology>> getUsedTechnologiesByCategory(List<Technology> allTools, List<String> usedTools) {
        return getUsedTechnologiesByCategory(usedTools.stream()
                .map(toolName -> allTools.stream().filter(t -> t.getName().equals(toolName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public String getTechnologiesNames(List<Technology> technologies) {
        AtomicReference<String> result = new AtomicReference<>("");

        technologies.forEach(t -> result.set(result.get() + t.getName() + ", "));

        return result.get();
    }

    public List<Technology> getAllTechnologiesFromFile(String path) {
        log.info("Loading all technologies...");

        if("default".equals(path)) {
            path = CodedWithConstants.PATH_TO_TECHNOLOGIES;
        }
        File source = new File(path);

        if(source.exists()) {
            try {
                return Arrays.stream(GSON.fromJson(Files.readString(Path.of(path)), Technology[].class)).toList();
            } catch (IOException e) {
                log.error("Not a (correct) JSON file!");
            }
        }
        else {
            log.error("Incorrect path to JSON file! - nonexistent file!");
        }

        return null;
    }
}
