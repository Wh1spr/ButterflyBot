package wh1spr.discord.butterflybot.database.entities.users;

import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.database.BasicUpdateItem;

import java.util.Arrays;
import java.util.List;

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

    public boolean hasPermission(String permission) {
        if (this.isOwner()) return true;
        if (this.isBanned()) return false;
        if (up == null) up = new UserPermissions(this);

        boolean res;
        this.lockDocument();
        if (this.up.isTakenPerm(permission)) res = false;
        else if (this.up.isDefaultPerm(permission)) res = true;
        else res = this.up.isGivenPerm(permission);
        this.openDocument();
        return res;
    }
    public boolean hasPermissions(String... permissions) {
        boolean res = true;
        this.lockDocument();
        for (String perm : permissions) {
            if (!hasPermission(perm)) {
                res = false;
                break;
            }
        }
        this.openDocument();
        return res;
    }
    public boolean hasPermissions(List<String> permissions) {
        return hasPermission(Arrays.toString(permissions.toArray()));
    }
    public boolean hasOneOfPermissions(String... permissions) {
        for (String perm : permissions) {
            if (hasPermission(perm)) return true;
        } return false;
    }
    public boolean hasOneOfPermissions(List<String> permissions) {
        return hasOneOfPermissions(Arrays.toString(permissions.toArray()));
    }

    public boolean isBanned() {
        if (this.isOwner()) return false;
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

    /**
     * Returns whether or not this user is a bot owner
     */
    public boolean isOwner() {
        return this.getReservedDocument(0L).getList("owners", Long.class).contains(this.getId());
    }

}
