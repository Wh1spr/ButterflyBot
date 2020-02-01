package wh1spr.discord.butterflybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import wh1spr.discord.butterflybot.command.CommandHandler;
import wh1spr.discord.butterflybot.command.CommandRegistry;
import wh1spr.discord.butterflybot.commands.EchoCommand;
import wh1spr.discord.butterflybot.database.Database;
import wh1spr.discord.butterflybot.database.entities.users.UserPermissions;

import javax.security.auth.login.LoginException;
import java.nio.file.Paths;

public class ButterflyBot extends ListenerAdapter {

    private final JDA jda;
    private CommandRegistry reg;
    private CommandHandler handler;

    public ButterflyBot(String token, String prefix, String dbURL, String dbName, String defaultPermsPath) throws LoginException {
        if (prefix != null && prefix.contains(" ")) throw new IllegalArgumentException("Prefix can not contain a space");
        Database.createInstance(dbURL, dbName);
        if (defaultPermsPath != null)
            UserPermissions.loadDefaultPermissions(this, Paths.get(defaultPermsPath));
        this.registerCommands(prefix);
        this.jda = new JDABuilder(token)
                .addEventListeners(this, this.handler)
                .build();
    }

    private void registerCommands(String prefix) {
        this.reg = new CommandRegistry(prefix==null?".":prefix);

        //register commands
        reg.registerCommand("echo", new EchoCommand());

        this.handler = new CommandHandler(prefix==null?".":prefix, reg);
    }

    public CommandHandler getCommandHandler() {
        return this.handler;
    }
    public CommandRegistry getCommandRegistry() {
        return this.reg;
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("ButterflyBot is ready to go!");
    }

}
