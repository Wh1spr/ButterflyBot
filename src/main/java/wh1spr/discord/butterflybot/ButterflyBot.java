package wh1spr.discord.butterflybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class ButterflyBot extends ListenerAdapter {

    private final JDA jda;

    public ButterflyBot(String token) throws LoginException {
        this.jda = new JDABuilder(token)
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("ButterflyBot is ready to go!");
    }

}
