package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IPlayer;
import net.draycia.lotteryplus.abstraction.interfaces.IPlayerUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;

public class SpongePlayerUtils implements IPlayerUtils {

    UserStorageService userStorage = Sponge.getGame().getServiceManager()
            .getRegistration(UserStorageService.class).get().getProvider();

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        User user = userStorage.get(uuid).orElse(null);

        if (user == null) {
            return null;
        }

        return new SpongePlayer(user.getName(), user.getUniqueId(), user.isOnline());
    }

    @Override
    public IPlayer getPlayerByName(String name) {
        Player player = Sponge.getServer().getPlayer(name).orElse(null);

        return new SpongePlayer(player.getName(), player.getUniqueId(), player.isOnline());
    }

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
        Optional<Player> player = Sponge.getServer().getPlayer(uuid);

        return player.map(value -> value.hasPermission(permission)).orElse(false);
    }

    @Override
    public String getNameFromUUID(UUID uuid) {
        return null;
    }

}
