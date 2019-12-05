package net.draycia.lotteryplus.config;

import com.google.common.reflect.TypeToken;
import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.config.serialized.PlayerTicketsSerializer;
import net.draycia.lotteryplus.datatypes.Lottery;
import net.draycia.lotteryplus.datatypes.PlayerTickets;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

/**
 * Config Manager deals with the serialized data for us
 * Handles loading, saving, etc
 */
public class ConfigManager {

    //Lottery Persistent State
    private SerializedConfigLoader<PersistentLotteryData> persistentLotteryData;

    public ConfigManager() {
        registerCustomSerializers();
        initSerializedConfigs();
    }

    /**
     * Initialize the serialized configs / data
     */
    private void initSerializedConfigs() {
        //Deserialize the data
        //Configurate will initialize an empty config with default field parameters as needed
        LotteryPlusCommon.getInstance().getLogger().info("Deserializing data...");
        persistentLotteryData = new SerializedConfigLoader<>(PersistentLotteryData.class, "lotterydata", "LotteryState", null);
    }

    /**
     * Serialize current lottery state
     *
     * @param lottery target lottery
     */
    synchronized public void serializeLotteryData(final Lottery lottery) {
        //TODO: Remove debug
//        System.out.println("DEBUG: Updating lottery state...");
        persistentLotteryData.getConfig().updateState(lottery);
        persistentLotteryData.save();
    }

    /**
     * Register custom serializers
     */
    private void registerCustomSerializers() {
        LotteryPlusCommon.getInstance().getLogger().info("Registering custom Configurate serializers.");
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(PlayerTickets.class), new PlayerTicketsSerializer());
    }

    /**
     * Get the instance of the deserialized PersistentLotteryData class
     *
     * @return the deserialized lottery data
     */
    public PersistentLotteryData getPersistentLotteryData() {
        return persistentLotteryData.getConfig();
    }
}
