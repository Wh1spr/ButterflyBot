package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import wh1spr.discord.butterflybot.database.Database;
import wh1spr.discord.butterflybot.database.entities.UserEntity;

//TODO Documentation
public class CommandHandler extends ListenerAdapter {

    private CommandRegistry reg;
    private final String PREFIX;

    public CommandHandler(String prefix, CommandRegistry reg) {
        if (reg == null) throw new IllegalArgumentException("Given CommandRegistry can not be null");
        if (prefix == null || prefix.isEmpty()) throw new IllegalArgumentException("Given prefix can not be null or empty");
        this.reg = reg;
        this.PREFIX = prefix;
        // TODO auto-register enable/disable, shutdown, eval
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        checkUser(event.getAuthor());
        // Get command
        if (event.isWebhookMessage() || event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentStripped().startsWith(PREFIX)) return;
        String command = event.getMessage().getContentStripped().split(" ",2)[0].substring(1).toLowerCase();
        if (!reg.commandExists(command)) return;
        Command c = reg.getCommand(command);
        if (!c.isEnabled()) return;

        // Log command call?
        c.onGuildMessageReceived(event.getJDA(), event.getMember(), event.getChannel(), event.getMessage());
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        checkUser(event.getAuthor());
        // Get command
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentStripped().startsWith(PREFIX)) return;
        String command = event.getMessage().getContentStripped().split(" ",2)[0].substring(1).toLowerCase();
        if (!reg.commandExists(command)) return;
        Command c = reg.getCommand(command);
        if (!c.isEnabled()) return;

        // Log command call?
        c.onPrivateMessageReceived(event.getJDA(), event.getAuthor(), event.getChannel(), event.getMessage());
    }

    public boolean disableCommand(String commandName) { //TODO make command for this
        if (!this.reg.commandExists(commandName.toLowerCase())) return false;
        else this.reg.getCommand(commandName.toLowerCase()).disable();
        return true;
    }

    public boolean enableCommand(String commandName) { //TODO make command for this
        if (!this.reg.commandExists(commandName.toLowerCase())) return false;
        else this.reg.getCommand(commandName.toLowerCase()).enable();
        return true;
    }

    private void checkUser(User u) {
        new UserEntity(u);
    }
}
