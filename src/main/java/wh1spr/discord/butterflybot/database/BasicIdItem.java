package wh1spr.discord.butterflybot.database;

import org.bson.Document;

// used when the item you have doesnt already have an ID of its own such as a user, a message etc.
public abstract class BasicIdItem extends BasicItem {

    public BasicIdItem(String collectionName, Long id, boolean shouldExist) {
        super(collectionName, id, shouldExist);
    }

    public BasicIdItem(String collectionName) {
        super(collectionName, getAndSetNextId(collectionName), false);
    }

    // Use 0 id to put lastId
    private static synchronized Long getAndSetNextId(String collectionName) {
        if (!Database.getInstance().existsCollection(collectionName)) throw new IllegalArgumentException("Collection does not exist: " + collectionName);
        Document doc = null;
        if (!BasicItem.exists(collectionName,0L)) {
            doc = BasicItem.createReservedDocument(collectionName, 0L);
        } else {
            doc = BasicItem.getReservedDocument(collectionName, 0L);
        }

        if (doc.getLong("lastId") == null) {
            doc.put("lastId", 10L);
            BasicItem.updateReservedDocument(collectionName, doc);
            return 10L;
        } else {
            Long next = doc.getLong("lastId") + 1L;
            doc.put("lastId", next);
            BasicItem.updateReservedDocument(collectionName, doc);
            return next;
        }
    }
}
