package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Technology {
    MAVEN("Maven", RuleType.FILE_NAME, Category.BUILD, "pom.xml"),
    ANGULAR("Angular", RuleType.FILE_NAME, Category.FRAMEWORK, "angular.json"), //TODO check if true:))

    INTELLIJ_IDEA("IntelliJ Idea", RuleType.FOLDER_NAME, Category.IDE, ".idea"),
    GIT_HUB("GitHub", RuleType.FOLDER_NAME, Category.VCS, ".git"); //TODO or is it simply git??

    private final String name;
    private final RuleType ruleType;
    private final Category category;
    private final String condition;
}
