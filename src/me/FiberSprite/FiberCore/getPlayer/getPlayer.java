package me.FiberSprite.FiberCore.getPlayer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class getPlayer
{
    private final String uuid;
    
    public getPlayer(final Player player)
    {
        this.uuid = player.getUniqueId().toString();
    }
    
    
    public final String getUUID()
    {
        return uuid;
    }

    public final Player getPlayer()
    {
        final UUID uuid = UUID.fromString(this.uuid);
        return Bukkit.getPlayer(uuid);
        // for(final Player player : Bukkit.getOnlinePlayers())
        // {
        // if(player.getUniqueId().equals(uuid)) {
        // return player;
        // }
        // }
    }
}
