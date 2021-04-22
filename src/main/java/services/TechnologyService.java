package services;

import technologies.Category;
import technologies.Technology;

import java.util.List;
import java.util.Map;

public interface TechnologyService {
    List<Technology> getUsedTechnologies(String root);
    Map<Category, List<Technology>> getUsedTechnologiesByCategory(String root);

    String getTechnologiesNames(List<Technology> technologies);
}
