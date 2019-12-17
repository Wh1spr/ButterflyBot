package wh1spr.discord.butterflybot.database.entities.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserPermissions {

    private static Logger log = Logger.getLogger("Permission");
    private static final List<String> defaultPerms = new ArrayList<>();

    static {
        try {
            defaultPerms.addAll(Files.readAllLines(Paths.get("defaultpermissions.txt")));
        } catch (IOException e) {
            log.info("Couldn't find defaultpermissions.txt. No default permissions loaded...");
        }
    }




}
