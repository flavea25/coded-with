package helpers;

public final class CodedWithConstants {

    //databases
    public static final String DATABASE_NAME = "coded-with";
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 27017;
    //collections
    public static final String ANALYSED_REPOSITORIES = "analysed-repositories";
    public static final String REPOSITORY_DB = "repoDB";
    public static final String TOOLS_RANKING = "toolsRanking";
    public static final String TOP2000 = "top2000";
    //paths
    public static final String GITHUB_REPOSITORY_LINK_START = "https://github.com/";
    public static final String BITBUCKET_REPOSITORY_LINK_START = "https://bitbucket.org/";
    public static final String PATH_TO_TECHNOLOGIES = "./src/main/resources/technologies.json";
    //GitHub REST API
    public static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    public static final String GITHUB_API_SEARCH_CODE_PATH = "search/code?q=";
}
