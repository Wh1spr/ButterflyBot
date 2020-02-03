package wh1spr.discord.butterflybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.Random;

public class DiceCommand extends Command {

    protected DiceCommand() {
        super("fun.dice");
        rand = new Random();
    }

    private Random rand = null;

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermission("fun.dice")) return;
        if (this.stripCommand(msg).isEmpty()) {
            this.sendIncorrectUse(msg);
            return;
        }
        //get the dice to roll
        int total = 0;

        String[] args = this.stripCommand(msg).split(" ");
        for (String arg : args) {
            if (arg.matches("^\\d+d\\d+$")) {
                Dice d = new Dice(Integer.parseInt(arg.split("d")[0]),
                        Integer.parseInt(arg.split("d")[0]));
                total += d.num;
                if (total > 50) {
                    this.sendFailedMessage(msg, "Try less dice, less then 50 to be exact.");
                    break;
                } else if (d.faces > 9999) {
                    this.sendFailedMessage(msg, "The maximum amount of faces is 9999, please keep that in mind.");
                    break;
                } else {
                    //good to go
                }
            }
        }
    }

    private class Dice {
        private int num, faces;
        private Dice(int num, int faces) {
            this.num = num;
            this.faces = faces;
        }
    }

    @Override
    public String getUsageMsg() {
        return "<1d2 = roll 1 dice, 2 faces>...";
    }

    @Override
    public String getHelpMsg() {
        return "Roll some dice! Max 50 dice, Max 9999 faces";
    }
}
