package net.draycia.lotteryplus.runnables;

import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.datatypes.Lottery;

/**
 * Periodically updates the lottery and advances between stages of the lottery if conditions are met
 */
public class LotteryUpdateTask implements Runnable {

    private Lottery lottery; //Lottery ref

    public LotteryUpdateTask(Lottery lottery) {
        this.lottery = lottery;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LotteryPlusCommon.getInstance().getLotteryManager().updateLottery();
    }
}
