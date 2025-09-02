package de.user.sellplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SellPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        getLogger().info("SellPlugin has been enabled!");
        this.getCommand("sell").setExecutor(new SellCommand(this));
        this.getCommand("sellreload").setExecutor(new SellReloadCommand(this));
        getServer().getPluginManager().registerEvents(new SellInventoryListener(this), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("SellPlugin has been disabled!");
    }
}
