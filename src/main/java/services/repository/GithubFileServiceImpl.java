package services.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.Map;

public class GithubFileServiceImpl implements RepositoryFileService{

    @Override
    public boolean isTextInFile(String root, String text) {
        return false;
    }

    @Override
    public boolean foundFileInRepository(String fileName, String repositoryUrl) {
        return false;
    }

    @Override
    public String getOrgFromRepositoryUrl(String repositoryUrl) {
        return null;
    }

    @Override
    public String getRepositoryNameFromUrl(String url) {
        return null;
    }

    private static Map makeRESTCall(String restUrl, String acceptHeaderValue)
            throws IOException {
        Request request = Request.Get(restUrl);

        if (acceptHeaderValue != null && !acceptHeaderValue.isBlank()) {
            request.addHeader("Accept", acceptHeaderValue);
        }

        Content content = request.execute().returnContent();
        String jsonString = content.asString();
        // System.out.println("content = " + jsonString);

        // To print response JSON, using GSON. Any other JSON parser can be used here.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map jsonMap = gson.fromJson(jsonString, Map.class);
        return jsonMap;
    }

    private static Map makeRESTCall(String restUrl) throws IOException {
        return makeRESTCall(restUrl, null);
    }

    private static void searchFileByFileName() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String GITHUB_API_BASE_URL = "https://api.github.com/";

        String GITHUB_API_SEARCH_CODE_PATH = "search/code?q=";
        String codeFileQuery = "filename:README+extension:txt+org:flavea25";

        Map fileNameSearchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + codeFileQuery);

        System.out.println("Total number or results = " + fileNameSearchResult.get("total_count"));
        gson.toJsonTree(fileNameSearchResult).getAsJsonObject().get("items").getAsJsonArray()
                .forEach(r -> System.out.println("\tFile: " + r.getAsJsonObject().get("name") + "\n\t\t | Repo: "
                        + r.getAsJsonObject().get("repository").getAsJsonObject().get("html_url") + "\n\t\t | Path: "
                        + r.getAsJsonObject().get("path")));
    }

//        try {   //TODO clone repository -> NOT IN A METHOD
//            //print branches
//            Git.lsRemoteRepository().setRemote(repoUrl).call().forEach(c -> System.out.println("" + c.getName()));
//
//            //close repository
//        String cloneDirectoryPath = "C:/Users/flavi/OneDrive/Desktop/licenta/mycode"; // Ex.in windows c:\\gitProjects\SpringBootMongoDbCRUD\
//            System.out.println("Cloning "+repoUrl+" into "+cloneDirectoryPath);
//            Git.cloneRepository()
//                    .setURI(repoUrl)
//                    .setBranch("feature/technologiesDefinition")
//                    .setDirectory(Paths.get(cloneDirectoryPath).toFile())
//                    .call();
//            System.out.println("Completed Cloning");
//        } catch (GitAPIException e) {
//            System.out.println("Exception occurred while cloning repo");
//            e.printStackTrace();
//        }
}
