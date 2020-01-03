package net.draycia.lotteryplus;

/**
 * Misc util methods for the Lottery
 */
public class LotteryUtil {

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int ONE_SECOND_TO_MINUTE_MULT = 60;
    private static final int MINUTE_IN_MILLIS = ONE_SECOND_IN_MILLIS * ONE_SECOND_TO_MINUTE_MULT;
    private static final int MINUTE_TO_HOUR_MULT = 60;

    /**
     * Gets the timestamp of X hours into the future from current time in millis
     *
     * @return X hours later in millis
     */
    public static long getTimeFromNowInMillis(long hours) {
        long currentTime = System.currentTimeMillis();
        long time = (MINUTE_IN_MILLIS * MINUTE_TO_HOUR_MULT) * hours;

        return currentTime + time;
    }
}
