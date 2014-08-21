package me.FiberSprite.FiberCore;

import java.io.IOException;

import me.FiberSprite.FiberCore.Metrics.Metrics;
import me.FiberSprite.FiberCore.Updater.Updater;
import me.FiberSprite.FiberCore.Updater.Updater.ReleaseType;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FiberCore extends JavaPlugin
{
    public static boolean update = false; //If Updater Shows Downloading
    public static int ID = 1; //BukkitDev ID
    public static String name = "FiberCore"; //Name Of Plugin
    public static ReleaseType type = null;
    public static String version = "0.1"; //Current Plugin Verison
    public static String link = ""; //Link To BukkitDev Page
    
    //Updater updater = new Updater(this, ID, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
     
    public void onEnable()//On Plugin Enable
    {
      Updater updater = new Updater(this, ID, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
      update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
      name = updater.getLatestName(); // Get the latest name
      version = updater.getLatestGameVersion(); // Get the latest game version
      type = updater.getLatestType(); // Get the latest file's type
      link = updater.getLatestFileLink(); // Get the latest link
      
      try 
      {
          Metrics metrics = new Metrics(this);
          metrics.start();
      } catch (IOException e) 
      {
          // Failed to submit the stats :-(
          Bukkit.getServer().getLogger().info("Metrics Failed To Submit");
      }
      
      
      Bukkit.getServer().getLogger().info("FiberCore Started");
    }
    
    public void onDisable()//On Plugin Disable
    {
        
    }
     
 
    
}
