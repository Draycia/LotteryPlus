package net.draycia.lotteryplus;

import net.draycia.lotteryplus.interfaces.IChatProcessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class BukkitChatProcessor implements IChatProcessor {

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void broadcastMessage(String message) {
        Bukkit.getServer().broadcastMessage(color(message));
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        if (uuid != null) {
            Bukkit.getPlayer(uuid).sendMessage(color(message));
        } else {
            Bukkit.getConsoleSender().sendMessage(color(message));
        }
    }

}
