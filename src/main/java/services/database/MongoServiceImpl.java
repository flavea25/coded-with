package services.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import helpers.CodedWithConstants;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class MongoServiceImpl implements MongoService{
    private static MongoClient mongoClient = null;

    private static MongoDatabase db = null;

    private boolean connectedToDatabase() {
        return mongoClient != null;
    }

    private boolean usesDatabase() {
        return connectedToDatabase() && db != null;
    }

    @Override
    public boolean existsCollection(String name) {
        boolean exists = false;
        MongoCursor<String> collections = db.listCollectionNames().iterator();
        while(collections.hasNext() && !exists) {
            if(collections.next().equals(name)) {
                exists = true;
            }
        }
        return exists;
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
        createConnection(CodedWithConstants.DEFAULT_HOST, CodedWithConstants.DEFAULT_PORT);
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
        if(usesDatabase() && !existsCollection(name)) {
            db.createCollection(name);
        }
    }

    @Override
    public void deleteCollection(String name) {
        if(usesDatabase() && existsCollection(name)) {
            db.getCollection(name).drop();
        }
    }

    @Override
    public void addDocumentToCollection(Document document, String collectionName) {
        if(usesDatabase()) {
            if(!existsCollection(collectionName)) {
                createCollection(collectionName);
            }
            db.getCollection(collectionName).insertOne(document);
        }
    }

    @Override
    public void addDocumentToCollection(Map<String, Object> dimensions, String collectionName) {
        addDocumentToCollection(new Document(dimensions), collectionName);
    }

    @Override
    public void updateDocumentInCollection(Document oldDocument, Document newDocument, String collectionName) {
        if(usesDatabase()) {
            if(existsCollection(collectionName)) {
                db.getCollection(collectionName).updateMany(oldDocument, new Document(Map.of("$set", newDocument)));
            }
            else {
                addDocumentToCollection(newDocument, collectionName);
            }
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
        if(usesDatabase() && existsCollection(collectionName)) {
            return db.getCollection(collectionName).find(searchQuery).iterator();
        }
        return null;
    }

    @Override
    public MongoCursor<Document> findDocumentsFromCollection(Map<String, Object> dimensions, String collectionName) {
        return findDocumentsFromCollection(new Document(dimensions), collectionName);
    }

    @Override
    public Document findDocumentFromCollection(Document searchQuery, String collectionName) {
        MongoCursor<Document> cursor = findDocumentsFromCollection(searchQuery, collectionName);
        return cursor == null ? null : cursor.hasNext() ? cursor.next() : new Document();
    }

    @Override
    public Document findDocumentFromCollection(Map<String, Object> dimensions, String collectionName) {
        return findDocumentFromCollection(new Document(dimensions), collectionName);
    }

    @Override
    public void deleteDocumentsFromCollection(Document searchQuery, String collectionName) {
        if(usesDatabase() && existsCollection(collectionName)) {
            db.getCollection(collectionName).deleteMany(searchQuery);
        }
    }

    @Override
    public void deleteDocumentsFromCollection(Map<String, Object> dimensions, String collectionName) {
        deleteDocumentsFromCollection(new Document(dimensions), collectionName);
    }

    @Override
    public Long getNumberOfSpecificDocumentsFromCollection(Document searchQuery, String collectionName) {
        if(usesDatabase() && existsCollection(collectionName)) {
            return db.getCollection(collectionName).countDocuments(searchQuery);
        }
        return 0L;
    }

    @Override
    public Long getNumberOfSpecificDocumentsFromCollection(Map<String, Object> dimensions, String collectionName) {
        return getNumberOfSpecificDocumentsFromCollection(new Document(dimensions), collectionName);
    }

    @Override
    public MongoCursor<Document> aggregateDocumentsFromCollection(List<Document> conditions, String collectionName) {
        if(usesDatabase() && existsCollection(collectionName)) {
            return db.getCollection(collectionName).aggregate(conditions).iterator();
        }
        return null;
    }
}
