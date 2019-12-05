package net.draycia.lotteryplus;

import net.draycia.lotteryplus.interfaces.IPlayer;
import net.draycia.lotteryplus.interfaces.IPlayerUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class SpongePlayerUtils implements IPlayerUtils {
    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player player = Sponge.getServer().getPlayer(uuid).orElse(null);

        return new SpongePlayer(player.getName(), player.getUniqueId(), player.isOnline());
    }

    @Override
    public IPlayer getPlayerByName(String name) {
        Player player = Sponge.getServer().getPlayer(name).orElse(null);

        return new SpongePlayer(player.getName(), player.getUniqueId(), player.isOnline());    }

    @Override
    public boolean playerIsOnline(UUID uuid) {
        return Sponge.getServer().getPlayer(uuid).isPresent();
    }

    @Override
    public boolean playerIsOnline(String name) {
        return Sponge.getServer().getPlayer(name).isPresent();
    }

    @Override
    public boolean playerHasPermission(UUID uuid, String permission) {
        Player player = Sponge.getServer().getPlayer(uuid).orElse(null);

        return player.hasPermission(permission);
    }

    @Override
    public String getNameFromUUID(UUID uuid) {
        return null;
    }

}