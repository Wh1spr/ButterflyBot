package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super( "bot.shutdown");
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);
        if (ue.hasPermission("bot.shutdown")) {
            if (this.stripCommand(msg).isEmpty()) {
                this.sendIncorrectUse(msg);
                return;
            }
            // For now, I don't need to save anything or the like, so just exit.
            jda.shutdownNow();
            System.exit(0);
        }
    }

    @Override
    public String getUsageMsg() {
        return "";
    }

    @Override
    public String getHelpMsg() {
        return "Shuts down the bot.";
    }
}
