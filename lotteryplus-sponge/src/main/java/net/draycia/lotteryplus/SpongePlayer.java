package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IPlayer;

import java.util.UUID;

public class SpongePlayer implements IPlayer {

    private String name;
    private UUID uuid;
    private boolean isOnline;

    public SpongePlayer(String name, UUID uuid, boolean isOnline) {
        this.name = name;
        this.uuid = uuid;
        this.isOnline = isOnline;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }
}
