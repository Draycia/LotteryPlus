package net.draycia.lotteryplus.interfaces;

import java.util.UUID;

public interface IChatProcessor {

    public void broadcastMessage(String message);

    public void sendMessage(UUID uuid, String message);

}
