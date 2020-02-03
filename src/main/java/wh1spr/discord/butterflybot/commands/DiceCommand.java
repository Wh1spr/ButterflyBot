package wh1spr.discord.butterflybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DiceCommand extends Command {

    public DiceCommand() {
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
        ArrayList<Die> diceList = new ArrayList<>();

        String[] args = this.stripCommand(msg).split(" ");
        for (String arg : args) {
            if (arg.matches("^\\d+d\\d+$")) {
                Die d = new Die(Integer.parseInt(arg.split("d")[0]),
                        Integer.parseInt(arg.split("d")[1]));
                total += d.num;
                if (total > 50 || total < 1) {
                    this.sendFailedMessage(msg, "Try a number of dice between 1 and 50.");
                    return;
                } else if (d.faces > 9999) {
                    this.sendFailedMessage(msg, "The maximum amount of faces is 9999, please keep that in mind.");
                    return;
                } else if (d.faces == 0) {
                    this.sendFailedMessage(msg, "I don't know man, I don't think I can do a 0 face roll...");
                    return;
                } else {
                    diceList.add(d);
                }
            } else {
                this.sendFailedMessage(msg, String.format("Couldn't parse '%s'. Rito pls fix.", arg));
                return;
            }
        }

        EmbedBuilder res = new EmbedBuilder().setTitle(":game_die: The dice were rolled!").setColor(Color.cyan);
        for (Die d : diceList) {
            StringBuilder s = new StringBuilder();
            s.append("*");
            s.append(this.rand.nextInt(d.faces)+1);
            for (int i=1; i < d.num; i++) {
                s.append(", ").append(this.rand.nextInt(d.faces)+1);
            }
            s.append("*");

            res.addField(String.format("%dd%d", d.num, d.faces), s.toString(), true);
        }
        channel.sendMessage(res.build()).queue();
    }

    private static class Die {
        private int num, faces;
        private Die(int num, int faces) {
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
        return "Roll some dice! Max 50 dice, Max 9999 faces.\n" +
                "Notation is 'xdy', where x is the number of dice, and y is the number of faces.\n" +
                "e.g.: '1d2' - one coinflip\n" +
                "e.g.: '5d2' - five coinflips\n" +
                "e.g.: '2d6' - 2 standard cube dice\n";
    }
}
