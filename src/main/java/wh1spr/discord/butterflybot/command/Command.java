package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.HashSet;
import java.util.Set;

//TODO add enable/disable
public abstract class Command {

    private Set<String> permissions = null;

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

    public abstract void onGuildMessageReceived(MessageReceivedEvent event, JDA jda, User author, MessageChannel channel, Message msg);
    public abstract void onPrivateMessageReceived(PrivateMessageReceivedEvent event, JDA jda, User author, PrivateChannel channel, Message msg);
}
