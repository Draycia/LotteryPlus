package net.draycia.lotteryplus.datatypes;

import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.datatypes.store.SimpleStore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * Implementation of a low pot Lottery
 *
 * @see Lottery
 */
public class LowPotLottery extends AbstractLottery {

    public LowPotLottery() {
        super(LotteryType.LOWPOT, new SimpleStore(100, 100));
    }

    /**
     * Makes a new lottery from persistent data
     *
     * @param persistentLotteryData the deserialized persistent lottery data
     * @return a new Lottery with state matching the persistent data
     */
    @Override
    public Lottery fromPersistentData(PersistentLotteryData persistentLotteryData) {
        //New instance
        LowPotLottery lowPotLottery = new LowPotLottery();

        //The cast to Long here is because the HOCON library has an issue in comparing Longs, this is fixed in dev builds but Configurate hasn't yet updated.
        lowPotLottery.setLotteryBeginTime(Long.parseLong(persistentLotteryData.getSerializedLotteryStartTime()));
        lowPotLottery.setLotteryEndTime(Long.parseLong(persistentLotteryData.getSerializedLotteryEndTime()));
        lowPotLottery.setLotteryPreviousWinners(persistentLotteryData.getSerializedPreviousWinners());
        lowPotLottery.getStore().copyTickets(persistentLotteryData.getSerializedLotteryTickets());
        lowPotLottery.setLotteryPhase(persistentLotteryData.getSerializedLotteryPhase());

        return lowPotLottery;
    }

    /**
     * Create a new lottery with memory of previous winners
     *
     * @param previousWinners previous winners
     * @return new lottery with previous winner data
     */
    @Override
    public Lottery fromPreviousWinners(ArrayList<String> previousWinners) {
        LowPotLottery lowPotLottery = new LowPotLottery();
        lowPotLottery.setLotteryPreviousWinners(previousWinners);
        return lowPotLottery;
    }

    /**
     * Choose the winners for the current Lottery
     *
     * @return the new winners
     */
    @Override
    public LinkedHashSet<PlayerTickets> chooseWinners() {
        ContestSimulator contestSimulator = new ContestSimulator(getStore().getContestTickets(), 1, getMinimumEntrees());
        return contestSimulator.getWinners();
    }

    /**
     * Pay the winners
     *
     * @param winningTickets the winners
     */
    @Override
    public void cashoutWinners(LinkedHashSet<PlayerTickets> winningTickets) {
        //Low Pot winner takes all
        PlayerTickets winningTicket = winningTickets.iterator().next();
        UUID uuid = winningTicket.getUUID();
        String name = LotteryPlusCommon.getInstance().getPlayerUtils().getNameFromUUID(uuid);

        LotteryPlusCommon.getInstance().getLogger().info("Paying " + name);
        LotteryPlusCommon.getInstance().getEconomy().deposit(uuid, getStore().calculatePrizePool());
        LotteryPlusCommon.getInstance().getMessages().congratulate(uuid, getStore().calculatePrizePool());
        LotteryPlusCommon.getInstance().getMessages().broadcastLowPotWinner(name);

        //Save name
        ArrayList<String> winnerName = new ArrayList<>();
        winnerName.add(name);
        setLotteryPreviousWinners(winnerName);
    }

    /**
     * The minimum amount of participants required to make this Lottery legal
     *
     * @return the minimum amount of participants to make this lottery legal
     */
    @Override
    public int getMinimumEntrees() {
        return 1;
    }
}
