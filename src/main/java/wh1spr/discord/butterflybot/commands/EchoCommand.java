package wh1spr.discord.butterflybot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.UserEntity;

public class EchoCommand extends Command {

    public EchoCommand() {
        super("fun.echo.normal", "fun.echo.user", "fun.echo.other");
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);

        String firstWord = super.stripCommand(msg).split(" ", 2)[0];
        String sentence = (super.stripCommand(msg) + " ").split(" ", 2)[1];
        if (firstWord.startsWith("u:")) {
            // send to user
            if (!ue.hasPermission("fun.echo.user")) return;
            if (sentence.isEmpty()) {
                super.sendFailedMessage(msg, "You should probably have a message here");
                return;
            }

            User to = jda.getUserById(firstWord.substring(2));
            if (to == null) {
                super.sendFailedMessage(msg, "I can't find that user");
            } else {
                try{
                    to.openPrivateChannel().complete(true).sendMessage(sentence).queue();
                } catch (Exception e) {
                    super.sendFailedMessage(msg, "I don't have permission to do that");
                }
            }
        } else if (firstWord.startsWith("c:")) {
            // send to another channel
            if (!ue.hasPermission("fun.echo.other")) return;
            if (sentence.isEmpty()) {
                super.sendFailedMessage(msg, "You should probably have a message here");
                return;
            }

            TextChannel to = jda.getTextChannelById(firstWord.substring(2));
            if (to == null) {
                super.sendFailedMessage(msg, "I can't find that channel");
            } else if (!to.canTalk()) {
                super.sendFailedMessage(msg, "I don't have permission to do that");
            } else {
                to.sendMessage(sentence).queue();
                msg.addReaction("\u2705").queue();
            }
        } else {
            if (!ue.hasPermission("fun.echo.normal")) return;
            if (super.stripCommand(msg).isEmpty()) {
                super.sendFailedMessage(msg, "You should probably have a message here");
                return;
            }
            channel.sendMessage(String.format("*%s*", super.stripCommand(msg))).queue();
        }
    }

    @Override
    public String getUsageMsg() {
        return "[c:<channelId>|u:<userId>] <msg>";
    }

    @Override
    public String getHelpMsg() {
        return "Used to send a message to the current channel, another server channel or a user.";
    }
}
