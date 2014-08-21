package me.FiberSprite.FiberCore.PlayerInfo;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

//Now you can save each player informations in a map for example :
//final HashMap<String, PlayerInfo> infos = new HashMap<String, PlayerInfos>();
//
//final Player player = Bukkit.getPlayer("Skyost");
//infos.put(player, new PlayerInfos(player);

public class PlayerInfo
{
    private PlayerInventory inventory;
    private Location location;
    private int expLevel;
    private float exp;
    

    public PlayerInfo(final Player player)
    {
        this.inventory = player.getInventory();
        this.location = player.getLocation();
        this.expLevel = player.getLevel();
        this.exp = player.getExp();
    }


    public final void setInventory(final PlayerInventory inventory)
    {
        this.inventory = inventory;
    }

    public final PlayerInventory getInventory()
    {
        return inventory;
    }

    public final void setLocation(final Location location)
    {
        this.location = location;
    }

    public final Location getLocation()
    {
        return location;
    }

    public final void setExpLevel(final int expLevel)
    {
        this.expLevel = expLevel;
    }

    public final int getExpLevel()
    {
        return expLevel;
    }

    public final void setExp(final float exp)
    {
        this.exp = exp;
    }

    public final float getExp()
    {
        return exp;
    }

    @Override
    public final String toString()
    {
        final List<String> invContents = new ArrayList<String>();
        for (final ItemStack item : inventory.getContents())
        {
            if (item != null)
            {
                invContents.add(new JsonItemStack(item).toJson());
            }
        }
        final List<String> armorContents = new ArrayList<String>();
        for (final ItemStack item : inventory.getArmorContents())
        {
            if (item != null)
            {
                armorContents.add(new JsonItemStack(item).toJson());
            }
        }
        final JSONObject object = new JSONObject();
        object.put("inventory", invContents);
        object.put("locWorld", location.getWorld().getName());
        object.put("locX", location.getX());
        object.put("locY", location.getY());
        object.put("locZ", location.getZ());
        object.put("expLevel", expLevel);
        object.put("exp", exp);
        return object.toJSONString();
    }

    public static class JsonItemStack
    {

        private static Reader jsonItemStack;
        private final String material;
        private final String name;
        private final List<String> lore;
        private final HashMap<String, Long> enchantments = new HashMap<String, Long>();
        private final long amount;

        public JsonItemStack(final ItemStack item)
        {
            material = item.getType().name();
            final ItemMeta meta = item.getItemMeta();
            name = meta.getDisplayName();
            lore = meta.getLore();
            for (final Entry<Enchantment, Integer> entry : meta.getEnchants()
                    .entrySet())
            {
                enchantments.put(entry.getKey().getName(),
                        Long.valueOf(entry.getValue()));
            }
            this.amount = item.getAmount();
        }

        public JsonItemStack(final String material, final String name,
                final List<String> lore,
                final HashMap<String, Long> enchantments, final Long amount)
        {
            this.material = material;
            this.name = name;
            this.lore = lore;
            if (enchantments != null)
            {
                this.enchantments.putAll(enchantments);
            }
            this.amount = amount == null ? 1L : amount;
        }

        public final String toJson()
        {
            final JSONObject object = new JSONObject();
            object.put("material", material);
            object.put("name", name);
            object.put("lore", lore);
            object.put("enchantments", enchantments);
            object.put("amount", amount);
            return object.toJSONString();
        }

        public static final JsonItemStack fromJson()
        {
            final JSONObject array = (JSONObject) JSONValue
                    .parse(jsonItemStack);
            return new JsonItemStack((String) array.get("material"),
                    (String) array.get("name"),
                    (List<String>) array.get("lore"),
                    (HashMap<String, Long>) array.get("enchantments"),
                    (Long) array.get("amount"));
        }

        public final ItemStack toItemStack()
        {
            final ItemStack itemStack = new ItemStack(
                    material == null ? Material.GRASS
                            : Material.valueOf(material));
            final ItemMeta meta = itemStack.getItemMeta();
            boolean applyMeta = false;
            if (name != null)
            {
                meta.setDisplayName(name);
                applyMeta = true;
            }
            if (lore != null)
            {
                meta.setLore(lore);
                applyMeta = true;
            }
            if (enchantments.size() != 0)
            {
                for (final Entry<String, Long> entry : enchantments.entrySet())
                {
                    meta.addEnchant(Enchantment.getByName(entry.getKey()),
                            Ints.checkedCast(entry.getValue()), true);
                }
                applyMeta = true;
            }
            itemStack.setItemMeta(meta);
            if (applyMeta)
            {
                itemStack.setAmount(Ints.checkedCast(amount));
            }
            return itemStack;
        }

    }

}


