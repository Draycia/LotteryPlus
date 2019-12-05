package net.draycia.lotteryplus.interfaces;

import java.util.UUID;

public interface IPlayer {

    public String getName();

    public UUID getUUID();

    public boolean isOnline();

}
