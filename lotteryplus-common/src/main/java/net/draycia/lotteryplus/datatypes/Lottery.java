package net.draycia.lotteryplus.datatypes;

import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.datatypes.store.Store;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Interface Representation of a Lottery
 */
public interface Lottery {
    /**
     * Get the LotteryType for this Lottery
     *
     * @return the LotteryType for this Lottery
     * @see LotteryType
     */
    LotteryType getLotteryType();

    /**
     * Get the system time at which this Lottery was started
     *
     * @return system time in millis when the lottery was started
     */
    long getLotteryStartTimeStamp();

    /**
     * Get the system time at which this Lottery is scheduled to end
     *
     * @return system time in millis when the lottery should end
     */
    long getLotteryEndTimeStamp();

    /**
     * Gets the winner(s) from the previous lottery
     *
     * @return the winners from the previous lottery, can be null
     */
    ArrayList<String> getPreviousLotteryWinners();

    /**
     * Set the lottery begin timestamp in millis
     *
     * @param beginTime the new begin time in millis
     */
    void setLotteryBeginTime(long beginTime);

    /**
     * Set the lottery end timestamp in millis
     *
     * @param endTime the new end time in millis
     */
    void setLotteryEndTime(long endTime);

    /**
     * Set the previous lottery winners
     *
     * @param previousWinners the new previous lottery winners
     */
    void setLotteryPreviousWinners(ArrayList<String> previousWinners);

    /**
     * End the lottery and choose the winners
     */
    void endLottery();

    /**
     * Gets the phase of the lottery
     *
     * @return the phase of the lottery
     * @see LotteryPhase
     */
    LotteryPhase getLotteryPhase();

    /**
     * Changes the phase of the Lottery
     *
     * @param lotteryPhase the new phase
     */
    void setLotteryPhase(LotteryPhase lotteryPhase);

    /**
     * Makes a new lottery from persistent data
     *
     * @param persistentLotteryData the deserialized persistent lottery data
     * @return a new Lottery with state matching the persistent data
     */
    Lottery fromPersistentData(PersistentLotteryData persistentLotteryData);

    /**
     * Create a new lottery with memory of previous winners
     *
     * @param previousWinners previous winners
     * @return new lottery with previous winner data
     */
    Lottery fromPreviousWinners(ArrayList<String> previousWinners);

    /**
     * Choose the winners for the current Lottery
     *
     * @return the new winners
     */
    LinkedHashSet<PlayerTickets> chooseWinners();

    /**
     * Updates the lottery
     * If the lottery has reached the end time limit, it will begin the process of choosing winners
     * A new lottery will start afterwards
     */
    void updateLottery();

    /**
     * Gets the ticket store
     *
     * @return the ticket store
     */
    Store getStore();

    /**
     * Serialize the state for this lottery
     */
    void saveLottery();

    /**
     * Pay the winners
     *
     * @param winningTickets the winners
     */
    void cashoutWinners(LinkedHashSet<PlayerTickets> winningTickets);

    /**
     * Grabs the cash value of the entire prize pool for a given percentage
     *
     * @param percentage the percentage of the prize pool
     * @return the cash prize for a given percentage of the prize pool
     */
    double getPercentagePrizePoolCashValue(double percentage);

    /**
     * The minimum amount of participants required to make this Lottery legal
     *
     * @return the minimum amount of participants to make this lottery legal
     */
    int getMinimumEntrees();
}
