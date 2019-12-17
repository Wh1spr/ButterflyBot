package wh1spr.discord.butterflybot.database.entities;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.database.BasicUpdateItem;

public class UserEntity extends BasicUpdateItem<User> {

    public UserEntity(Long id) {
        super("users", id);
    }

    public UserEntity(User user) {
        super("users", user.getIdLong(), user);
    }

    @Override
    protected void update(User item) {
        this.update(Updates.combine(Updates.set("username", item.getAsTag())));
    }
}
