package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Technology {
    //TODO check all...
    MAVEN("Maven", RuleType.FILE_NAME, Category.BUILD, "pom.xml"),
    GRADLE("Gradle", RuleType.FILE_NAME, Category.BUILD, "build.gradle"),
    ANGULAR("Angular", RuleType.FILE_NAME, Category.FRAMEWORK, "angular.json"),

    INTELLIJ_IDEA("IntelliJ Idea", RuleType.FOLDER_NAME, Category.IDE, ".idea"),
    ECLIPSE("Eclipse IDE", RuleType.FILE_CONTENT, Category.IDE, "<name>org.eclipse.jdt.core.javabuilder</name>"), //TODO file .project (POATE FI SI ALCEVA?? -> check doar dc exista "eclipse")

    GIT_HUB("GitHub", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://github.com/"), //TODO .git/config
    DOCKER("Docker", RuleType.FILE_NAME, Category.CI_CD, "Dockerfile"),
    JENKINS("Jenkins", RuleType.FILE_NAME, Category.CI_CD, "Jenkinsfile"),
    BITBUCKET("BitBucket", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://bitbucket.org/");

    private final String name;
    private final RuleType ruleType;
    private final Category category;
    private final String condition; //TODO if FILE_CONTENT: a path to the specific file? or at leas a fileName..?
}
