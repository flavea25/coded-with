package services.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Map;

public class MongoServiceImpl implements MongoService{
    private final static String DEFAULT_HOST = "localhost";

    private final static int DEFAULT_PORT = 27017;

    private static MongoClient mongoClient = null;

    private static MongoDatabase db = null;

    private boolean connectedToDatabase() {
        return mongoClient != null;
    }

    private boolean usesDatabase() {
        return connectedToDatabase() && db != null;
    }

    @Override
    public void createConnection(String host, int port) {
        if(connectedToDatabase()) {
            closeConnection();
        }
        mongoClient = new MongoClient(host, port);
    }

    @Override
    public void createDefaultConnection() {
        createConnection(DEFAULT_HOST, DEFAULT_PORT);
    }

    @Override
    public void createDatabase(String name) {
        if(connectedToDatabase()) {
            mongoClient.getDatabase(name);
        }
    }

    @Override
    public void deleteDatabase(String name) {
        if(connectedToDatabase()) {
            if(usesDatabase() && db.getName().equals(name)) {
                db.drop();
                db = null;
            }
            else {
                mongoClient.getDatabase(name).drop();
            }
        }
    }

    @Override
    public void closeConnection() {
        if(connectedToDatabase()) {
            mongoClient.close();
            mongoClient = null;
            db = null;
        }
    }

    @Override
    public void useDatabase(String name) {
        if(connectedToDatabase()) {
            db = mongoClient.getDatabase(name);
        }
    }

    @Override
    public void createCollection(String name) {
        if(usesDatabase()) {
            db.createCollection(name);
        }
    }

    @Override
    public void deleteCollection(String name) {
        if(usesDatabase()) {
            db.getCollection(name).drop();
        }
    }

    @Override
    public void addDocumentToCollection(Document document, String collectionName) {
        if(usesDatabase()) {
            db.getCollection(collectionName).insertOne(document);
        }
    }

    @Override
    public void addDocumentToCollection(Map<String, Object> dimensions, String collectionName) {
        if(usesDatabase()) {
            Document doc = new Document();
            doc.putAll(dimensions);
            db.getCollection(collectionName).insertOne(doc);
        }
    }

    @Override
    public void updateDocumentInCollection(Document oldDocument, Document newDocument, String collectionName) {
        if(usesDatabase()) {
            db.getCollection(collectionName).updateOne(oldDocument, newDocument);
        }
    }

    @Override
    public void updateDocumentInCollection(Document oldDocument, Map<String, Object> newDimensions, String collectionName) {
        updateDocumentInCollection(oldDocument, new Document(newDimensions), collectionName);
    }

    @Override
    public void updateDocumentInCollection(Map<String, Object> oldDimensions, Map<String, Object> newDimensions, String collectionName) {
        updateDocumentInCollection(new Document(oldDimensions), new Document(newDimensions), collectionName);
    }

    @Override
    public MongoCursor<Document> findDocumentsFromCollection(Document searchQuery, String collectionName) {
        return db.getCollection(collectionName).find(searchQuery).iterator();
    }

    @Override
    public MongoCursor<Document> findDocumentsFromCollection(Map<String, Object> dimensions, String collectionName) {
        return db.getCollection(collectionName).find(new Document(dimensions)).iterator();
    }

    @Override
    public void deleteDocumentsFromCollection(Document searchQuery, String collectionName) {
        db.getCollection(collectionName).deleteMany(searchQuery);
    }

    @Override
    public void deleteDocumentsFromCollection(Map<String, Object> dimensions, String collectionName) {
        deleteDocumentsFromCollection(new Document(dimensions), collectionName);
    }
}
