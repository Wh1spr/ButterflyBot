package wh1spr.discord.butterflybot.database.entities.users;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.database.BasicUpdateItem;

//TODO documentation
public class UserEntity extends BasicUpdateItem<User> {

    public static final String COLLECTION_NAME = "users";

    public UserEntity(Long id) {
        super(COLLECTION_NAME, id);
    }

    public UserEntity(User user) {
        super(COLLECTION_NAME, user.getIdLong(), user);
    }

    public static boolean hasPermission(User u, String permission) {
        return new UserEntity(u).hasPermission(permission);
    }

    private UserPermissions up = null;

    //TODO
    public boolean hasPermission(String permission) {
        if (this.isBanned()) return false;
        if (up == null) up = new UserPermissions(this);
        // check taken away perms
        // check default perms
        // check given perms


        return true;
    }

    public boolean isBanned() {
        boolean banned = this.getDocument().getBoolean("banned", false);
        return banned;
    }

    /**
     * Bans the user from using bot commands
     * @return True if the user was not banned, and now is
     *         False if the user was already banned
     */
    public boolean ban() {
        if (this.isBanned()) return false;
        this.update(Updates.set("banned", true));
        return true;
    }

    /**
     * Pardons/unbans the user from using bot commands
     * @return True if the user was banned, and now is not
     *         False if the user was not banned
     */
    public boolean pardon() {
        if (!this.isBanned()) return false;
        this.update(Updates.unset("banned"));
        return true;
    }

    @Override
    protected void update(User item) {
        this.update(Updates.combine(Updates.set("username", item.getAsTag())));
    }

}
