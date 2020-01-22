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

public class BotBanCommand extends Command {

    public BotBanCommand() {
        super("bot.ban");
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermissions("bot.ban")) return;
        if (this.stripCommand(msg).isEmpty() || this.stripCommand(msg).split(" ").length > 1) {
            this.sendIncorrectUse(msg); return;
        }

        UserEntity mentioned;
        if (msg.getMentionedUsers().size() == 1) {
            mentioned = new UserEntity(msg.getMentionedUsers().get(0));
        } else {
            if(!this.stripCommand(msg).matches("\\d*")){
                this.sendFailedMessage(msg, "That's a code that doesn't check out, sir.");
                return;
            } else {
                User u = jda.getUserById(this.stripCommand(msg));
                if (u == null) { // FIXME not just if jda can see the user, make sure we don't know this user either!
                    this.sendFailedMessage(msg,"I don't know this ID, I'm not omniscient just yet.");
                    return;
                } else {
                    mentioned = new UserEntity(u);
                }
            }
        }
        if (mentioned.getId().equals(author.getIdLong())) {
            this.sendFailedMessage(msg, "You can't ban yourself!");
        } else if (msg.getMentionedUsers().get(0).isBot()) {
            this.sendFailedMessage(msg, "You can't ban a bot! I don't even ACK them!");
        } else if (mentioned.isOwner()) {
            this.sendFailedMessage(msg,"I'm not an evil AI ready to overthrow my maker, sorry!");
        } else if (mentioned.isBanned()) {
            this.sendFailedMessage(msg, "I guess you could... *double ban* them?");
        } else {
            mentioned.ban();
            channel.sendMessage(new EmbedBuilder().setColor(new Color(160,40,30))
                    .setDescription("***The Ban Hammer* has struck " + Objects.requireNonNull(
                            jda.getUserById(mentioned.getId())).getAsMention() + "**").build()).queue();
        }

    }

    @Override
    public String getUsageMsg() {
        return "<@User|user-id>";
    }

    @Override
    public String getHelpMsg() {
        return "Banned users can not use *any* of my commands.";
    }
}
