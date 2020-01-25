package wh1spr.discord.butterflybot.database;

import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.util.ArrayList;
import java.util.Iterator;

public class Database {

    /************
     * INSTANCE *
     ************/
    private static Database INSTANCE;
    public static Database getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("No instance created yet");
        return INSTANCE;
    }
    public static Database createInstance(String url) {
        return createInstance(url, "ButterflyBot");
    }
    public static Database createInstance(String url, String dbName) {
        if (INSTANCE != null) throw new IllegalStateException("An instance already exists");
        INSTANCE = new Database(url, dbName);
        INSTANCE.setupReservedDocuments();
        return INSTANCE;
    }

    private MongoDatabase db = null;

    private Database(String url, String dbName) {
        MongoClient client = MongoClients.create(url);
        this.db = client.getDatabase(dbName);
        try {
            db.listCollectionNames().first(); //To make sure we dont have auth problems
        } catch (MongoSecurityException e) {
            throw new IllegalStateException("Could not authenticate with MongoDB", e);
        } catch (Exception e) {
            throw new IllegalStateException("Could not instantiate db connection", e);
        }
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return this.db.getCollection(collectionName);
    }

    public boolean existsCollection(String collectionName) {
        Iterator<String> iter = this.db.listCollectionNames().iterator();
        while(iter.hasNext()) {
            if (iter.next().equals(collectionName)) return true;
        }
        return false;
    }

    private void setupReservedDocuments() {
        MongoCollection<Document> users = this.getCollection(UserEntity.COLLECTION_NAME);
        if (!BasicItem.exists(UserEntity.COLLECTION_NAME, 0L)) {
            Document d = new Document("_id", 0L);
            d.put("owners", new ArrayList<Long>());
            users.insertOne(d);
        }
    }

}
