package net.draycia.lotteryplus;

import net.draycia.lotteryplus.interfaces.ILogger;
import org.bukkit.Bukkit;

public class BukkitLogger implements ILogger {

    @Override
    public void info(String message) {
        Bukkit.getServer().getLogger().info(message);
    }

    @Override
    public void severe(String message) {
        Bukkit.getServer().getLogger().severe(message);
    }

}
