package net.draycia.lotteryplus.interfaces;

import java.util.UUID;

public interface IPlayerUtils {

    public IPlayer getPlayerByUUID(UUID uuid);

    public IPlayer getPlayerByName(String name);

    public boolean playerIsOnline(UUID uuid);

    public boolean playerIsOnline(String name);

    public boolean playerHasPermission(UUID uuid, String permission);

    public String getNameFromUUID(UUID uuid);

}
