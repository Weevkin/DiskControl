package com.github.weevkin.diskcontrol;

import org.bukkit.Server;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;


public class DiskControlListener implements Listener{

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Item i = event.getItem();
		Player p = event.getPlayer();
		String picked = i.getItemStack().getType().toString();
		if(picked == "RECORD_9"){
			p.sendMessage(p.getDisplayName()+ " picked up the record!");
			p.getPlayer().getWorld().strikeLightning(p.getPlayer().getTargetBlock(null, 200).getLocation());
			
		}
		else{
			p.sendMessage(i.getItemStack().getType().toString().toLowerCase());
			p.sendMessage("no biggy, just a " + i.getItemStack().getType().toString().toLowerCase().replace("_", " "));
		}
		
		//p.sendMessage(p.getDisplayName()+ " picked up a(n) " + i.getItemStack().getType().toString().toLowerCase().replace("_", " "));
	}
	/*public void onBlockPlace(BlockPlaceEvent event){
		Player p = event.getPlayer();
		Block bp = event.getBlockPlaced();
		
		p.sendMessage("You placed a " + bp.getType().toString());
	}*/
}
