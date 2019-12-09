package wh1spr.discord.butterflybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import wh1spr.discord.butterflybot.command.CommandHandler;
import wh1spr.discord.butterflybot.command.CommandRegistry;
import wh1spr.discord.butterflybot.commands.EchoCommand;
import wh1spr.discord.butterflybot.database.Database;

import javax.security.auth.login.LoginException;

public class ButterflyBot extends ListenerAdapter {

    private final JDA jda;
    private CommandRegistry reg;
    private CommandHandler handler;

    public ButterflyBot(String token, String dbURL) throws LoginException {
        Database.createInstance(dbURL);
        this.registerCommands();
        this.jda = new JDABuilder(token)
                .addEventListeners(this, this.handler)
                .build();
    }

    private void registerCommands() {
        this.reg = new CommandRegistry();

        //register commands
        reg.registerCommand("echo", new EchoCommand());

        this.handler = new CommandHandler(".", reg);
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("ButterflyBot is ready to go!");
    }

}
