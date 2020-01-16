package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IPlayer;
import net.draycia.lotteryplus.abstraction.interfaces.IPlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerUtils implements IPlayerUtils {

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        return new BukkitPlayer(player.getName(), player.getUniqueId(), player.isOnline());
    }

    @Override
    public IPlayer getPlayerByName(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        return new BukkitPlayer(player.getName(), player.getUniqueId(), player.isOnline());
    }

    @Override
    public boolean playerIsOnline(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).isOnline();
    }

    @Override
    public boolean playerIsOnline(String name) {
        return Bukkit.getOfflinePlayer(name).isOnline();
    }

    @Override
    public boolean playerHasPermission(UUID uuid, String permission) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return false;
        }

        return player.hasPermission(permission);
    }

    @Override
    public String getNameFromUUID(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

}
