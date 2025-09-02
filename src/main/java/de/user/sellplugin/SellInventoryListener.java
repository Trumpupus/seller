package de.user.sellplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.text.DecimalFormat;

public class SellInventoryListener implements Listener {

    private final SellPlugin plugin;

    public SellInventoryListener(SellPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals("Sell Your Items")) {
            return;
        }

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        double totalPrice = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                continue;
            }

            if (item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
                if (blockStateMeta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                    for (ItemStack shulkerItem : shulkerBox.getInventory().getContents()) {
                        if (shulkerItem != null) {
                            totalPrice += getPrice(shulkerItem) * shulkerItem.getAmount();
                        }
                    }
                }
            } else {
                totalPrice += getPrice(item) * item.getAmount();
            }
        }

        if (totalPrice > 0) {
            // Clear the inventory after selling
            inventory.clear();

            // Give the player the money
            plugin.getEconomy().depositPlayer(player, totalPrice);

            // Send the message to the player
            String messageFormat = plugin.getConfigManager().getConfig().getString("sell-message", "&a+%price% €");
            boolean formatNumbers = plugin.getConfigManager().getConfig().getBoolean("format-numbers", true);

            String formattedPrice = formatPrice(totalPrice, formatNumbers);
            String message = messageFormat.replace("%price%", formattedPrice);

            player.sendActionBar(Component.text(message.replace("&", "§")));
        }
    }

    private double getPrice(ItemStack item) {
        String itemName = item.getType().name().toLowerCase();
        return plugin.getConfigManager().getPricesConfig().getDouble(itemName, 0.0);
    }

    private String formatPrice(double price, boolean format) {
        if (!format) {
            return String.valueOf(price);
        }

        if (price < 1000) {
            return new DecimalFormat("0.##").format(price);
        } else if (price < 1_000_000) {
            return new DecimalFormat("0.##").format(price / 1000) + "k";
        } else {
            return new DecimalFormat("0.##").format(price / 1_000_000) + "M";
        }
    }
}
