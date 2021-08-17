package technologies;

import lombok.Getter;

import java.util.List;

@Getter
public class Technology {
    private final String name;

    private final RuleType ruleType;

    private final Category category;

    private final List<String> contents;

    private final List<String> conditions; //can be containing file for FILE_CONTENT or containing folder for FILE_NAME

    private final List<String> dependencies;

    public Technology(String name, String ruleType, String category, List<String> contents, List<String> conditions, List<String> dependencies) {
        this.name = name;
        this.ruleType = RuleType.valueOf(ruleType);
        this.category = Category.valueOf(category);
        this.contents = contents;
        this.conditions = conditions;
        this.dependencies = dependencies;
    }
}
