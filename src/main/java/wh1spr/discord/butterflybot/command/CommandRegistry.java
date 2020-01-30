package wh1spr.discord.butterflybot.command;

import wh1spr.discord.butterflybot.command.defaults.*;

import java.util.*;

public class CommandRegistry {

    private Map<String, Command> commands = new HashMap<>();
    private Map<String, Command> reqCmds = new HashMap<>();
    private Set<String> perms = new HashSet<>();

    public CommandRegistry() {
        /*
         TODO Register required commands
         these are help, perms (add/remove/reset), info
         permissions are bot.*
         */
        this.registerCommand(true, "shutdown", new ShutdownCommand());
        this.registerCommand(true, "disablecommand", new DisableCommand(this), "dcmd");
        this.registerCommand(true, "enablecommand", new EnableCommand(this), "ecmd");

        this.registerCommand(true, "botban", new BotBanCommand(), "bban");
        this.registerCommand(true, "botpardon", new BotPardonCommand(), "bpardon");

        this.registerCommand(true, "eval", new EvalCommand());
    }

    /**
     * @return A Set of all the registered permissions in this CommandRegistry
     */
    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(perms);
    }

    /**
     * Register a Command with CommandRegistry.
     * @param commandName The standard name for the command
     * @param command The Command instance
     * @param aliases A (possibly) list of aliases for this command
     * @throws IllegalArgumentException If the commandName or an alias was already taken
     * @throws IllegalArgumentException If a command-specific permission was already registered.
     *          A command can only register new permissions.
     */
    public void registerCommand(String commandName, Command command, String... aliases) {
        registerCommand(false, commandName, command, aliases);
    }

    private void registerCommand(boolean isReq, String commandName, Command command, String... aliases) {
        if (commands.containsKey(commandName.toLowerCase()) || reqCmds.containsKey(commandName.toLowerCase()))
            throw new IllegalArgumentException("CommandName already registered.");
        else {
            for (String alias : aliases) {
                if (commands.containsKey(alias.toLowerCase()) || reqCmds.containsKey(commandName.toLowerCase()))
                    throw new IllegalArgumentException("Alias '" + alias + "' already registered.");
            }
        }
        for (String perm : command.getPermissions()) {
            if (this.permissionExists(perm))
                throw new IllegalArgumentException("Permission '" + perm + "' already registered.");
        }

        if (isReq) {
            reqCmds.put(commandName.toLowerCase(), command);
            for (String alias : aliases) {
                reqCmds.put(alias.toLowerCase(), command);
            }
        } else {
            commands.put(commandName.toLowerCase(), command);
            for (String alias : aliases) {
                commands.put(alias.toLowerCase(), command);
            }
        }
        this.perms.addAll(command.getPermissions());
    }

    /**
     * Returns the command with the given name or alias.
     * @param commandName The name or alias of the command
     * @return A Command instance or null if it does not exist
     */
    public Command getCommand(String commandName) {
        if (this.commands.containsKey(commandName.toLowerCase()))
            return this.commands.get(commandName.toLowerCase());
        else
            return this.reqCmds.get(commandName.toLowerCase());
    }

    /**
     * Returns whether or not a command with the given name or alias exists in the CommandRegistry.
     * @param commandName The name or alias of the command
     * @return True if it exists
     */
    public boolean commandExists(String commandName) {
        return this.commands.containsKey(commandName.toLowerCase())
                || this.reqCmds.containsKey(commandName.toLowerCase());
    }

    /**
     * Returns whether or not a command with the given permission exists in the CommandRegistry.
     * @param permission The permission to check
     * @return True if it exists
     */
    public boolean permissionExists(String permission) {
        return this.perms.contains(permission.toLowerCase());
    }

    /**
     * Returns the amount of registered commands.
     */
    public int size() {
        return commands.size() + reqCmds.size();
    }
}
