package technologies;

import lombok.Getter;

@Getter
public class Technology {
    private final String name;

    private final RuleType ruleType;

    private final Category category;

    private final String content;

    private final String condition; //can be containing file for FILE_CONTENT or containing folder for FILE_NAME

    public Technology(String name, String ruleType, String category, String content, String condition) {
        this.name = name;
        this.ruleType = RuleType.valueOf(ruleType);
        this.category = Category.valueOf(category);
        this.content = content;
        this.condition = condition;
    }
}
