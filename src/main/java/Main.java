import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        File root = new File("C:/Users/flavi/IdeaProjects/firstmaven"); //TODO take dynamically

        if(root.listFiles() != null) {
            for(File f: Objects.requireNonNull(root.listFiles())) {
                System.out.println(f.getName()); //TODO replace with log
            }
        }
    }
}
