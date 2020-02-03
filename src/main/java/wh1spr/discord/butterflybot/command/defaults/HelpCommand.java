package wh1spr.discord.butterflybot.command.defaults;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import wh1spr.discord.butterflybot.command.Command;
import wh1spr.discord.butterflybot.command.CommandRegistry;
import wh1spr.discord.butterflybot.database.entities.users.UserEntity;

import java.awt.*;
import java.util.*;
import java.util.List;

public class HelpCommand extends Command {

    private CommandRegistry reg;
    private String prefix;
    // category-separated commands based on permission
    private HashMap<String, ArrayList<Command>> catCommands = null;
    // Command - names, list[0] being the "main" name, rest are aliases
    private HashMap<Command, List<String>> aliases = null;

    public HelpCommand(CommandRegistry reg, String prefix) {
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
        if (arg.isEmpty() || arg.toLowerCase().equals("-all")) {
            boolean all = arg.toLowerCase().equals("-all");
            // .help
            eb.setTitle("Butterfly Help")
                    .setDescription("A list of commands you can use")
                    .setColor(Color.cyan);
            if (all) {
                eb.setDescription("A list of all commands. Commands with an :x: are disabled.");
            }

            // create the fields
            // start with strings for the commands
            for (String cat : catCommands.keySet()) {
                List<Command> cmds = catCommands.get(cat);
                StringBuilder value = new StringBuilder();
                for (Command cmd : cmds) {
                    if (all || (cmd.isEnabled() &&
                            ue.hasOneOfPermissions(cmd.getPermissions())))
                    value.append(prefix).append(aliases.get(cmd).get(0))
                            .append(cmd.isEnabled() ? "" : " :x:").append("\n");
                }
                cat = cat.substring(0, 1).toUpperCase() + cat.substring(1);
                if (!value.toString().strip().isEmpty())
                    eb.addField(cat, value.toString().strip(), true);
            }
            channel.sendMessage(eb.build()).queue();
        } else {
            // .help command
            Command cmd = reg.getCommand(arg);
            if (cmd == null) {
                this.sendIncorrectUse(channel, msg);
            } else {
                eb.setColor(Color.cyan).setDescription(cmd.getHelpMsg())
                        .setTitle("Help for " + prefix + aliases.get(cmd).get(0));
                if (!cmd.isEnabled()) eb.setFooter("This command is NOT ENABLED").setColor(new Color(170, 0, 0));
                else if (!ue.hasOneOfPermissions(cmd.getPermissions()))
                    eb.setFooter("You do NOT have permission to use this command").setColor(new Color(170, 100, 0));
                //usage
                eb.addField("Usage",
                        String.format("`%s%s %s`", prefix, aliases.get(cmd).get(0), cmd.getUsageMsg()), false);
                //aliases
                if (aliases.get(cmd).size() > 1) {
                    String val = Arrays.toString(aliases.get(cmd).subList(1, aliases.get(cmd).size()).toArray());
                    val = val.substring(1, val.length()-1);
                    eb.addField("Aliases", String.format("*%s*", val), true);
                }
                //permissions
                if (ue.hasPermission("*")) {
                    String val = Arrays.toString(cmd.getPermissions().toArray());
                    val = val.substring(1, val.length()-1);
                    eb.addField("Permissions", String.format("*%s*", val), true);
                }

                channel.sendMessage(eb.build()).queue();
            }
        }
    }

    @Override
    public String getUsageMsg() {
        return "[-all|command]";
    }

    // If there's a better way that I didn't think off, please make a pull request for it :)
    private void buildCmdMap() {
        this.catCommands = new HashMap<>();
        this.aliases = new HashMap<>();
        //create map and stuff
        Map<String, Command> cmds = this.reg.getCommands(false);
        Map<String, Command> cmds2 = this.reg.getCommands(true);

        for (String name : new TreeSet<>(cmds.keySet())) {
            ArrayList<String> names = new ArrayList<>();
            names.add(name);
            aliases.put(cmds.get(name), names);

            String category = cmds.get(name).getPermissions().iterator().next().split("\\.")[0];
            if (!catCommands.containsKey(category)) {
                ArrayList<Command> commands = new ArrayList<>();
                commands.add(cmds.get(name));
                catCommands.put(category, commands);
            } else {
                catCommands.get(category).add(cmds.get(name));
            }
        }

        for (String name : new TreeSet<>(cmds2.keySet())) {
            if (!cmds.containsKey(name))
                aliases.get(cmds2.get(name)).add(name);
        }
    }

    @Override
    public String getHelpMsg() {
        return "The help command, shows the commands you can execute and can give more info about each command.\n" +
                "```Usage notation:\n" +
                "   <> - a required argument\n" +
                "   [] - an optional argument\n" +
                "   arg... - a repeatable argument```";
    }

}
