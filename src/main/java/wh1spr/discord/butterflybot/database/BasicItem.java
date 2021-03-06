package wh1spr.discord.butterflybot.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public abstract class BasicItem {

    private String collectionName;
    private MongoCollection<Document> coll;
    private Long id = 0L;

    public BasicItem(String collectionName, Long id, boolean shouldExist) {
        if (id == null) throw new IllegalArgumentException("Id can not be null");
        if (id < 10L) throw new IllegalArgumentException("Ids lower than 10 are reserved");
        this.collectionName = collectionName;
        this.coll = Database.getInstance().getCollection(collectionName);
        this.id = id;
        if (!this.exists(id)) {
            if (shouldExist)
                throw new IllegalArgumentException("The given Id does not yet exist, while shouldExist flag is true");
            else this.getCollection().insertOne(new Document("_id", id));
        }
    }

    //FIXME make collection insert/update/delete unable to be done with id < 10 because reserved
    protected final MongoCollection<Document> getCollection() {
        return this.coll;
    }

    public Long getId() {
        return this.id;
    }

    public Bson getFilter() {
        return Filters.eq("_id", this.getId());
    }

    private Document doc = null;
    private boolean docLocked = false;
    protected synchronized final void lockDocument() {
        this.docLocked = true;
    }
    protected synchronized final void openDocument() {
        this.docLocked = false;
    }
    public Document getDocument() {
        if (docLocked && doc != null) return doc;
        else {
            doc = (Document) this.getCollection().find(getFilter()).first();
            return doc;
        }
    }

    public boolean exists(Long id) {
        return this.getCollection().find(Filters.eq("_id", id)).first() != null;
    }

    public static boolean exists(String collectionName, Long id) {
        if (!Database.getInstance().existsCollection(collectionName)) return false;
        return Database.getInstance().getCollection(collectionName).find(Filters.eq("_id", id)).first() != null;
    }

    public void update(Bson update) {
        this.getCollection().findOneAndUpdate(getFilter(), update);
    }
    public void update(List<Bson> updates) {
        this.getCollection().findOneAndUpdate(this.getFilter(), updates);
    }

    public void delete() {
        this.getCollection().deleteOne(getFilter());
    }

    protected final Document getReservedDocument(Long id) {
        if (id >= 10L) throw new IllegalArgumentException("Reserved document ID has to be lower than 10");
        if (!exists(id)) throw new IllegalArgumentException("Reserved document with this ID does not exist");
        return this.getCollection().find(Filters.eq("_id", id)).first();
    }

    static Document getReservedDocument(String collectionName, Long id) {
        if (!Database.getInstance().existsCollection(collectionName)) throw new IllegalArgumentException("Given collection does not exist: " + collectionName);
        if (id >= 10L) throw new IllegalArgumentException("Reserved document ID has to be lower than 10");
        if (!exists(collectionName, id)) throw new IllegalArgumentException("Reserved document with this ID does not exist");
        return Database.getInstance().getCollection(collectionName).find(Filters.eq("_id", id)).first();
    }

    static void createReservedDocument(String collectionName, Document doc) {
        if (!Database.getInstance().existsCollection(collectionName)) throw new IllegalArgumentException("Given collection does not exist: " + collectionName);
        if (doc.getLong("_id") == null) throw new IllegalArgumentException("Given document has no _id parameter");
        if (doc.getLong("_id") >= 10L) throw new IllegalArgumentException("Reserved document ID has to be lower than 10");
        if (exists(collectionName, doc.getLong("_id"))) throw new IllegalArgumentException("Reserved document with this ID already exists");
        Database.getInstance().getCollection(collectionName).insertOne(doc);
    }

    static Document createReservedDocument(String collectionName, Long id) {
        if (id == null) throw new IllegalArgumentException("Given ID can not be null");
        Document doc = new Document("_id", id);
        createReservedDocument(collectionName, doc);
        return doc;
    }

    static void updateReservedDocument(String collectionName, Document doc) {
        if (!Database.getInstance().existsCollection(collectionName)) throw new IllegalArgumentException("Given collection does not exist: " + collectionName);
        if (doc.getLong("_id") == null) throw new IllegalArgumentException("Given document has no _id parameter");
        if (doc.getLong("_id") >= 10L) throw new IllegalArgumentException("Reserved document ID has to be lower than 10");
        if (!exists(collectionName, doc.getLong("_id"))) throw new IllegalArgumentException("Given document does not exist yet");
        Database.getInstance().getCollection(collectionName).updateOne(Filters.eq(Filters.eq("_id", doc.getLong("_id"))), doc);
    }
}
