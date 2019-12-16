package wh1spr.discord.butterflybot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
        INSTANCE = new Database(url);
        return INSTANCE;
    }

    private MongoClient client = null;
    private MongoDatabase db = null;

    public Database(String url) {
        this.client = MongoClients.create(url);
        this.db = this.client.getDatabase("ButterflyBot");
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return this.db.getCollection(collectionName);
    }

}
