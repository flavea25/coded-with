package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Technology {
    MAVEN("Maven", RuleType.FILE_NAME, Category.BUILD, "pom.xml", null),
    GRADLE("Gradle", RuleType.FILE_NAME, Category.BUILD, "build.gradle", null),

    ANGULAR("Angular", RuleType.FILE_NAME, Category.FRAMEWORK, "angular.json", null),

    INTELLIJ_IDEA("IntelliJ Idea", RuleType.FOLDER_NAME, Category.IDE, ".idea", null),
    ECLIPSE("Eclipse IDE", RuleType.FILE_CONTENT, Category.IDE, "<name>org.eclipse.jdt.core.javabuilder</name>", ".project"),

    GIT_HUB("GitHub", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://github.com/", ".git/config"),
    DOCKER("Docker", RuleType.FILE_NAME, Category.CI_CD, "Dockerfile", null),
    JENKINS("Jenkins", RuleType.FILE_NAME, Category.CI_CD, "Jenkinsfile", null),
    BITBUCKET("BitBucket", RuleType.FILE_CONTENT, Category.CI_CD, "url = https://bitbucket.org/", ".git/config");

    private final String name;
    private final RuleType ruleType;
    private final Category category;
    private final String condition;
    private final String pathEnding;
}
