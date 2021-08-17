package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildTools {
    MAVEN("pom.xml"),
    GRADLE("build.gradle"),
    ANT("build.xml"),
    GANT("build.gant");

    private String fileName;
}
