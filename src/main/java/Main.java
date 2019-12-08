import wh1spr.discord.butterflybot.ButterflyBot;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws LoginException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("butterfly.properties"));

        new ButterflyBot(props.getProperty("butterfly.token"));
    }
}
