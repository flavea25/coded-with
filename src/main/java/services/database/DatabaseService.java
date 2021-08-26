package services.database;

public interface DatabaseService {
    void createConnection(String host, int port);

    void createDefaultConnection();

    void createDatabase(String name);

    void deleteDatabase(String name);

    void closeConnection();
}
