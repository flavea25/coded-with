package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileNameTechnologies {
    MAVEN("Maven", Category.BUILD, "pom.xml");

    private final String name;
    private final Category category;
    private final String fileName;
}
