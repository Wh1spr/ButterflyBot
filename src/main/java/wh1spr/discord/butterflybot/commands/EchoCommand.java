package wh1spr.discord.butterflybot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;

public class EchoCommand extends Command {

    public EchoCommand() {
        super("fun.echo", "fun.echo.private", "fun.echo.other");
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        channel.sendMessage(String.format("*%s*",super.stripCommand(msg))).queue();
    }
}
