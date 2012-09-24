package com.github.weevkin.diskcontrol;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DiskControl extends JavaPlugin{
	public String logPrefix = "[DiskControl] "; // Prefix to go in front of all log entries
	public Logger log = Logger.getLogger("Minecraft"); // Minecraft log and console
	public File pFolder = new File("plugins" + File.separator + "DiskControl"); // Folder to store plugin settings file and database
	
	public MySQL mysql;
	
	public boolean MySQL = true;
	public String dbHost = "localhost";
	public String dbPort = "3306";
	public String dbUser = "root";
	public String dbPass = "";
	public String dbDatabase = "diskcontrol";
	
	@Override
    public void onEnable(){
		getServer().getPluginManager().registerEvents(new DiskControlListener(this), this);
		//SQL Stuff
		if (this.MySQL) {
			if (this.dbHost.equals(null)) {
				this.MySQL = false;
				this.log.severe(this.logPrefix + "MySQL is on, but host is not defined, defaulting to SQLite");
			}
			if (this.dbUser.equals(null)) {
				this.MySQL = false;
				this.log.severe(this.logPrefix + "MySQL is on, but username is not defined, defaulting to SQLite");
			}
			if (this.dbPass.equals(null)) {
				this.MySQL = false;
				this.log.severe(this.logPrefix + "MySQL is on, but password is not defined, defaulting to SQLite");
			}
			if (this.dbDatabase.equals(null)) {
				this.MySQL = false;
				this.log.severe(this.logPrefix + "MySQL is on, but database is not defined, defaulting to SQLite");
			}
		
			// Declare MySQL Handler
			this.mysql = new MySQL(this.log, this.logPrefix, this.dbHost, this.dbPort, this.dbDatabase, this.dbUser, this.dbPass);
			this.log.info(this.logPrefix + "MySQL Initializing");
			// Initialize MySQL Handler
			try {
				this.mysql.open();
			} catch (Exception e) {
				Bukkit.broadcastMessage(e.getMessage());
			}
			
			//try {
				if (this.mysql.checkConnection()) { // Check if the Connection was successful
					this.log.info(this.logPrefix + "MySQL connection successful");
					if (!this.mysql.checkTable("blocks")) { // Check if the table exists in the database if not create it
						this.log.info(this.logPrefix + "Creating table blocks");
						String query = "CREATE TABLE blocks (id INT, owner VARCHAR(255), x INT, y INT, z INT);";
						this.mysql.createTable(query);
					}
				} else {
					this.log.severe(this.logPrefix + "MySQL connection failed");
					this.MySQL = false;
				}
			/*} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		else{
			Bukkit.broadcastMessage("MYSQL Failed to load.");
			Bukkit.broadcastMessage(this.logPrefix+" / "+this.dbHost+" / "+this.dbPort+" / "+this.dbDatabase+" / "+this.dbUser+" / "+this.dbPass);
		}
		
		// Register Listeners \\
		//PluginManager pm = this.getServer().getPluginManager();
		//pm.registerEvent(, new OwnerPlayerListener(this), EventPriority.NORMAL, new SQLEventExecutor(), this);
		
		this.log.info(this.logPrefix + "Owner is finished initializing");
    }
	
	public ResultSet runInsert(String query){
		ResultSet result = null;
		if (this.MySQL) {
			try {
				result = this.mysql.query(query);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public ResultSet runSelect(String query){
		ResultSet resultSet = null;
		if (this.MySQL) {
			try {
				resultSet = this.mysql.query(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultSet;
	}
}