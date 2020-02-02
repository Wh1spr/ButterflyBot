package wh1spr.discord.butterflybot.database;

public abstract class BasicUpdateItem<U> extends BasicItem {

    // update info and get item
    public BasicUpdateItem(String collectionName, Long id, U item) {
        super(collectionName, id, false);
        update(item);
    }

    // just get item
    public BasicUpdateItem(String collectionName, Long id, boolean shouldExist) {
        super(collectionName, id, shouldExist);
    }

    protected abstract void update(U item);
}
