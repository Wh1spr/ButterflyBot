package wh1spr.discord.butterflybot.database.entities.messages;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.Message;
import wh1spr.discord.butterflybot.database.BasicItem;

public abstract class MessageEntity extends BasicItem {

    public MessageEntity(String collectionName, Long id) {
        super(collectionName, id, true);
    }

    public MessageEntity(String collectionName, Message msg) {
        super(collectionName, msg.getIdLong(), false);
        this.update(Updates.combine(
                Updates.set("contentRaw", msg.getContentRaw()),
                Updates.set("contentDisplay", msg.getContentDisplay()),
                Updates.set("time", msg.getTimeCreated().toInstant()),
                Updates.set("author", msg.getAuthor().getIdLong()),
                Updates.set("channel", msg.getChannel().getIdLong()),
                Updates.set("attachments", msg.getAttachments().size())
        ));
    }
}
