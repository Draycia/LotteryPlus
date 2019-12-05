package net.draycia.lotteryplus.config.serialized;

import net.draycia.lotteryplus.datatypes.Lottery;
import net.draycia.lotteryplus.datatypes.LotteryPhase;
import net.draycia.lotteryplus.datatypes.LotteryType;
import net.draycia.lotteryplus.datatypes.PlayerTickets;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;

/**
 * Represents the serialized data for the Lottery
 */
@ConfigSerializable
public class PersistentLotteryData {

    @Setting(value = "Current-Lottery-Type")
    private LotteryType serializedLotteryType = LotteryType.LOWPOT;

    @Setting(value = "Lottery-PlayerTickets")
    private ArrayList<PlayerTickets> serializedLotteryTickets = new ArrayList<>();

    //This is a String because the Hocon library has an issue with comparing Longs in this version of Configurate
    //This should be fixed in future versions of Configurate, its just a mild inconvenience here
    @Setting("Lottery-End-Time")
    private String lotteryEndTime = "0";

    @Setting("Lottery-Start-Time")
    private String lotteryStartTime = "0";

    @Setting("Previous-Winners")
    private ArrayList<String> previousWinners = new ArrayList<>();

    @Setting("Lottery-Phase")
    private LotteryPhase lotteryPhase = LotteryPhase.RUNNING;

    /**
     * Get the serialized lottery type
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized lottery type
     */
    public LotteryType getSerializedLotteryType() {
        return serializedLotteryType;
    }

    /**
     * Get the serialized lottery tickets
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized lottery tickets
     */
    public ArrayList<PlayerTickets> getSerializedLotteryTickets() {
        return serializedLotteryTickets;
    }

    /**
     * Get the serialized lottery end time in millis
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized end time for the lottery
     */
    public String getSerializedLotteryEndTime() {
        return lotteryEndTime;
    }

    /**
     * Get the serialized lottery start time in millis
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized start time for the lottery
     */
    public String getSerializedLotteryStartTime() {
        return lotteryStartTime;
    }

    /**
     * Get the serialized previous winners
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized previous winners
     */
    public ArrayList<String> getSerializedPreviousWinners() {
        return previousWinners;
    }

    /**
     * Get the serialized lottery phase
     * This data is often out of date, if you need to grab fresh data grab it from the LotteryManager
     *
     * @return serialized lottery phase
     */
    public LotteryPhase getSerializedLotteryPhase() {
        return lotteryPhase;
    }

    /**
     * Updates the state of this config
     * This is done to serialize up to date information
     *
     * @param lottery lottery instance to copy
     */
    synchronized public void updateState(final Lottery lottery) {
        this.serializedLotteryType = lottery.getLotteryType();
        this.lotteryStartTime = String.valueOf(lottery.getLotteryStartTimeStamp());
        this.lotteryEndTime = String.valueOf(lottery.getLotteryEndTimeStamp());

        ArrayList previousWinnerList = new ArrayList();
        if (lottery.getPreviousLotteryWinners() != null) {
            previousWinnerList.addAll(lottery.getPreviousLotteryWinners());
        }

        this.previousWinners = previousWinnerList;

        if (lottery.getStore().getContestTickets() != null)
            this.serializedLotteryTickets = lottery.getStore().getContestTickets();
        else
            this.serializedLotteryTickets = new ArrayList<>();

        this.lotteryPhase = lottery.getLotteryPhase();
    }
}
