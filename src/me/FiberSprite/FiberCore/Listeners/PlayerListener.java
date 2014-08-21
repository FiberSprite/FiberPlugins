package me.FiberSprite.FiberCore.Listeners;

import me.FiberSprite.FiberCore.FiberCore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener
{
    
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
      Player player = event.getPlayer();
      if(player.hasPermission("FiberCore.ReciveUpdate") && player.isOp() && FiberCore.update)
      {
        player.sendMessage("An update is available: " + FiberCore.name + ", a " + FiberCore.type + " for " + FiberCore.version + " available at " + FiberCore.link);
        // Will look like - An update is available: AntiCheat v1.5.9, a release for CB 1.6.2-R0.1 available at http://media.curseforge.com/XYZ
        player.sendMessage("Type /fibercore update if you would like to automatically update.");
      }
    }
    
    
}
