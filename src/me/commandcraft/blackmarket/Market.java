package me.commandcraft.blackmarket;

import static org.bukkit.ChatColor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Market {
	private static ArrayList<Inventory> inventories = new ArrayList<Inventory>();
	public static ArrayList<LightPlayer> playersGui = new ArrayList<LightPlayer>();
	public static HashMap<LightPlayer, ArrayList<Inventory>> playersMe = new HashMap<LightPlayer, ArrayList<Inventory>>();
	public static HashMap<LightPlayer, ItemStack> playersRemove = new HashMap<LightPlayer, ItemStack>();

	
 	public static void openGui(String pName, int n) {
		inventories.clear();
		Inventory myInventory = Bukkit.createInventory(null, 54, "Items Aviable");
		int j = 0;
		int counter = 1;
		for (LightItem lItem : Main.market) {
			ItemStack item = new ItemStack(lItem.getMaterial(), lItem.getCuantity());
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(BOLD + lItem.getPlayer());
			lore.add(GREEN + Integer.toString(lItem.getPrice()));
			meta.setLore(lore);	
			item.setItemMeta(meta);
			myInventory.setItem(j, item);
			j++;
			if (j >= 45) {
				if (inventories.size() >= 1) {
					item = new ItemStack(Material.REDSTONE_BLOCK, 1);
					ItemMeta metadata = item.getItemMeta();
					metadata.setDisplayName("Previous page");
					item.setItemMeta(metadata);
					myInventory.setItem(48, item);
				}
				if (Main.market.size() - counter >= 1) {
					item = new ItemStack(Material.REDSTONE_BLOCK, 1);
					ItemMeta metadata = item.getItemMeta();
					metadata.setDisplayName("Next page");
					item.setItemMeta(metadata);
					myInventory.setItem(50, item);
				}
				inventories.add(myInventory);
				myInventory = Bukkit.createInventory(null, 54, "Items Aviable");
				counter++;
			}
		}
		if (myInventory.getContents().length > 0) {
			inventories.add(myInventory);
		}
		Bukkit.getServer().getPlayer(pName).openInventory(inventories.get(n));
	}
	
	public static void openMe(String player, int n) {
		ArrayList<Inventory> inventories = new ArrayList<Inventory>();
		Inventory myInventory = Bukkit.createInventory(null, 54, "Your items");
		int j = 0;
		int counter = 1;
		for (LightItem lItem : Main.market) {
			if (lItem.getPlayer().equals(player)) {
				ItemStack item = new ItemStack(lItem.getMaterial(), lItem.getCuantity());
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(GREEN + Integer.toString(lItem.getPrice()));
				meta.setLore(lore);	
				item.setItemMeta(meta);
				myInventory.setItem(j, item);
				j++;
				if (j >= 45) {
					if (inventories.size() >= 1) {
						item = new ItemStack(Material.REDSTONE_BLOCK, 1);
						ItemMeta metadata = item.getItemMeta();
						metadata.setDisplayName("Previous page");
						item.setItemMeta(metadata);
						myInventory.setItem(48, item);
					}
					if (Main.market.size() - counter > 1) {
						item = new ItemStack(Material.REDSTONE_BLOCK, 1);
						ItemMeta metadata = item.getItemMeta();
						metadata.setDisplayName("Next page");
						item.setItemMeta(metadata);
						myInventory.setItem(50, item);
					}
					inventories.add(myInventory);
					myInventory = Bukkit.createInventory(null, 54, "Items Aviable");
					counter++;
				}
			}
		}
		if (myInventory.getContents().length > 0) {
			inventories.add(myInventory);
		}
		Bukkit.getServer().getPlayer(player).openInventory(inventories.get(n));
		playersMe.put(new LightPlayer(player, n), inventories);
	}

	public static void openSure(LightPlayer lp) {
		Player p = Bukkit.getServer().getPlayer(lp.getPlayer());
		//ItemStack item = playersRemove.get(lp);
		Inventory sure = Bukkit.createInventory(null, 9, ChatColor.RED + "Are you sure?");
		ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Yes, return item to inventory");
		item.setItemMeta(meta);
		sure.setItem(2, item);
		item = new ItemStack(Material.WOOL, 1, (byte) 5);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "No, return to my items");
		item.setItemMeta(meta);
		sure.setItem(6, item);
		p.openInventory(sure);
	}
}
