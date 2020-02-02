package wh1spr.discord.butterflybot.database.entities.messages;

import net.dv8tion.jda.api.entities.Message;

public class PrivateMessageEntity extends MessageEntity {

    public static final String COLLECTION_NAME = "private_msg";

    public PrivateMessageEntity(Long id) {
        super(COLLECTION_NAME, id);
    }
    public PrivateMessageEntity(Message msg) {
        super(COLLECTION_NAME, msg);
    }
}
