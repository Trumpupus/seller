package de.user.sellplugin;

import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SellPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private Economy econ = null;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        configManager = new ConfigManager(this);
        getLogger().info("SellPlugin has been enabled!");
        this.getCommand("sell").setExecutor(new SellCommand(this));
        this.getCommand("sellreload").setExecutor(new SellReloadCommand(this));
        getServer().getPluginManager().registerEvents(new SellInventoryListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SellPlugin has been disabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Economy getEconomy() {
        return econ;
    }
}
