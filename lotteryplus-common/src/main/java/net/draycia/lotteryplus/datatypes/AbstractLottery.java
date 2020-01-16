package net.draycia.lotteryplus.datatypes;

import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.datatypes.store.Store;
import net.draycia.lotteryplus.abstraction.interfaces.IEconomy;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Abstract implementation of Lottery
 */
public abstract class AbstractLottery implements Lottery {

    private LotteryType lotteryType;
    private long lotteryStartTime;
    private long lotteryEndtime;
    private int maximumWinners;

    private ArrayList<String> previousWinners;
    private LotteryPhase lotteryPhase;
    private Store store;

    public AbstractLottery(LotteryType lotteryType, Store store, Long lotteryEndtime, int maximumWinners) {
        this.lotteryType = lotteryType;
        this.lotteryStartTime = System.currentTimeMillis();
        this.lotteryEndtime = lotteryEndtime;
        this.store = store;
        this.maximumWinners = maximumWinners;

        lotteryPhase = LotteryPhase.RUNNING;
    }

    /**
     * Get the maximum number of winners for this Lottery
     *
     * @return the maximum number of winners for this Lottery
     */
    public int getMaximumWinners() {
        return maximumWinners;
    }

    /**
     * Sets the maximum number of winners for this Lottery
     */
    public void setMaximumWinners(int maximumWinners) {
        this.maximumWinners = maximumWinners;
    }

    /**
     * Get the LotteryType for this Lottery
     *
     * @return the LotteryType for this Lottery
     * @see LotteryType
     */
    @Override
    public LotteryType getLotteryType() {
        return lotteryType;
    }

    /**
     * Get the system time at which this Lottery was started
     *
     * @return system time in millis when the lottery was started
     */
    @Override
    public long getLotteryStartTimeStamp() {
        return lotteryStartTime;
    }

    /**
     * Get the system time at which this Lottery is scheduled to end
     *
     * @return system time in millis when the lottery should end
     */
    @Override
    public long getLotteryEndTimeStamp() {
        return lotteryEndtime;
    }

    /**
     * Gets the winner(s) from the previous lottery
     *
     * @return the winners from the previous lottery, can be null
     */
    @Override
    public ArrayList<String> getPreviousLotteryWinners() {
        return previousWinners;
    }

    /**
     * Set the lottery begin timestamp in millis
     *
     * @param beginTime the new begin time in millis
     */
    @Override
    public void setLotteryBeginTime(long beginTime) {
        this.lotteryStartTime = beginTime;
    }

    /**
     * Set the lottery end timestamp in millis
     *
     * @param endTime the new end time in millis
     */
    @Override
    public void setLotteryEndTime(long endTime) {
        this.lotteryEndtime = endTime;
    }

    /**
     * Set the previous lottery winners
     *
     * @param previousWinners the new previous lottery winners
     */
    @Override
    public void setLotteryPreviousWinners(ArrayList<String> previousWinners) {
        this.previousWinners = previousWinners;
    }

    /**
     * End the lottery and choose the winners
     */
    @Override
    public void endLottery() {
        LotteryPlusCommon.getInstance().getLogger().info("Choosing winners for " + getLotteryType().toString());
        LinkedHashSet<PlayerTickets> winnerTickets = chooseWinners();

        if (winnerTickets != null) {
            if (winnerTickets.size() <= 0) {
                //No winners, start a new lottery
                LotteryPlusCommon.getInstance().getLogger().info("No winners, starting new lottery");
            } else {
                LotteryPlusCommon.getInstance().getLogger().info("Paying winners...");
                cashoutWinners(winnerTickets);
            }
        } else {
            LotteryPlusCommon.getInstance().getMessages().printLotteryRefund();

            IEconomy eco = LotteryPlusCommon.getInstance().getEconomy();

            for (PlayerTickets tickets : getStore().getContestTickets()) {
                eco.deposit(tickets.getUUID(), tickets.getTicketPurchaseCount() * getStore().getTicketPrice());
            }
        }

        setLotteryPhase(LotteryPhase.FINISHED); //This tells the manager to start a new lottery
        saveLottery();
        LotteryPlusCommon.getInstance().getLotteryManager().scheduleNewLottery();
    }

    /**
     * Updates the lottery
     * If the lottery has reached the end time limit, it will begin the process of choosing winners
     * A new lottery will start afterwards
     */
    @Override
    public void updateLottery() {
        if (System.currentTimeMillis() >= lotteryEndtime) {
            if (getLotteryPhase() == LotteryPhase.RUNNING) {
                LotteryPlusCommon.getInstance().getLogger().info("Lottery has reached its time limit! Ending the lottery.");
                endLottery();
            }
        } else if (getLotteryPhase() == LotteryPhase.FINISHED) {
            LotteryPlusCommon.getInstance().getLotteryManager().scheduleNewLottery();
        } else {
            LotteryPlusCommon.getInstance().getLotteryManager().scheduleUpdate();
        }

    }

    /**
     * Gets the ticket store
     *
     * @return the ticket store
     */
    @Override
    public Store getStore() {
        return store;
    }

    @Override
    public String toString() {
        return "AbstractLottery{" +
                "lotteryType=" + lotteryType +
                ", lotteryStartTime=" + lotteryStartTime +
                ", lotteryEndtime=" + lotteryEndtime +
                ", previousWinners=" + previousWinners +
                ", lotteryPhase=" + lotteryPhase +
                ", store=" + store +
                '}';
    }

    /**
     * Serialize the state for this lottery
     */
    @Override
    public void saveLottery() {
        LotteryPlusCommon.getInstance().getConfigManager().serializeLotteryData(this);
    }

    /**
     * Calculates the cash prize for a given percentage of the prize pool
     *
     * @param percentage values should be between 0.0 and 1.0
     * @return the cash prize for a given percentage of the prize pool
     */
    @Override
    public double getPercentagePrizePoolCashValue(double percentage) {
        if (percentage < 0.0D || percentage > 1.0D)
            LotteryPlusCommon.getInstance().getLogger().severe("Cash Value percentage is outside normal limits! - " + percentage);

        double cashPrize = getStore().calculatePrizePool();
        return cashPrize * percentage;
    }

    /**
     * Gets the phase of the lottery
     *
     * @return the phase of the lottery
     * @see LotteryPhase
     */
    @Override
    public LotteryPhase getLotteryPhase() {
        return lotteryPhase;
    }

    /**
     * Changes the phase of the Lottery
     *
     * @param lotteryPhase the new phase
     */
    @Override
    public void setLotteryPhase(LotteryPhase lotteryPhase) {
        LotteryPlusCommon.getInstance().getLogger().info("Lottery changing phase from " + this.lotteryPhase.toString()
                + " to " + lotteryPhase.toString());

        this.lotteryPhase = lotteryPhase;
        saveLottery();
    }
}
