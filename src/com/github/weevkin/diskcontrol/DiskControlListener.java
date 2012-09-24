package com.github.weevkin.diskcontrol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class DiskControlListener implements Listener{
	private DiskControl plugin;
	
	public DiskControlListener() {
		
	}
	
	public DiskControlListener(DiskControl plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		List<ItemStack> i = event.getDrops();
		Player p = event.getEntity().getPlayer();
		
		for (Iterator<ItemStack> j = i.iterator(); j.hasNext(); ) {
			ItemStack s = j.next();
			if (s.getType() == Material.RECORD_9){
				Bukkit.broadcastMessage(p.getDisplayName()+ " dropped the record at X: "+p.getLocation().getBlockX()+" Y: "+p.getLocation().getBlockY()+" Z: "+p.getLocation().getBlockZ());
			}
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) throws SQLException{
		Player p = event.getPlayer();
		String query = "SELECT `id`, `player`, DATE_FORMAT(`date`, '%W') as `date`, DATE_FORMAT(`date`, '%l:%i %p') as `hm` FROM capture_info ORDER BY `id` desc LIMIT 1";
		ResultSet res = this.plugin.runSelect(query);
		
		if (res != null && res.next()) {
			String player = res.getString("player");
			String date = res.getString("date");
			String hm = res.getString("hm");
			
			p.sendMessage(player + " captured the disk on " + date + " at " + hm);
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event){
		Item i = event.getItem();
		Player p = event.getPlayer();
		String picked = i.getItemStack().getType().toString();
		if(picked == "RECORD_9"){
			Bukkit.broadcastMessage(p.getDisplayName()+ " picked up the Disk!");
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
	public void onJukeboxUse(PlayerInteractEvent event) throws SQLException{
		Player p = event.getPlayer();
		Action a = event.getAction();
		Block b = event.getClickedBlock();
		
		if(a.equals(Action.RIGHT_CLICK_BLOCK) && b.getType().toString().equals("JUKEBOX") && p.getItemInHand().getType().toString().equals("RECORD_9")){
			//Check to see if item placed as part of interaction was a RECORD_9
			p.getPlayer().getWorld().createExplosion(p.getLocation(), 0);
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
			event.getPlayer().getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0);
			p.getPlayer().getWorld().strikeLightningEffect(p.getPlayer().getTargetBlock(null, 200).getLocation());
			
			//Run query to see if last insert is the Same name as the person dropping disk off.
			String query = "SELECT `id`, `player` FROM capture_info ORDER BY `id` desc LIMIT 1";
			ResultSet res = this.plugin.runSelect(query);
			
			if (res != null && res.next()) {
				String playerdb = res.getString("player").toLowerCase();
				String player = p.getDisplayName();
				if(!(player.equals(playerdb))){
					query = "INSERT INTO capture_info(`player`, `date`) VALUES('"+p.getDisplayName()+"', NOW());";
					Bukkit.broadcastMessage(p.getDisplayName() + " has captured the Disk!");
					this.plugin.runInsert(query);
				}
				else{
					Bukkit.broadcastMessage(p.getDisplayName() + " has succesfully defended the Disk!");
				}
			}
		}
	}
}