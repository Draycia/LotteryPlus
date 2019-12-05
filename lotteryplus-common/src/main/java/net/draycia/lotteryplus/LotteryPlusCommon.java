package net.draycia.lotteryplus;

import net.draycia.lotteryplus.config.ConfigManager;
import net.draycia.lotteryplus.interfaces.*;
import net.draycia.lotteryplus.messaging.Messages;

import java.io.File;
import java.util.logging.Logger;

public class LotteryPlusCommon {

    private IEconomy economy;
    private IScheduler scheduler;
    private IChatProcessor chatProcessor;
    private IPlayerUtils playerUtils;
    private ILogger logger;

    private File dataPath;

    private static LotteryPlusCommon instance;

    public LotteryPlusCommon(IEconomy economy, IScheduler scheduler, IChatProcessor chatProcessor,
                             IPlayerUtils playerUtils, ILogger logger, File dataPath) {

        // TODO: Better way to pass in all of these values
        this.economy = economy;
        this.scheduler = scheduler;
        this.chatProcessor = chatProcessor;
        this.playerUtils = playerUtils;
        this.logger = logger;
        this.dataPath = dataPath;

        instance = this; //Plugin instance

        messages = new Messages(this);
        configManager = new ConfigManager(); //Load first
        lotteryManager = new LotteryManager(this); //Init manager

        //Start the automated lottery

        //scheduler.scheduleSyncDelayedTask(() -> lotteryManager.startLottery(), 600L);
        lotteryManager.startLottery();

        // TODO: Fix these
        //Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        //this.getCommand("lottery").setExecutor(new LotteryCommand());

        //Finished loading
        logger.info("LotteryPlus has loaded!");
    }

    public void onShutdown() {
        lotteryManager.disable();
    }

    public IEconomy getEconomy() {
        return economy;
    }

    public IScheduler getScheduler() {
        return scheduler;
    }

    public IChatProcessor getChatProcessor() {
        return chatProcessor;
    }

    public IPlayerUtils getPlayerUtils() {
        return playerUtils;
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

    public ILogger getLogger() {
        return logger;
    }

    public File getDataPath() {
        return dataPath;
    }
}
