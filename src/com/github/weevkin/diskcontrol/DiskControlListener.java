package com.github.weevkin.diskcontrol;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;


public class DiskControlListener implements Listener{

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Item i = event.getItem();
		Player p = event.getPlayer();
		String picked = i.getItemStack().getType().toString();
		if(picked == "RECORD_9"){
			Bukkit.broadcastMessage(p.getDisplayName() + " has picked up the record!");
			p.getPlayer().getWorld().strikeLightning(p.getPlayer().getTargetBlock(null, 200).getLocation());
			
		}
	}
	@EventHandler
	public void onJukeboxUse(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Action a = event.getAction();
		Block b = event.getClickedBlock();
		String inhand = p.getItemInHand().getType().toString();
		
		if(a.equals(Action.RIGHT_CLICK_BLOCK) && b.getType().equals(Material.JUKEBOX) && inhand.equals("RECORD_9")){
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0);
			event.getPlayer().getWorld().createExplosion(p.getLocation(), 0);
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
			Bukkit.broadcastMessage(p.getDisplayName() + " has placed the Disk into a jukebox!");
		}
	}
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		Item i = event.getItemDrop();
		
		String dropped = i.getItemStack().getType().toString();
		if(dropped.equals("RECORD_9")){
			Bukkit.broadcastMessage(p.getDisplayName() + " has dropped at X: " + i.getLocation().getBlockX() + " Y: " + i.getLocation().getBlockY() + " Z: " + i.getLocation().getBlockZ());
		}
	}
}
