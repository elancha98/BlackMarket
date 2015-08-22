package me.commandcraft.blackmarket;

import static org.bukkit.ChatColor.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main extends JavaPlugin {
	public static ArrayList<LightItem> market = new ArrayList<LightItem>();
	public static ArrayList<LightItem> temp = new ArrayList<LightItem>();
	protected static int time;
	public static Economy economy;
	public final static Logger logger = Logger.getLogger("Minecraft");

	public void onEnable() {
		this.saveDefaultConfig();
		time  = this.getConfig().getInt("time_in_market");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ClickManager(), this);
		
		if(Bukkit.getPluginManager().getPlugin("Vault") instanceof Vault) {
		    RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		 
		    if(service != null)
		        economy = service.getProvider();
		}
		if ( economy == null) {
			logger.info("[BlackMarket] Disabling plugin because Economy is not enabled");
			Bukkit.getPluginManager().disablePlugin(this);
		} else if (!economy.isEnabled()) {
			logger.info("[BlackMarket] Disabling plugin because Economy is not enabled");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			File file = new File(System.getProperty("user.dir") + "/plugins/BlackMarket/items.json");
			Gson gson = new Gson();
			if (file.exists()) {
				try {
					Type type = new TypeToken<List<LightItem>>(){}.getType();
					BufferedReader br = new BufferedReader(new FileReader(file));
					market = gson.fromJson(br, type);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					for (LightItem l : Main.market) {
						l.setTime(l.getTime() + 1);
					}
					for (LightItem li : Main.market) {
						if (li.getTime() > Main.time) {
							Main.market.remove(li);
							HashMap<Integer, ItemStack> result = Bukkit.getServer()
									.getPlayer(li.getPlayer()).getInventory()
									.addItem(li.toItemStack());
							if (!result.isEmpty()) {
								Player p = Bukkit.getServer().getPlayer(
										li.getPlayer());
								for (ItemStack its : result.values()) {
									p.getLocation().getWorld()
											.dropItem(p.getLocation(), its);
								}
							}
						}
					}
					if (temp != market) {
						for (LightPlayer p : Market.playersGui) {
							String s = p.getPlayer();
							int n = p.getInventory();
							Bukkit.getServer().getPlayer(s).closeInventory();
							Market.openGui(s, n);
							Market.playersGui.add(new LightPlayer(s, n));
						}
						for (LightPlayer lp : Market.playersMe.keySet()) {
							String s = lp.getPlayer();
							int n = lp.getInventory();
							Bukkit.getServer().getPlayer(s).closeInventory();
							Market.openMe(s, n);
						}
						for (LightPlayer lpl : Market.playersRemove.keySet()) {
							String s = lpl.getPlayer();
							Bukkit.getServer().getPlayer(s).closeInventory();
							Market.openSure(lpl);
						}
						temp = market;
					}
				}
			}, 0L, 20L);
		}
	}

	public void onDisable() {
		if (!market.isEmpty()) {
			Gson gson = new Gson();
			String json = gson.toJson(market);
			File carpet = new File(System.getProperty("user.dir") + "/plugins/BlackMarket");
			File file = new File(System.getProperty("user.dir") + "/plugins/BlackMarket/items.json");
			if (!carpet.exists()) {
				carpet.mkdir();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				writer.close();
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("market")
				|| label.equalsIgnoreCase("market")) {
			Player p = (Player) sender;
			if (p.hasPermission("market.open")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("sell")) {
						if (p.hasPermission("market.sell")) {
							try {
								int price = Integer.parseInt(args[1]);
								Material m = p.getItemInHand().getType();
								int c = p.getItemInHand().getAmount();
								market.add(new LightItem(m, price, c, 0, p
										.getName()));
								p.setItemInHand(null);
							} catch (Exception e) {
								p.sendMessage(RED + "You must specify a price");
							}
						} else {
							p.sendMessage(RED
									+ "You don't have permissions to use this command");
						}
					} else if (args[0].equalsIgnoreCase("me")) {
						Market.openMe(p.getName(), 0);
					} else {
						p.sendMessage(RED
								+ "Unknown option: /market sell or /market me");
					}
				} else {
					if (market.size() > 0) {
						Market.openGui(p.getName(), 0);
						Market.playersGui.add( new LightPlayer(p.getName(), 0));
					} else {
						p.sendMessage(RED + "Sorry, there's nothing to buy");
					}
				}
				return true;
			} else {
				p.sendMessage(RED
						+ "You don't have permissions to use this command");
			}
		}
		return false;
	}
}
