package wh1spr.discord.butterflybot.database.entities.users;

import com.mongodb.client.model.Updates;
import org.bson.Document;
import wh1spr.discord.butterflybot.ButterflyBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserPermissions {

    private static Logger log = Logger.getLogger("Permission");
    private static final List<String> defaultPerms = new ArrayList<>();
    private static ButterflyBot bot;

    public static void loadDefaultPermissions(ButterflyBot b) {
        bot = b;
        try {
            defaultPerms.addAll(Files.readAllLines(Paths.get("defaultpermissions.txt")));
        } catch (IOException e) {
            log.info("Couldn't find defaultpermissions.txt. No default permissions loaded...");
        }
    }

    UserPermissions(UserEntity ue) {
        this.ue = ue;
        Document d = ue.getDocument();
        if (!d.containsKey("takenPerms")) {
            ue.update(Updates.set("takenPerms", new ArrayList<String>()));
        }
        if (!d.containsKey("givenPerms")) {
            ue.update(Updates.set("givenPerms", new ArrayList<String>()));
        }
    }

    private UserEntity ue = null;

    public boolean isTakenPerm(String perm) {
        List<String> taken = this.ue.getDocument().getList("takenPerms", String.class, new ArrayList<String>());
        return isPermInList(perm, taken);
    }

    public boolean isGivenPerm(String perm) {
        List<String> given = this.ue.getDocument().getList("givenPerms", String.class, new ArrayList<String>());
        return isPermInList(perm, given);
    }

    public boolean isDefaultPerm(String perm) {
        return isPermInList(perm, defaultPerms);
    }

    // HELP - Do this with regexes?
    private boolean isPermInList(String toCheck, List<String> perms) {
        if (perms.contains(toCheck)) return true;

        // change perms to list without stars
        perms = this.unstarrify(perms);
        // Change tocheck to list of perms without stars
        List<String> tc = new ArrayList<>();
        tc.add(toCheck);
        tc = this.unstarrify(tc);

        for (String perm : tc) {
            if (!perms.contains(perm)) return false;
        }
        return true;
    }

    private List<String> unstarrify(List<String> perms) {
        ArrayList<String> newPerms = new ArrayList<>();
        for (String perm : perms) {
            if (perm.contains("*")) {
                String start = perm.split("\\*")[0];
                for (String def : this.getRegisteredPerms()) {
                    if (def.startsWith(start)) newPerms.add(def);
                }
            } else newPerms.add(perm);
        }
        return newPerms;
    }

    private List<String> getRegisteredPerms() {
        return new ArrayList<>(bot.getCommandRegistry().getPermissions());
    }

}
