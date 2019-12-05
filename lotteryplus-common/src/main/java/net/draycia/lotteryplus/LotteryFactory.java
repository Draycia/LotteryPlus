package net.draycia.lotteryplus;

import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.datatypes.HighPotLottery;
import net.draycia.lotteryplus.datatypes.Lottery;
import net.draycia.lotteryplus.datatypes.LotteryType;
import net.draycia.lotteryplus.datatypes.LowPotLottery;

import java.util.ArrayList;

/**
 * Handles creating Lottery objects
 */
public class LotteryFactory {

    /**
     * Makes a new lottery
     * First checks to see if a lottery is in memory, if it is then it goes to the next lottery
     * HIGH -> LOW -> HIGH -> LOW ... etc
     * If no lottery is in memory, it then attempts to read serialized data
     * If the serialized data is zero initialized, it ignores it and makes a lottery with no previous data
     * If the serialized data is not zero initialized, it makes a copy of a Lottery from that serialized data
     *
     * @return
     */
    public static Lottery createNewLottery() {
        /* IF A LOTTERY CURRENTLY IS LOADED */
        if (LotteryPlusCommon.getInstance().getLotteryManager().getCurrentLottery() != null
                && LotteryPlusCommon.getInstance().getLotteryManager().getCurrentLottery().getPreviousLotteryWinners() != null) {

            ArrayList<String> previousWinners = LotteryPlusCommon.getInstance().getLotteryManager().getCurrentLottery().getPreviousLotteryWinners();

            Lottery lottery;
            if (LotteryPlusCommon.getInstance().getLotteryManager().getCurrentLottery().getLotteryType() == LotteryType.LOWPOT) {
                lottery = new HighPotLottery().fromPreviousWinners(previousWinners);
            } else {
                lottery = new LowPotLottery().fromPreviousWinners(previousWinners);
            }

            lottery.setLotteryPreviousWinners(LotteryPlusCommon.getInstance().getLotteryManager().getCurrentLottery().getPreviousLotteryWinners());
            return lottery;
        }

        /* IF A LOTTERY IS NOT LOADED, READ SERIALIZED DATA ANF PROCEED */
        try {
            if (Long.parseLong(LotteryPlusCommon.getInstance().getConfigManager().getPersistentLotteryData().getSerializedLotteryEndTime()) != 0) {
                PersistentLotteryData persistentLotteryData = LotteryPlusCommon.getInstance().getConfigManager().getPersistentLotteryData();

                LotteryPlusCommon.getInstance().getLogger().info("Lottery Data was found, starting a lotter using prior data...");
                return createLotteryFromPersistentData(persistentLotteryData);

            } else {
                LotteryPlusCommon.getInstance().getLogger().info("Lottery Data wasn't found, starting a new lottery from scratch...");
                return createLotteryWithoutPreviousData();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return createLotteryWithoutPreviousData();
        }
    }

    /**
     * Creates a new lottery without any previous data, this will override any previous data if it exists.
     * New lotteries are always LowPotLottery in type
     *
     * @return a new Lottery with no prior data
     */
    private static LowPotLottery createLotteryWithoutPreviousData() {
        //Default lottery to create without persistent data is LOW POT lottery
        return new LowPotLottery();
    }

    /**
     * Creates a Lottery instance from our PersistentLotteryData
     *
     * @param persistentLotteryData our persistent data
     * @return Lottery instance with state built from our persistent data
     */
    private static Lottery createLotteryFromPersistentData(PersistentLotteryData persistentLotteryData) {
        LotteryType lotteryType = persistentLotteryData.getSerializedLotteryType();
        Lottery lottery;

        //Create lottery based on type
        if (lotteryType == LotteryType.LOWPOT)
            lottery = new LowPotLottery().fromPersistentData(persistentLotteryData);
        else
            lottery = new HighPotLottery().fromPersistentData(persistentLotteryData);

        return lottery;
    }
}
