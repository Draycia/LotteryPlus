package net.draycia.lotteryplus;

import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.datatypes.Lottery;
import net.draycia.lotteryplus.datatypes.LotteryPhase;
import net.draycia.lotteryplus.datatypes.LotteryType;
import net.draycia.lotteryplus.datatypes.PlayerTickets;
import net.draycia.lotteryplus.datatypes.store.Store;
import net.draycia.lotteryplus.messaging.Messages;
import net.draycia.lotteryplus.runnables.LotteryUpdateTask;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Manages the lottery
 * Checks for updates, handles payouts, starts and stops lotteries
 */
public class LotteryManager {

    private LotteryPlusCommon plugin; //plugin ref for task scheduling
    private Lottery currentLottery; //the current running lottery

    private Messages messages;

    /**
     * @param plugin reference to our Plugin object in Bukkit
     */
    public LotteryManager(LotteryPlusCommon plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
    }

    /**
     * Starts the automated lotteries
     */
    public void startLottery() {
        initLottery();
    }

    /**
     * Initialize our lottery
     * This first checks for existing serialized state data for a lottery and if its not found it makes a new one
     */
    private void initLottery() {
        //Load lottery data from config or make new data if none found
        currentLottery = LotteryFactory.createNewLottery();
        currentLottery.saveLottery();

        messages.broadcastNewLotteryStart(currentLottery.getLotteryType());

        plugin.getScheduler().scheduleSyncDelayedTask(() -> currentLottery.updateLottery(), 0L);
    }

    /**
     * Schedule a non-repeating task to update the lottery
     */
    public void scheduleUpdate() {
        plugin.getScheduler().scheduleSyncDelayedTask(new LotteryUpdateTask(currentLottery), 200L);
    }

    /**
     * Start a new lottery
     */
    public void scheduleNewLottery() {
        plugin.getScheduler().scheduleSyncDelayedTask(this::initLottery, 20L);
    }

    /**
     * Handles the logic for when a player attempts to buy x number of tickets
     *
     * @param uuid target player
     * @param amount the amount they are requesting to buy
     */
    public void processPurchaseRequest(UUID uuid, int amount) {
        if (currentLottery.getLotteryPhase() == LotteryPhase.FINISHED) {
            //Don't allow purchases
            messages.purchaseNotAllowedNoLotteryRunning(uuid);
            return;
        }

        currentLottery.getStore().processPurchase(uuid, amount);
    }

    /**
     * Shuts down the lottery
     * Serializes any state information as it does
     */
    public void disable() {
        //Save lottery
        currentLottery.saveLottery();
    }

    /**
     * Gets the instance of the current Lottery
     *
     * @return the current lottery instance
     */
    public Lottery getCurrentLottery() {
        return currentLottery;
    }

    /**
     * Get the LotteryType for this Lottery
     *
     * @return the LotteryType for this Lottery
     * @see LotteryType
     */
    public LotteryType getLotteryType() {
        return getCurrentLottery().getLotteryType();
    }

    /**
     * Updates the lottery
     * If the lottery has reached the end time limit, it will begin the process of choosing winners
     * A new lottery will start afterwards
     */
    public void updateLottery() {
        getCurrentLottery().updateLottery();
    }

    /**
     * Get the time that the current lottery will end
     *
     * @return formatted time remaining for this lottery
     */
    public String getFormattedTimeLotteryEnds() {
        if (currentLottery.getLotteryPhase() == LotteryPhase.FINISHED || System.currentTimeMillis() > currentLottery.getLotteryEndTimeStamp())
            return "Lottery has ended!";

        long timeEnd = -(System.currentTimeMillis() - currentLottery.getLotteryEndTimeStamp());

        long hoursLeft = TimeUnit.MILLISECONDS.toHours(timeEnd);
        timeEnd -= TimeUnit.HOURS.toMillis(hoursLeft);
        long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeEnd);
        timeEnd -= TimeUnit.MINUTES.toMillis(minutesLeft);
        long secondsLeft = TimeUnit.MILLISECONDS.toSeconds(timeEnd);

        StringBuilder stringBuilder = new StringBuilder(64);

        stringBuilder.append(String.format("&3%02d", hoursLeft));
        stringBuilder.append("&bH&7, ");

        stringBuilder.append(String.format("&3%02d", minutesLeft));
        stringBuilder.append("&bM&7, ");

        stringBuilder.append(String.format("&3%02d", secondsLeft));
        stringBuilder.append("&bS");

        return stringBuilder.toString();
    }

    /*
     * DELEGATE BOILER PLATE
     * DELEGATE BOILER PLATE
     * DELEGATE BOILER PLATE
     * DELEGATE BOILER PLATE
     * DELEGATE BOILER PLATE
     * DELEGATE BOILER PLATE
     */

    /**
     * Get the system time at which this Lottery was started
     *
     * @return system time in millis when the lottery was started
     */
    public long getLotteryStartTimeStamp() {
        return getCurrentLottery().getLotteryStartTimeStamp();
    }

    /**
     * Get the system time at which this Lottery is scheduled to end
     *
     * @return system time in millis when the lottery should end
     */
    public long getLotteryEndTimeStamp() {
        return getCurrentLottery().getLotteryEndTimeStamp();
    }

    /**
     * Gets the winner(s) from the previous lottery
     *
     * @return the winners from the previous lottery, can be null
     */
    public ArrayList<String> getPreviousLotteryWinners() {
        return getCurrentLottery().getPreviousLotteryWinners();
    }

    /**
     * Set the lottery begin timestamp in millis
     *
     * @param beginTime the new begin time in millis
     */
    public void setLotteryBeginTime(long beginTime) {
        getCurrentLottery().setLotteryBeginTime(beginTime);
    }

    /**
     * Set the lottery end timestamp in millis
     *
     * @param endTime the new end time in millis
     */
    public void setLotteryEndTime(long endTime) {
        getCurrentLottery().setLotteryEndTime(endTime);
    }

    /**
     * Set the previous lottery winners
     *
     * @param previousWinners the new previous lottery winners
     */
    public void setLotteryPreviousWinners(ArrayList<String> previousWinners) {
        getCurrentLottery().setLotteryPreviousWinners(previousWinners);
    }

    /**
     * End the lottery and choose the winners
     */
    public void endLottery() {
        getCurrentLottery().endLottery();
    }

    /**
     * Changes the phase of the Lottery
     *
     * @param lotteryPhase the new phase
     */
    public void setLotteryPhase(LotteryPhase lotteryPhase) {
        getCurrentLottery().setLotteryPhase(lotteryPhase);
    }

    /**
     * Makes a new lottery from persistent data
     *
     * @param persistentLotteryData the deserialized persistent lottery data
     * @return a new Lottery with state matching the persistent data
     */
    public Lottery fromPersistentData(PersistentLotteryData persistentLotteryData) {
        return getCurrentLottery().fromPersistentData(persistentLotteryData);
    }

    /**
     * Choose the winners for the current Lottery
     *
     * @return the new winners
     */
    public LinkedHashSet<PlayerTickets> chooseWinners() {
        return getCurrentLottery().chooseWinners();
    }


    /**
     * Gets the ticket store
     *
     * @return the ticket store
     */
    public Store getStore() {
        return getCurrentLottery().getStore();
    }

    /**
     * Serialize the state for this lottery
     */
    public void saveLottery() {
        getCurrentLottery().saveLottery();
    }

    /**
     * Pay the winners
     *
     * @param winningTickets the winners
     */
    public void cashoutWinners(LinkedHashSet<PlayerTickets> winningTickets) {
        getCurrentLottery().cashoutWinners(winningTickets);
    }

    /**
     * Grabs the cash value of the entire prize pool for a given percentage
     *
     * @param percentage the percentage of the prize pool
     * @return the cash prize for a given percentage of the prize pool
     */
    public double getPercentagePrizePoolCashValue(double percentage) {
        return getCurrentLottery().getPercentagePrizePoolCashValue(percentage);
    }

}
