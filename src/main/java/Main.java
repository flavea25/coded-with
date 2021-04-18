import services.FileService;

public class Main {

    public static void main(String[] args) {
        FileService fileService = new FileService();

//        fileService.printFileLines("C:/Users/flavi/IdeaProjects/firstmaven/pom.xml");
        fileService.printAllFilesFromFolder("C:/Users/flavi/IdeaProjects/firstmaven","");
    }
}
