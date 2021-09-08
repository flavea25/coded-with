package helpers;

import lombok.Getter;
import lombok.Setter;
import technologies.Category;
import technologies.Technology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnalysedRepository {
    private String project;

    private Map<String, List<String>> tools = new HashMap<>();

    public AnalysedRepository(String project, Map<Category, List<Technology>> tools) {
        this.project = project;
        tools.forEach((category, technologies) -> this.tools.put(category.name(), technologies.stream().map(Technology::getName).collect(Collectors.toList())));
    }
}
