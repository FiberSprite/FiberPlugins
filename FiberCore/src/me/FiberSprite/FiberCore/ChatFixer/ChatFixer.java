package me.FiberSprite.FiberCore.ChatFixer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.FiberSprite.FiberCore.getPlayer.getPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

/**
 * To make work in main class put
 *
 *public static ChatFixer chatfixer;
 *
 *in onEnable put
 *
 *chatFixer = new ChatFixer(this);
 *
 */
public class ChatFixer implements Listener
{
    private ArrayList<String> players;

    private String censorURL = "§4*CENSORED LINK*§r";

    private Runnable onURLCensor;

    private boolean cancelOnURL = false;

    public ChatFixer(Plugin pl)
    {
        this(pl, new ArrayList<String>());
    }

    public ChatFixer(Plugin pl, ArrayList<String> players)
    {
        pl.getServer().getPluginManager().registerEvents(this, pl);
        this.players = players;
    }

    public ChatFixer(Plugin pl, String... players)
    {
        this(pl, (ArrayList<String>) Arrays.asList(players));
    }

    public void addPlayer(getPlayer p)
    {
        addPlayer(p.getUUID().toString());
    }

    public void addPlayer(String str)
    {
        players.add(str);
    }

    private String correctGrammar(String message)
    {
        String msg = message;
        // Fix grammar from fixer words
        HashMap<String, String> fix = fixerWords();
        for (String str : fix.keySet())
        {
            msg = msg.replaceAll(str, fix.get(str));
        }
        // Check for 'a' and 'an's
        String[] parts = msg.split(" ");
        for (int i = 0; i < (parts.length - 1); i++)
        {
            String str = parts[i];
            String after = parts[i + 1];
            if (str.equalsIgnoreCase("a"))
            {
                if (after.toLowerCase().startsWith("a")
                        || after.toLowerCase().startsWith("e")
                        || after.toLowerCase().startsWith("i")
                        || after.toLowerCase().startsWith("o")
                        || after.toLowerCase().startsWith("u"))
                {
                    str = "an";
                }
            }
            else
                if (str.equalsIgnoreCase("an"))
                {
                    if (!(after.toLowerCase().startsWith("a")
                            || after.toLowerCase().startsWith("e")
                            || after.toLowerCase().startsWith("i")
                            || after.toLowerCase().startsWith("o") || after
                            .toLowerCase().startsWith("u")))
                    {
                        str = "a";
                    }
                }
        }
        // Trim all end spaces
        msg = msg.trim();
        // Add dot if sentence does'nt have one or ends with smiley
        Matcher matcher = Pattern.compile(
                "[:).):)\\-.)(XD)(=.)(;.)(8=+D)(T_T)(^_^)(lol)?!\\.;]$")
                .matcher(msg);
        if (!matcher.find())
        {
            msg += ".";
        }
        // Uppercase the first letter in the sentence
        msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
        // return the fixed message
        return msg;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void fixChat(AsyncPlayerChatEvent e)
    {
        if (players.isEmpty())
        {
            return;
        }
        if (!players.contains(e.getPlayer().getName()))
        {
            return;
        }
        String message = e.getMessage();
        message = correctGrammar(message);
        if (getUrls(message).length > 0)
        {
            onURLCensor.run();
            if (cancelOnURL)
            {
                return;
            }
            message = replaceUrls(message);
        }
        e.setMessage(message);
    }

    private HashMap<String, String> fixerWords()
    {
        HashMap<String, String> fix = new HashMap<>();
        fix.put("\\byoure\\b", "you're");
        fix.put("\\bi\\b", "I");
        fix.put("your\\ba\\b", "you're a");
        fix.put("your\\ban\\b", "you're an");
        fix.put("\\bu\\b", "you");
        fix.put("\\by\\b", "why");
        fix.put("\\br\\b", "are");
        fix.put("\\bn\\b", "an");
        fix.put("\\bw8\\b", "wait");
        fix.put("\\bc\\b", "see");
        fix.put("\\bwut\\b", "what");
        fix.put("\\bwat\\b", "what");
        fix.put("\\bdoin\\b", "doing");
        fix.put("\\bdont\\b", "don't");
        fix.put("\\bisnt\\b", "isn't");
        fix.put("\\bcant\\b", "can't");
        fix.put("\\bits\\b", "it's");
        fix.put("\\bwont\\b", "won't");
        fix.put("\\bive\\b", "I've");
        fix.put("\\bhes\\b", "he's");
        fix.put("\\bshes\\b", "she's");
        fix.put("\\bits\\b", "it's");
        fix.put("\\bhed\\b", "he'd");
        fix.put("\\bshed\\b", "she'd");
        fix.put("\\bitd\\b", "it'd");
        fix.put("\\btheyre\\b", "they're");
        fix.put("\\barent\\b", "aren't");
        fix.put("\\bwhos\\b", "who's");
        fix.put("\\blets\\b", "let's");
        return fix;
    }

    private String[] getUrls(String message)
    {
        ArrayList<String> list = new ArrayList<>();
        String[] parts = message.split("\\s+");
        // Attempt to convert each item into an URL.
        for (String item : parts)
        {
            if (isUrl(item))
            {
                list.add(item);
            }
            else
                if (item.contains(".com") || item.contains(".tk")
                        || item.contains(".net") || item.contains(".org")
                        || item.contains(".gov") || item.contains(".info"))
                {
                    list.add(item);
                }
        }
        return list.toArray(new String[list.size()]);
    }

    private boolean isUrl(String str)
    {
        try
        {
            new URL(str);
            return true;
        }
        catch (MalformedURLException e)
        {
            return false;
        }
    }

    public void removePlayer(getPlayer p)
    {
        removePlayer(p.getUUID().toString());
    }

    public void removePlayer(String str)
    {
        players.add(str);
    }

    private String replaceUrls(String message)
    {
        String msg = message;
        for (String url : getUrls(msg))
        {
            msg.replaceAll(url, censorURL);
        }
        return msg;
    }

    public void setupURLFixer(boolean censorB, String censor, Runnable runnable)
    {
        this.cancelOnURL = censorB;
        this.censorURL = censorB ? null : censor;
        this.onURLCensor = runnable;
    }
}
