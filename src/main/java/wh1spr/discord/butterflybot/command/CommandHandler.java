package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.util.Objects;

//TODO Documentation
public class CommandHandler extends ListenerAdapter {

    private CommandRegistry reg;
    private final String PREFIX;

    public CommandHandler(String prefix, CommandRegistry reg) {
        if (reg == null) throw new IllegalArgumentException("Given CommandRegistry can not be null");
        if (prefix == null || prefix.isEmpty()) throw new IllegalArgumentException("Given prefix can not be null or empty");
        this.reg = reg;
        this.PREFIX = prefix;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Command c = getCommand(event.getAuthor(), event.getMessage());
        if (c != null)
            c.onGuildMessageReceived(event.getJDA(), Objects.requireNonNull(event.getMember()), event.getChannel(), event.getMessage());
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        Command c = getCommand(event.getAuthor(), event.getMessage());
        if (c != null)
            c.onPrivateMessageReceived(event.getJDA(), event.getAuthor(), event.getChannel(), event.getMessage());
    }

    /**
     * Returns the command that will be executed
     * @return Command if it should be executed
     *          or null if a command should not be executed
     */
    private Command getCommand(User author, Message m) {
        UserEntity ue = new UserEntity(author);
        if (!ue.isOwner() && ue.isBanned()) return null;
        // Get command
        if (author.isBot()) return null;
        if (!m.getContentStripped().startsWith(PREFIX)) return null;
        String command = m.getContentStripped().split(" ",2)[0].substring(1).toLowerCase();
        if (!reg.commandExists(command)) return null;
        else {
            // TODO log message
        }
        Command c = reg.getCommand(command);
        if (!c.isEnabled()) return null;

        return c;
    }
}
