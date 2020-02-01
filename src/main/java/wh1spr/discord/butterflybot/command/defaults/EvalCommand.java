package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;

public class EvalCommand extends Command {

    public EvalCommand() {
        super("bot.eval");
    }

    @Override
    public void onGuildMessageReceived(JDA jda, Member author, TextChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author.getUser());
        if (!ue.hasPermission("bot.eval")) return;
        if (this.stripCommand(msg).isEmpty()) {
            sendIncorrectUse(msg);
            return;
        }
        ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
        se.put("guild", author.getGuild());
        this.evaluateScriptEngine(this.fillEngine(se, jda, author.getUser(), channel, msg), msg);
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        UserEntity ue = new UserEntity(author);
        if (!ue.hasPermission("bot.eval")) return;
        if (this.stripCommand(msg).isEmpty()) {
            sendIncorrectUse(msg);
            return;
        }
        ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
        this.evaluateScriptEngine(this.fillEngine(se, jda, author, channel, msg), msg);
    }

    private ScriptEngine fillEngine(ScriptEngine se, JDA jda, User author, MessageChannel channel, Message msg) {
        se.put("jda", jda);
        se.put("user", author);
        se.put("channel", channel);
        se.put("msg", msg);
        return se;
    }

    private String replaceWords(String msg) {
        msg = msg.replaceAll("OnlineStatus\\.", "Packages.net.dv8tion.jda.api.OnlineStatus.")
            .replaceAll("Activity\\.", "Packages.net.dv8tion.jda.api.entities.Activity.");

        return msg; //TODO do this with properties file?
    }

    private void evaluateScriptEngine(ScriptEngine se, Message m) {
        EmbedBuilder eb = new EmbedBuilder();
        String toEval = replaceWords(this.stripCommandRaw(m));
        try {
            Object output = se.eval(toEval);
            eb.setTitle(":white_check_mark: Succesfully Executed!")
                    .setColor(new Color(40,190,30))
                    .addField("Output","```" + output + "```", false);
        } catch (ScriptException e) {
            eb.setTitle(":x: Execution Failed.")
                    .setColor(new Color(170, 0, 0))
                    .addField("Output", "```" + e.getMessage() + "```", false);
        }
        m.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsageMsg() {
        return "<nashorn script code>";
    }

    @Override
    public String getHelpMsg() {
        return "Evaluates Nashorn code.";
    }
}
