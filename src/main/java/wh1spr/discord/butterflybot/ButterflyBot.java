package wh1spr.discord.butterflybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.List;

public class ButterflyBot extends ListenerAdapter {

    private final JDA jda;

    public ButterflyBot(String token) throws LoginException {
        this.jda = new JDABuilder(token)
                .addEventListeners(this)
                .build();
    }

    // 160463456246431744 - wolfgang
    // 293074070055026690 - mathijs

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("ButterflyBot is ready to go!");

//        TextChannel test = event.getJDA().getTextChannelById("653004452667850770");
//        PrivateChannel pr = event.getJDA().getUserById("160463456246431744").openPrivateChannel().complete();
//        test.sendMessage("*TEST - Got private channel with user* " + String.format("**%s#%s:**", pr.getUser().getName(), pr.getUser().getDiscriminator())).queue();
//        pr.sendMessage("I have detected you are using an adblocker. Please disable your adblocker.").queue();

        System.out.println("Checking private channel");
        TextChannel test = event.getJDA().getTextChannelById("653004452667850770");
        PrivateChannel pr = event.getJDA().getUserById("160463456246431744").openPrivateChannel().complete();
        test.sendMessage("*TEST - Got private channel with user* " + String.format("**%s#%s:**", pr.getUser().getName(), pr.getUser().getDiscriminator())).queue();
        List<Message> history = pr.getHistory().retrievePast(40).complete();
        Collections.reverse(history);

        for (Message m : history) {
            test.sendMessage(String.format("**%s#%s:** *%s*", m.getAuthor().getName(), m.getAuthor().getDiscriminator(), m.getContentStripped())).queue();
        }
        try {
            test.sendMessage("*TEST - Done retrieving history*").complete(true);
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
//        System.exit(0);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        TextChannel test = event.getJDA().getTextChannelById("653004452667850770");
        test.sendMessage(String.format("**%s#%s:** *%s*", event.getAuthor().getName(), event.getAuthor().getDiscriminator(), event.getMessage().getContentRaw())).queue();
    }
}
