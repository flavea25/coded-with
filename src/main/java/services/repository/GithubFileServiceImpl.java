package services.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class GithubFileServiceImpl implements RepositoryFileService{

    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    private static final String GITHUB_API_SEARCH_CODE_PATH = "search/code?q=";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static Map<String, Object> makeRESTCall(String restUrl) {
        Request request = Request.Get(restUrl);
        try {
            return gson.fromJson(request.execute().returnContent().asString(), Map.class);
        } catch (IOException ignored) {
            log.error("Error while executing the request!: " + restUrl); //TODO analyze error: wrong request or request rate exceeded? : https://api.github.com/rate_limit
            return null;
        }
    }

    @Override
    public boolean isTextInFile(String repositoryName, String pathEnding, String searchedText) {
        String searchQuery = searchedText + "+repo:" + repositoryName;
        Map<String, Object> searchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + searchQuery);

        if(pathEnding == null) {
            return searchResult != null && Double.parseDouble(searchResult.get("total_count").toString()) > 0;
        }

        AtomicBoolean foundText = new AtomicBoolean(false);
        if(searchResult != null) {
            gson.toJsonTree(searchResult).getAsJsonObject().get("items").getAsJsonArray().forEach(item -> {
                if(item.getAsJsonObject().get("path").toString().endsWith(pathEnding + "\"")) {
                    foundText.set(true);
                }
            });
        }

        return foundText.get();
    }

    @Override
    public boolean foundFileInRepository(String fileName, String repositoryName) {
        String searchQuery = "filename:" + fileName + "+repo:" + repositoryName;
        Map<String, Object> searchResult = makeRESTCall(GITHUB_API_BASE_URL + GITHUB_API_SEARCH_CODE_PATH + searchQuery);

        return searchResult != null && Double.parseDouble(searchResult.get("total_count").toString()) > 0;
    }

    @Override
    public Set<String> getDirectoriesFromRepository(String repositoryName) {
        Set<String> directories = new HashSet<>();

        Map<String, Object> jsonMap = makeRESTCall(GITHUB_API_BASE_URL + "repos/" + repositoryName + "/branches/master");

        //Path in JSON = root > commit > commit > tree > url
        if(jsonMap != null) {
            String treeApiUrl = gson.toJsonTree(jsonMap)
                    .getAsJsonObject().get("commit")
                    .getAsJsonObject().get("commit")
                    .getAsJsonObject().get("tree")
                    .getAsJsonObject().get("url")
                    .getAsString();
            Map<String, Object> jsonTreeMap = makeRESTCall(treeApiUrl + "?recursive=1");

            for (Map<String, Object> obj : (List<Map<String, Object>>) jsonTreeMap.get("tree")) {
                if (obj.get("type").equals("tree")) {
                    int startIndex = Math.max(((String)obj.get("path")).lastIndexOf("/"), 0);
                    directories.add(((String) obj.get("path")).substring(startIndex));
                }
            }
        }

        return directories;
    }

    @Override
    public String getRepositoryNameFromUrl(String url) {
        String editedUrl = url.substring(url.indexOf(".com/") + 5);
        String owner = editedUrl.substring(0, editedUrl.indexOf('/'));
        editedUrl = editedUrl.substring(editedUrl.indexOf(owner) + owner.length() + 1);
        String repo = editedUrl.substring(0, editedUrl.contains("/") ? editedUrl.indexOf('/') : editedUrl.length());
        return owner + '/' + repo;
    }
}
