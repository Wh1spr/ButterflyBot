package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.command.CommandRegistry;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {

    private CommandRegistry reg;
    private String prefix;
    // category-separated commands based on permission
    private HashMap<String, ArrayList<Command>> catCommands = null;

    protected HelpCommand(CommandRegistry reg, String prefix) {
        super("bot.help");
        if (reg == null) throw new IllegalArgumentException("Registry cannot be null");
        this.reg = reg;
        this.prefix = prefix;
    }

    @Override
    public void onGuildMessageReceived(JDA jda, Member author, TextChannel channel, Message msg) {
        if (!new UserEntity(author.getUser()).isBanned()) {
            channel.sendMessage(new EmbedBuilder().setTitle("Sending it to you!")
            .setDescription("If you're not receiving anything, try sending the command directly to me")
            .setColor(Color.cyan).build()).queue();
        }
        onPrivateMessageReceived(jda, author.getUser(), author.getUser().openPrivateChannel().complete(), msg);
    }

    @Override
    public void onPrivateMessageReceived(JDA jda, User author, MessageChannel channel, Message msg) {
        // .help command -> if owner or *, add in perms list
        if (catCommands == null) buildCmdMap();
        if (this.stripCommand(msg).split(" ").length > 1) {
            this.sendIncorrectUse(channel, msg);
            return;
        }
        UserEntity ue = new UserEntity(author);
        boolean isOwner = ue.isOwner();
        String arg = this.stripCommand(msg);


        EmbedBuilder eb = new EmbedBuilder().setColor(Color.cyan);
        if (arg.isEmpty()) {
            // .help
            eb.setTitle("Butterfly Help").setDescription("A list of commands you can use");


        } else if (arg.toLowerCase().equals("-all")) {
            // .help -all
        } else {
            // .help command
        }


    }

    @Override
    public String getUsageMsg() {
        return "[-all|command]";
    }

    private void buildCmdMap() {
        //create map and stuff
        Map<String, Command> cmds = this.reg.getCommands();
    }
}
