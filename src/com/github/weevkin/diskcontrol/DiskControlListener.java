package com.github.weevkin.diskcontrol;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
	public void onJukeboxUse(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Action a = event.getAction();
		p.sendMessage("fu");
		//if(a.equals(Action.RIGHT_CLICK_BLOCK)){
			p.sendMessage(" Interacted with:" + event.getEventName().toString());
		//}
		p.sendMessage(" Interacted with:" + event.getEventName().toString());
	}
}
