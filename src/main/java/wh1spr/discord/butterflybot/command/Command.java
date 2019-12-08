package wh1spr.discord.butterflybot.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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

    public void onGuildMessageReceived(JDA jda, Member author, TextChannel channel, Message msg) {
        onPrivateMessageReceived(jda, author.getUser(), channel, msg);
    }
    public abstract void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg);
}
