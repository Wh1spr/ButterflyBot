package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.command.CommandRegistry;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnableCommand extends Command {

    private CommandRegistry reg;

    public EnableCommand(CommandRegistry reg) {
        super("bot.enablecommand");
        if (reg == null) throw new IllegalArgumentException("Given CommandRegistry is null");
        this.reg = reg;
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        //user needs bot.enablecommand perm
        // user also needs permissions of the command!
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermission("bot.enablecommand")) return;
        else if (this.stripCommand(msg).isEmpty()) {
            this.sendIncorrectUse(msg);
            return;
        }

        List<String> toEnable = new ArrayList<>();
        List<String> wasEnabled = new ArrayList<>();
        List<String> noPerm = new ArrayList<>();
        List<String> noExist = new ArrayList<>();

        for (String cname : this.stripCommand(msg).toLowerCase().split(" ")) {
            Command cmd = reg.getCommand(cname);
            if (cmd == null) noExist.add(cname);
            else {
                if (!ue.hasPermissions(Arrays.toString(cmd.getPermissions().toArray()))) noPerm.add(cname);
                else {
                    if (cmd.isEnabled()) {
                        wasEnabled.add(cname);
                    } else {
                        cmd.enable();
                        toEnable.add(cname);
                    }
                }
            }
        }

        //make a nice list
        EmbedBuilder eb = new EmbedBuilder().setTitle(":warning: Enable Command Output")
                .setColor(new Color(0,124,215));
        if (toEnable.size() != 0) {
            eb.addField("Enabled", "`" + listToString(toEnable) + "`", false);
        }
        if (wasEnabled.size() != 0) {
            eb.addField("Already Enabled", "`" + listToString(wasEnabled) + "`", false);
        }
        if (noPerm.size() != 0) {
            eb.addField("No Permission", "`" + listToString(noPerm) + "`", false);
        }
        if (noExist.size() != 0) {
            eb.addField("Does Not Exist", "`" + listToString(noExist) + "`", false);
        }

        channel.sendMessage(eb.build()).queue();
    }

    private String listToString(List<String> list) {
        StringBuilder res = new StringBuilder();
        for (String s : list) {
            res.append(", ").append(s);
        }
        return res.substring(2);
    }

    @Override
    public String getUsageMsg() {
        return "<cmd/alias> [cmd/alias]...";
    }

    @Override
    public String getHelpMsg() {
        return "Used to enable a command in case there is contains a bug, is being abused,...";
    }

    // Command can not be enabled.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
