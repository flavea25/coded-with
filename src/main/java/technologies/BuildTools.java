package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildTools {
    MAVEN("Maven", "pom.xml"),
    GRADLE("Gradle", "build.gradle"),
    ANT("Ant", "build.xml"),
    GANT("Gant", "build.gant");

    private String name;
    private String fileName;
}
