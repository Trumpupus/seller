package de.user.sellplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SellGUI {

    public void openInventory(Player player) {
        Inventory sellInventory = Bukkit.createInventory(null, 54, "Sell Your Items");
        player.openInventory(sellInventory);
    }
}
