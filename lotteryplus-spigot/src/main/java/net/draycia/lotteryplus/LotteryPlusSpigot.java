package net.draycia.lotteryplus;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LotteryPlusSpigot extends JavaPlugin implements Listener {

    private LotteryPlusCommon common;

    @Override
    public void onEnable() {
        this.common = new LotteryPlusCommon(
                new VaultEconomy(),
                new BukkitScheduler(this),
                new BukkitChatProcessor(),
                new BukkitPlayerUtils(),
                new BukkitLogger(),
                getDataFolder());

        getServer().getPluginManager().registerEvents(this, this);

        getCommand("lottery").setExecutor(new BukkitLotteryCommand());
    }

    @Override
    public void onDisable() {
        common.onShutdown();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', common.getMessages().getPlayerJoinMessage()));
    }

}
