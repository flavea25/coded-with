package services.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
public class GithubFileServiceImpl implements RepositoryFileService{

    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    private static final String GITHUB_API_SEARCH_CODE_PATH = "search/code?q=";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public boolean isTextInFile(String root, String text) {
        return false;
    } //TODO next

    @Override
    public boolean foundFileInRepository(String fileName, String repositoryName) {
        String codeFileQuery = "filename:" + fileName + "+repo:" + repositoryName;
        Map<String, Object> searchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + codeFileQuery);

        return searchResult != null && Double.parseDouble(searchResult.get("total_count").toString()) > 0;

//        gson.toJsonTree(fileNameSearchResult).getAsJsonObject().get("items").getAsJsonArray()
//                .forEach(r -> log.info("\n\tFile: " + r.getAsJsonObject().get("name") +
//                        "\n\t\t | Repo: " + r.getAsJsonObject().get("repository").getAsJsonObject().get("html_url") +
//                        "\n\t\t | Path: " + r.getAsJsonObject().get("path")));
    }

    @Override
    public List<String> getDirectoriesFromRepository(String repositoryName) { //TODO next FIRST
        return null;
    }

    private static Map<String, Object> makeRESTCall(String restUrl) {
        Request request = Request.Get(restUrl);
        try {
            return gson.fromJson(request.execute().returnContent().asString(), Map.class);
        } catch (IOException ignored) {
            log.error("Error while executing the request!");
            return null;
        }
    }

    public void printBranches(String repositoryUrl) {
        try {
            Git.lsRemoteRepository()
                    .setRemote(repositoryUrl)
                    .call()
                    .forEach(c -> log.info(c.getName()));
        } catch (GitAPIException e) {
            log.error("Exception occurred while printing repository branches!");
            e.printStackTrace();
        }
    }

    public void cloneRepositoryBranchAtPath(String repositoryUrl, String branch, String path) {
        try {
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setBranch(branch)
                    .setDirectory(Paths.get(path).toFile())
                    .call();
        } catch (GitAPIException e) {
            log.error("Exception occurred while cloning repository!");
            e.printStackTrace();
        }
    }
}
