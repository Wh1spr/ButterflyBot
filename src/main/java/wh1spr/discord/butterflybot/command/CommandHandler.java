package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import wh1spr.discord.butterflybot.database.entities.messages.CommandMessageEntity;
import wh1spr.discord.butterflybot.database.entities.messages.PrivateMessageEntity;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.Objects;

//TODO Documentation
public class CommandHandler extends ListenerAdapter {

    private CommandRegistry reg;
    private final String PREFIX;
    private final MessageEmbed failed;

    public CommandHandler(String prefix, CommandRegistry reg) {
        if (reg == null) throw new IllegalArgumentException("Given CommandRegistry can not be null");
        if (prefix == null || prefix.isEmpty()) throw new IllegalArgumentException("Given prefix can not be null or empty");
        this.reg = reg;
        this.PREFIX = prefix;
        this.failed = new EmbedBuilder().setColor(new Color(170, 0, 0))
                .setTitle("An exception has occurred. We apologise for the inconvenience.").build();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Command c = getCommand(event.getAuthor(), event.getMessage());
        if (c != null) {
            CommandMessageEntity cme = new CommandMessageEntity(event.getMessage(), false);
            try {
                c.onGuildMessageReceived(event.getJDA(), Objects.requireNonNull(event.getMember()), event.getChannel(), event.getMessage());
            } catch (Exception e) {
                cme.exception(e);
                event.getChannel().sendMessage(this.failed).queue();
            }
        }

    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        Command c = getCommand(event.getAuthor(), event.getMessage());
        if (!event.getAuthor().isBot()) new PrivateMessageEntity(event.getMessage());
        if (c != null) {
            CommandMessageEntity cme = new CommandMessageEntity(event.getMessage(), false);
            try {
                c.onPrivateMessageReceived(event.getJDA(), event.getAuthor(), event.getChannel(), event.getMessage());
            } catch (Exception e) {
                cme.exception(e);
                event.getChannel().sendMessage(this.failed).queue();
            }
        }
    }

    /**
     * Returns the command that will be executed
     * @return Command if it should be executed
     *          or null if a command should not be executed
     */
    private Command getCommand(User author, Message m) {
        if (author.isBot()) return null;
        // Get command
        if (!m.getContentStripped().startsWith(PREFIX)) return null;
        String command = m.getContentStripped().split(" ",2)[0].substring(PREFIX.length()).toLowerCase();
        if (!reg.commandExists(command)) return null;
        Command c = reg.getCommand(command);
        if (!c.isEnabled()) return null;
        UserEntity ue = new UserEntity(author);
        if (!ue.isOwner() && ue.isBanned()) return null;
        return c;
    }
}
