import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.security.auth.login.LoginException;

public class Main implements EventListener {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder("NjUzMDAwMTc3NTcyOTA0OTYx.Xewtow.0NEMcvD936g7pHXT02RaHEkB29Y")
                .addEventListeners(new Main())
                .build();
    }

    @Override
    public void onEvent(GenericEvent event) {
        System.out.println(event);
        if (event instanceof ReadyEvent) {
            System.out.println("ready!");
            User u = event.getJDA().getUserById("293074070055026690");
            System.out.println("got user");
            PrivateChannel t = u.openPrivateChannel().complete();
            System.out.println("got channel");
            t.sendMessage("Hey you, you're pretty awesome. Love you :heart:").complete();
            System.out.println("sent message");
            System.exit(0);
        }
    }

}
