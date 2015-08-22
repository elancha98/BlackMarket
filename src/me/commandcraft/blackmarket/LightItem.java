package me.commandcraft.blackmarket;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LightItem {

	public Material material;
	public int price;
	public int cuantity;
	public int time;
	public String player;
	
	public ItemStack toItemStack() {
		return new ItemStack(this.material, this.cuantity);
	}
	
	public static LightItem toLightItem(ItemStack its, String player) {
		for (LightItem ls : Main.market) {
			if (ls.getCuantity() == its.getAmount() && ls.getMaterial() == its.getType() && ls.getPlayer().equalsIgnoreCase(player)) {
				return ls;
			}
		}
		return null;
	}
	
	public LightItem(Material material, int price, int cuantity, int time, String player) {
		super();
		this.material = material;
		this.price = price;
		this.cuantity = cuantity;
		this.time = time;
		this.player = player;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getCuantity() {
		return cuantity;
	}
	public void setCuantity(int cuantity) {
		this.cuantity = cuantity;
	}
	
	
}
