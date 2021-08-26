package services.database;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface MongoService extends DatabaseService{
    void useDatabase(String name);

    void createCollection(String name);
    
    void deleteCollection(String name);
    
    void addDocumentToCollection(Document document, String collectionName);
    
    void addDocumentToCollection(Map<String, Object> dimensions, String collectionName);

    void updateDocumentInCollection(Document oldDocument, Document newDocument, String collectionName);

    void updateDocumentInCollection(Document oldDocument, Map<String, Object> newDimensions, String collectionName);

    void updateDocumentInCollection(Map<String, Object> oldDimensions, Map<String, Object> newDimensions, String collectionName);
    
    MongoCursor<Document> findDocumentsFromCollection(Document searchQuery, String collectionName);

    MongoCursor<Document> findDocumentsFromCollection(Map<String, Object> dimensions, String collectionName);
    
    void deleteDocumentsFromCollection(Document searchQuery, String collectionName);
    
    void deleteDocumentsFromCollection(Map<String, Object> dimensions, String collectionName);
}
