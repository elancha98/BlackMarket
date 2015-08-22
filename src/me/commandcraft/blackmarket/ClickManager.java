package me.commandcraft.blackmarket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickManager implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		if (slot == 48 || slot == 50) {
			int n;
			if (slot == 48) {n = -1;} else {n = 1;}
			if (LightPlayer.isPlayerGui(p.getName())) {
				LightPlayer lp = LightPlayer.getPlayerGui(p.getName());
				Market.playersGui.remove(lp);
				lp.setInventory(lp.getInventory() + n);
				Market.playersGui.add(lp);
			} else if (LightPlayer.isPlayerMe(p.getName())) {
				LightPlayer lp = LightPlayer.getPlayerMe(p.getName());
				ArrayList<Inventory> inventoris = Market.playersMe.get(lp);
				Market.playersMe.remove(lp);
				lp.setInventory(lp.getInventory() + n);
				Market.playersMe.put(lp, inventoris);
			}
		} else {
			if (LightPlayer.isPlayerGui(p.getName())) {
				event.setCancelled(true);
				ItemStack item = event.getClickedInventory().getItem(event.getSlot());
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				Player seller = Bukkit.getServer().getPlayer(lore.get(0).replace("§l", ""));
				int price = Integer.parseInt(lore.get(1).replace("§a", ""));
				if (Main.economy.getBalance(p) > price) {
					Main.market.remove(LightItem.toLightItem(item, seller.getName()));
					Main.economy.withdrawPlayer(p, price);
					HashMap<Integer, ItemStack> result = p.getInventory().addItem(item);
					if (!result.isEmpty()) {
						for (ItemStack its : result.values()) {
							p.getLocation().getWorld().dropItem(p.getLocation(), its);
						}
					}
					Main.economy.depositPlayer(seller, price);
					p.closeInventory();
				} else {
					p.sendMessage(ChatColor.RED + "Sorry that's too expensive");
				}
			} else if (LightPlayer.isPlayerMe(p.getName())) {
				event.setCancelled(true);
				ItemStack item = event.getClickedInventory().getItem(slot);
				LightPlayer lp = LightPlayer.getPlayerMe(p.getName());
				Market.playersRemove.put(lp, item);
				Market.playersMe.remove(lp);
				Bukkit.getServer().getPlayer(lp.getPlayer()).closeInventory();
				Market.openSure(lp);
			} else if (LightPlayer.isPlayerRemove(p.getName())) {
				event.setCancelled(true);
				LightPlayer lp = LightPlayer.getPlayerRemove(p.getName());
				if (slot == 2) {
					LightItem li = LightItem.toLightItem(Market.playersRemove.get(lp), p.getName());
					Main.market.remove(li);
					HashMap<Integer, ItemStack> result = p.getInventory().addItem(Market.playersRemove.get(lp));
					if (!result.isEmpty()) {
						for (ItemStack i : result.values()) {
							p.getLocation().getWorld().dropItem(p.getLocation(), i);
						}
					}
					p.closeInventory();
				} else if (slot == 6) {
					Bukkit.getServer().getPlayer(lp.getPlayer()).closeInventory();
					Market.playersRemove.remove(lp);
					Market.openMe(lp.getPlayer(), 0);
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		if (Market.playersGui.contains(p.getName())) {
			Market.playersGui.remove(p.getName());
		}
		if (Market.playersMe.containsKey(p.getName())) {
			Market.playersMe.remove(p.getName());
		}
		if (Market.playersRemove.containsKey(p.getName())) {
			Market.playersRemove.remove(p.getName());
		}
	}
}
