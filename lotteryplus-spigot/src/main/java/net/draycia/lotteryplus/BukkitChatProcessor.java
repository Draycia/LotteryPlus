package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IChatProcessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(color(message));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(color(message));
        }
    }

}
