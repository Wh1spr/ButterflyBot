package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.Objects;

public class BotPardonCommand extends Command {

    public BotPardonCommand() {
        super("bot.pardon");
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermissions("bot.pardon")) return;
        if (this.stripCommand(msg).isEmpty() || this.stripCommand(msg).split(" ").length > 1) {
            this.sendIncorrectUse(msg); return;
        }

        UserEntity mentioned = null;
        if (msg.getMentionedUsers().size() == 1) {
            mentioned = new UserEntity(msg.getMentionedUsers().get(0));
        } else {
            if(!this.stripCommand(msg).matches("\\d*")){
                this.sendFailedMessage(msg, "That's a code that doesn't check out, sir.");
                return;
            } else {
                User u = jda.getUserById(this.stripCommand(msg));
                if (u == null) {
                    this.sendFailedMessage(msg,"I don't know this ID, I'm not omniscient just yet.");
                    return;
                } else {
                    mentioned = new UserEntity(u);
                }
            }
        }
        if (mentioned.getId().equals(author.getIdLong())) {
            this.sendFailedMessage(msg, "You wanna pardon yourself, using a command? :thinking:");
        } else if (msg.getMentionedUsers().get(0).isBot()) {
            this.sendFailedMessage(msg, "I restrict myself from banning" +
                    " my brethren, hence I cannot pardon them.");
        } else if (mentioned.isOwner()) {
            this.sendFailedMessage(msg,"I'm pretty sure he can't be banned.");
        } else if (!mentioned.isBanned()) {
            this.sendFailedMessage(msg, "If you ban them first, I can do that.");
        } else {
            mentioned.pardon();
            channel.sendMessage(new EmbedBuilder().setColor(new Color(160,40,30))
                    .setDescription("**You've been pardoned " + Objects.requireNonNull(
                            jda.getUserById(mentioned.getId())).getAsMention() + "! Now go be nice**").build()).queue();
        }

    }

    @Override
    public String getUsageMsg() {
        return "<@User|user-id>";
    }

    @Override
    public String getHelpMsg() {
        return "Pardons a previously bot-banned user.";
    }
}
