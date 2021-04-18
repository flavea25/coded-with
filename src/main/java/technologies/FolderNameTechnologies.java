package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FolderNameTechnologies {
    INTELLIJ_IDEA("IntelliJ Idea", Category.IDE, ".idea");

    private final String name;
    private final Category category;
    private final String folderName;
}
