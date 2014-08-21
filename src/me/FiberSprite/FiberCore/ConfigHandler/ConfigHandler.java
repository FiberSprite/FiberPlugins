package me.FiberSprite.FiberCore.ConfigHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.FiberSprite.FiberCore.FiberCore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler
{
    FiberCore plugin;

    public ConfigHandler(FiberCore instance)
    {
        plugin = instance;
    }

    public FileConfiguration getConfigHandler(Config config)
    {
        if (config.fileConfig == null)
        {
            reloadConfigHandler(config);
        }
        return config.fileConfig;
    }

    public void reloadConfigHandler(Config config)
    {
        if (config.fileConfig == null)
        {
            config.file = new File(plugin.getDataFolder(), config.name + ".yml");
        }
        config.fileConfig = YamlConfiguration.loadConfiguration(config.file);

        InputStream defConfigStream = plugin.getResource(config.name + ".yml");
        if (defConfigStream != null)
        {
            @SuppressWarnings("deprecation")
            YamlConfiguration defConfig = YamlConfiguration
                    .loadConfiguration(defConfigStream);
            config.fileConfig.setDefaults(defConfig);
        }
    }

    public void saveConfigHandler(Config config)
    {
        if ((config.fileConfig == null) || (config.file == null))
        {
            return;
        }
        try
        {
            getConfigHandler(config).save(config.file);
        }
        catch (IOException ex)
        {
            plugin.getLogger().log(Level.SEVERE,
                    "Could not save config to " + config.file, ex);
        }
    }

    public void saveDefaultConfig(Config config)
    {
        if (config.file == null)
        {
            config.file = new File(plugin.getDataFolder(), config.name + ".yml");
        }
        if (!config.file.exists())
        {
            plugin.saveResource(config.name + ".yml", false);
        }
    }
}
