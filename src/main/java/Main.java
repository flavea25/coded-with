import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        File root = new File("C:/Users/flavi/IdeaProject/firstmaven");
        System.out.println(root.getName());
        if(root.listFiles() != null) {
            for(File f: root.listFiles()) {
                System.out.println(f.getName());
            }
        }
    }
}
