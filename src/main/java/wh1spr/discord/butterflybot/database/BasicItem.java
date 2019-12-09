package wh1spr.discord.butterflybot.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public abstract class BasicItem {

    private String collectionName;
    private MongoCollection coll;
    private Long id = 0L;

    public BasicItem(String collectionName, Long id) {
        if (id < 10L) throw new IllegalArgumentException("Ids lower than 10 are reserved");
        this.collectionName = collectionName;
        this.coll = Database.getInstance().getCollection(collectionName);
        this.id = id;
    }

    protected MongoCollection getCollection() {
        return this.coll;
    }

    public Long getId() {
        return this.id;
    }

    public Bson getFilter() {
        return Filters.eq("_id", this.getId());
    }

    public Document get() {
        return (Document) this.coll.find(getFilter()).first();
    }

    public boolean exists(Long id) {
        return this.getCollection().find(Filters.eq("_id", this.getId())).first() != null;
    }

    public void update(Bson update) {
        this.getCollection().findOneAndUpdate(getFilter(), update);
    }

    public void delete() {
        this.getCollection().deleteOne(getFilter());
    }
}
