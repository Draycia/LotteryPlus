package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IScheduler;
import org.bukkit.Bukkit;

public class BukkitScheduler implements IScheduler {

    private LotteryPlusSpigot plugin;

    public BukkitScheduler(LotteryPlusSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable runnable, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
    }

}
