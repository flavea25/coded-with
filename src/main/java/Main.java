import services.FileService;
import services.FileServiceImpl;

public class Main {

    public static void main(String[] args) {
        FileService fileService = new FileServiceImpl();

//        fileService.getFolderNames("C:/Users/flavi/IdeaProjects/firstmaven").forEach(System.out::println);
//        fileService.getFilePaths("C:/Users/flavi/IdeaProjects/firstmaven").forEach(System.out::println);
    }
}
