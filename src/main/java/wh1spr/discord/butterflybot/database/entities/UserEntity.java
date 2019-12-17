package wh1spr.discord.butterflybot.database.entities;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.database.BasicItem;
import wh1spr.discord.butterflybot.database.BasicUpdateItem;

public class UserEntity extends BasicUpdateItem<User> {

    public static final String COLLECTION_NAME = "users";

    public UserEntity(Long id) {
        super(COLLECTION_NAME, id);
    }

    public UserEntity(User user) {
        super(COLLECTION_NAME, user.getIdLong(), user);
    }

    @Override
    protected void update(User item) {
        this.update(Updates.combine(Updates.set("username", item.getAsTag())));
    }

}
