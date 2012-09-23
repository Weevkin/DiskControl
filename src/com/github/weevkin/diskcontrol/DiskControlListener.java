package com.github.weevkin.diskcontrol;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
	private DiskControl plugin;
	
	public DiskControlListener() {
		
	}
	
	public DiskControlListener(DiskControl plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Item i = event.getItem();
		Player p = event.getPlayer();
		String picked = i.getItemStack().getType().toString();
		if(picked == "RECORD_9"){
			Bukkit.broadcastMessage(p.getDisplayName()+ " picked up the record!");
			p.getPlayer().getWorld().strikeLightningEffect(p.getPlayer().getTargetBlock(null, 200).getLocation());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event){
		Item i = event.getItemDrop();
		Player p = event.getPlayer();
		
		String dropped = i.getItemStack().getType().toString();
		if(dropped.equals("RECORD_9")){
			Bukkit.broadcastMessage(p.getDisplayName()+ " dropped the record at X: "+i.getLocation().getBlockX()+" Y: "+i.getLocation().getBlockY()+" Z: "+i.getLocation().getBlockZ());
		}
	}
	
	@EventHandler
	public void onJukeboxUse(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Action a = event.getAction();
		Block b = event.getClickedBlock();
		
		if(a.equals(Action.RIGHT_CLICK_BLOCK) && b.getType().toString().equals("JUKEBOX") && p.getItemInHand().getType().toString().equals("RECORD_9")){
			//Check to see if item placed as part of interaction was a RECORD_9
			//World..getWorld().playEffect(p.getLocation(),"",1);
			p.getPlayer().getWorld().createExplosion(p.getLocation(), 0);
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0);
			Bukkit.broadcastMessage(p.getDisplayName() + " has placed the Disk into a Jukebox");
			
			String query = "INSERT INTO capture_info(`player`, `date`) VALUES('"+p.getDisplayName()+"', NOW());";
			ResultSet result = null;
			
			mySQL_Error(result);
		}
	}
}