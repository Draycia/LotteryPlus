package net.draycia.lotteryplus;

/**
 * Misc util methods for the Lottery
 */
public class LotteryUtil {

    private static final int ONE_SECOND_IN_MILLIS = 1000;
    private static final int ONE_SECOND_TO_MINUTE_MULT = 60;
    private static final int MINUTE_IN_MILLIS = ONE_SECOND_IN_MILLIS * ONE_SECOND_TO_MINUTE_MULT;
    private static final int MINUTE_TO_HOUR_MULT = 60;
    private static final int HOUR_TO_DAY_MULT = 24;

    /**
     * Gets the timestamp of 24 hours into the future from current time in millis
     *
     * @return 24 hours later in millis
     */
    public static long get24HoursFromNowInMillis() {
        //DEBUG
//        return System.currentTimeMillis() + (1000 * 20);

        long currentTime = System.currentTimeMillis();
        long fullDay = (MINUTE_IN_MILLIS * MINUTE_TO_HOUR_MULT) * HOUR_TO_DAY_MULT;

        return currentTime + fullDay;
    }
}
