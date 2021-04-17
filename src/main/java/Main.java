import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File root = new File("C:/Users/flavi/IdeaProjects/firstmaven");

        if(root.listFiles() != null) {
            for(File f: root.listFiles()) {
                System.out.println(f.getName());
            }
        }
    }
}
