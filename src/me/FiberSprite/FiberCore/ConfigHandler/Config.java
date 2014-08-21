package me.FiberSprite.FiberCore.ConfigHandler;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
	public String name;
	public File file;
	public FileConfiguration fileConfig;
	public Config(String name)
	{
		this.name = name;
	}
}
