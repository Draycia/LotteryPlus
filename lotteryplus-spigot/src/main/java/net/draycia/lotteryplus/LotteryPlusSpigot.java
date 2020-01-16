package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.LotteryServiceManager;
import net.draycia.lotteryplus.abstraction.interfaces.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LotteryPlusSpigot extends JavaPlugin implements Listener {

    private LotteryPlusCommon common;

    @Override
    public void onEnable() {
        this.common = new LotteryPlusCommon();

        LotteryServiceManager manager = common.getServiceManager();

        manager.register(IEconomy.class, new VaultEconomy());
        manager.register(IScheduler.class, new BukkitScheduler(this));
        manager.register(IChatProcessor.class, new BukkitChatProcessor());
        manager.register(IPlayerUtils.class, new BukkitPlayerUtils());
        manager.register(ILogger.class, new BukkitLogger());

        common.setup(getDataFolder());

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
