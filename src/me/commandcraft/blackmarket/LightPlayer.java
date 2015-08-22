package me.commandcraft.blackmarket;

public class LightPlayer {
	
	public String player;
	public int inventory;
	
	public static boolean isPlayerRemove(String s) {
		for (LightPlayer lp : Market.playersRemove.keySet()) {
			if (lp.getPlayer().equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static LightPlayer getPlayerRemove(String s) {
		for (LightPlayer lp : Market.playersRemove.keySet()) {
			if (lp.getPlayer().equals(s)) {
				return lp;
			}
		}
		return null;
	}
	
	public static LightPlayer getPlayerGui(String s) {
		for (LightPlayer lp : Market.playersGui) {
			if (lp.getPlayer().equals(s)) {
				return lp;
			}
		}
		return null;
	}
	
	public static LightPlayer getPlayerMe(String s) {
		for (LightPlayer lp : Market.playersMe.keySet()) {
			if (lp.getPlayer().equals(s)) {
				return lp;
			}
		}
		return null;
	}
	
	public static boolean isPlayerGui(String s) {
		for (LightPlayer lp : Market.playersGui) {
			if (lp.getPlayer().equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlayerMe(String s) {
		for (LightPlayer lp : Market.playersMe.keySet()) {
			if (lp.getPlayer().equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public LightPlayer(String player, int inventory) {
		super();
		this.player = player;
		this.inventory = inventory;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
}
