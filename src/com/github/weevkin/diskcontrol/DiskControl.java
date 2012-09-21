package com.github.weevkin.diskcontrol;

import org.bukkit.plugin.java.JavaPlugin;

public class DiskControl extends JavaPlugin{
	@Override
    public void onEnable(){
		getServer().getPluginManager().registerEvents(new DiskControlListener(), this);
    }
}