package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.LotteryServiceManager;
import net.draycia.lotteryplus.abstraction.interfaces.*;
import net.draycia.lotteryplus.config.ConfigManager;
import net.draycia.lotteryplus.messaging.Messages;

import java.io.File;

public class LotteryPlusCommon {

    private File dataPath;
    private LotteryServiceManager serviceManager;

    private static LotteryPlusCommon instance;

    public LotteryPlusCommon() {
        // TODO: Don't singleton the class
        instance = this; //Plugin instance

        serviceManager = new LotteryServiceManager();
    }

    public void setup(File dataPath) {
        this.dataPath = dataPath;
        messages = new Messages(this);
        configManager = new ConfigManager(); //Load first
        lotteryManager = new LotteryManager(this); //Init manager

        //Start the automated lottery
        lotteryManager.startLottery();

        //Finished loading
        ILogger logger = serviceManager.getRegistration(ILogger.class);

        if (logger != null) {
            logger.info("LotteryPlus has loaded!");
        }
    }

    public void onShutdown() {
        lotteryManager.disable();
    }

    public LotteryServiceManager getServiceManager() {
        return serviceManager;
    }

    public IEconomy getEconomy() {
        return serviceManager.getRegistration(IEconomy.class);
    }

    public IScheduler getScheduler() {
        return serviceManager.getRegistration(IScheduler.class);
    }

    public IChatProcessor getChatProcessor() {
        return serviceManager.getRegistration(IChatProcessor.class);
    }

    public IPlayerUtils getPlayerUtils() {
        return serviceManager.getRegistration(IPlayerUtils.class);
    }

    public ILogger getLogger() {
        return serviceManager.getRegistration(ILogger.class);
    }

    /* LOTTERY MANAGER */
    private LotteryManager lotteryManager;
    private ConfigManager configManager;
    private Messages messages;

    /**
     * Gets the instance of this LotteryPlusCommon
     *
     * @return the instance of LotteryPlusCommon
     */
    public static LotteryPlusCommon getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LotteryManager getLotteryManager() {
        return lotteryManager;
    }

    public Messages getMessages() {
        return messages;
    }

    public File getDataPath() {
        return dataPath;
    }
}
