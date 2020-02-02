package wh1spr.discord.butterflybot.database.entities.messages;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.Message;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommandMessageEntity extends MessageEntity {

    public static final String COLLECTION_NAME = "cmd_msg";

    public CommandMessageEntity(Long id) {
        super(COLLECTION_NAME, id);
    }

    public CommandMessageEntity(Message msg, boolean isPrivate) {
        super(COLLECTION_NAME, msg);
        this.update(Updates.set("isPrivate", isPrivate));
    }

    public void exception(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        this.update(Updates.set("exception", exceptionAsString));
    }

}
