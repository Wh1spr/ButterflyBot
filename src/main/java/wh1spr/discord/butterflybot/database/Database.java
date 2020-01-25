package wh1spr.discord.butterflybot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
        INSTANCE = new Database(url, dbName);
        return INSTANCE;
    }

    private MongoClient client = null;
    private MongoDatabase db = null;

    public Database(String url, String dbName) {
        this.client = MongoClients.create(url);
        this.db = this.client.getDatabase(dbName);
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

}
