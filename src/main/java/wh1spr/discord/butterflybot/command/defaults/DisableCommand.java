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

public class DisableCommand extends Command {

    private CommandRegistry reg = null;

    public DisableCommand(CommandRegistry reg) {
        super("bot.disablecommand");
        if (reg == null) throw new IllegalArgumentException("Given CommandRegistry is null");
        this.reg = reg;
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        //user needs bot.disablecommand perm
        // user also needs permissions of the command!
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermission("bot.disablecommand")) return;
        else if (this.stripCommand(msg).isEmpty()) {
            this.sendIncorrectUse(msg);
            return;
        }

        List<String> toDisable = new ArrayList<>();
        List<String> wasDisabled = new ArrayList<>();
        List<String> cantDisable = new ArrayList<>(); // by checking perm, bot.* cant be
        List<String> noPerm = new ArrayList<>();
        List<String> noExist = new ArrayList<>();

        for (String cname : this.stripCommand(msg).toLowerCase().split(" ")) {
            Command cmd = reg.getCommand(cname);
            if (cmd == null) noExist.add(cname);
            else {
                if (!ue.hasPermissions(Arrays.toString(cmd.getPermissions().toArray()))) noPerm.add(cname);
                else {
                    boolean canDisable = true;
                    for (String perm : cmd.getPermissions()) {
                        if (perm.startsWith("bot.")) {
                            cantDisable.add(cname);
                            canDisable = false;
                            break;
                        }
                    }
                    if (canDisable) {
                        if (cmd.isEnabled()) {
                            cmd.disable();
                            toDisable.add(cname);
                        } else {
                            wasDisabled.add(cname);
                        }
                    }
                }
            }
        }

        //make a nice list
        EmbedBuilder eb = new EmbedBuilder().setTitle(":warning: Disable Command Output")
                .setColor(new Color(0,124,215));
        if (toDisable.size() != 0) {
            List<String> names = new ArrayList<>();
            eb.addField("Disabled", "`" + listToString(toDisable) + "`", false);
        }
        if (wasDisabled.size() != 0) {
            List<String> names = new ArrayList<>();
            eb.addField("Already Disabled", "`" + listToString(wasDisabled) + "`", false);
        }
        if (cantDisable.size() != 0) {
            List<String> names = new ArrayList<>();
            eb.addField("Not Disableable", "`" + listToString(cantDisable) + "`", false);
        }
        if (noPerm.size() != 0) {
            List<String> names = new ArrayList<>();
            eb.addField("No Permission", "`" + listToString(noPerm) + "`", false);
        }
        if (noExist.size() != 0) {
            List<String> names = new ArrayList<>();
            eb.addField("Non-Existent", "`" + listToString(noExist) + "`", false);
        }

        channel.sendMessage(eb.build()).queue();
    }

    private String listToString(List<String> list) {
        String res = "";
        for (String s : list) {
            res += ", " + s;
        }
        return res.substring(2);
    }

    @Override
    public String getUsageMsg() {
        return "<cmd/alias> [cmd/alias]...";
    }

    @Override
    public String getHelpMsg() {
        return "Used to disable a command in case there is contains a bug, is being abused,...";
    }

    // Command can not be disabled.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
