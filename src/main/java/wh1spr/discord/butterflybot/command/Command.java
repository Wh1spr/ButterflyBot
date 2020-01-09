package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

//TODO Documentation
public abstract class Command {

    private Set<String> permissions = null;
    private boolean enabled = true;
    private static final String NO_HELP = "No help was found for this command.";

    protected Command(String... permissions) {
        HashSet<String> p = new HashSet<String>();
        for (int i = 0; i < permissions.length; i++) {
            p.add(permissions[i]);
        }
        this.permissions = p;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public void onGuildMessageReceived(JDA jda, Member author, TextChannel channel, Message msg) {
        onPrivateMessageReceived(jda, author.getUser(), channel, msg);
    }
    public abstract void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg);

    public synchronized void enable() {
        this.enabled = true;
    }

    public synchronized void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    protected final String stripCommand(Message m) {
        if (m.getContentStripped().split(" ").length < 2) return "";
        return m.getContentStripped().split(" ",2)[1];
    }
    protected final String getUsedCommand(Message m) {
        return m.getContentStripped().split(" ",2)[0];
    }

    //for help command
    public String getHelpMsg() {
        return NO_HELP;
    }
    public abstract String getUsageMsg(); // usage without command's name, prefix and the space

    protected final void sendIncorrectUse(Message m) {
        m.getChannel().sendMessage(new EmbedBuilder().setColor(new Color(170, 100, 0))
                .setTitle(String.format("Usage: `%s %s`", this.getUsedCommand(m), this.getUsageMsg())).build()).queue();
    }

    protected final void sendFailedMessage(Message m, String message) {
        m.getChannel().sendMessage(new EmbedBuilder().setColor(new Color(170, 0, 0))
                .setTitle(message).build()).queue();
    }

    protected final void checkmark(Message msg) {
        msg.addReaction("\u2705").queue();
    }
}
