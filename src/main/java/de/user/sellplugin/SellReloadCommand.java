package de.user.sellplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SellReloadCommand implements CommandExecutor {

    private final SellPlugin plugin;

    public SellReloadCommand(SellPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sellplugin.reload")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        plugin.getConfigManager().reloadConfigs();
        sender.sendMessage("SellPlugin configuration reloaded.");
        return true;
    }
}
