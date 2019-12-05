package net.draycia.lotteryplus;

import net.draycia.lotteryplus.interfaces.IChatProcessor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.UUID;

public class SpongeChatProcessor implements IChatProcessor {

    private Text color(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

    @Override
    public void broadcastMessage(String message) {
        Sponge.getServer().getBroadcastChannel().send(color(message));
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        Sponge.getServer().getPlayer(uuid).get().sendMessage(color(message));
    }

}
