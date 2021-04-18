package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Technology {
    MAVEN("Maven", RuleType.FILE_NAME, Category.BUILD, "pom.xml"),
    ANGULAR("Angular", RuleType.FILE_NAME, Category.FRAMEWORK, "angular.json"), //TODO check if true:))

    INTELLIJ_IDEA("IntelliJ Idea", RuleType.FOLDER_NAME, Category.IDE, ".idea"),
    ECLIPSE("Eclipse IDE", RuleType.FILE_CONTENT, Category.IDE, "<name>org.eclipse.jdt.core.javabuilder</name>"), //TODO file .project (POATE FI SI ALCEVA?? -> check doar dc exista "eclipse")

    GIT_HUB("GitHub", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://github.com/"), //TODO from folder .git -> file config
    BITBUCKET("BitBucket", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://bitbucket.org/"); //TODO check if true..

    private final String name;
    private final RuleType ruleType;
    private final Category category;
    private final String condition; //TODO if FILE_CONTENT: a path to the specific file? or at leas a fileName..?
}
