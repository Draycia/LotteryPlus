package net.draycia.lotteryplus.datatypes;

import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.LotteryUtil;
import net.draycia.lotteryplus.config.serialized.PersistentLotteryData;
import net.draycia.lotteryplus.datatypes.store.SimpleStore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * Implementation of a high pot Lottery
 * <p>
 * High pot lottery follows these rules
 * Prize pool: The prize pool is equal to the total number of tickets * 100
 * Cap: 1000 ticket cap per player
 * Prizepool distribution:
 * 3 Winners
 * 1st Place: 70%
 * 2nd Place: 20%
 * 3rd Place: 10%
 *
 * @see Lottery
 */
public class HighPotLottery extends AbstractLottery {

    public HighPotLottery() {
        super(LotteryType.HIGHPOT,
                new SimpleStore(1000, 100),
                LotteryUtil.getTimeFromNowInMillis(24));
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
        HighPotLottery highPotLottery = new HighPotLottery();

        //The cast to Long here is because the HOCON library has an issue in comparing Longs, this is fixed in dev builds but Configurate hasn't yet updated.
        highPotLottery.setLotteryBeginTime(Long.parseLong(persistentLotteryData.getSerializedLotteryStartTime()));
        highPotLottery.setLotteryEndTime(Long.parseLong(persistentLotteryData.getSerializedLotteryEndTime()));
        highPotLottery.setLotteryPreviousWinners(persistentLotteryData.getSerializedPreviousWinners());
        highPotLottery.getStore().copyTickets(persistentLotteryData.getSerializedLotteryTickets());
        highPotLottery.setLotteryPhase(persistentLotteryData.getSerializedLotteryPhase());

        return highPotLottery;
    }

    /**
     * Create a new lottery with memory of previous winners
     *
     * @param previousWinners previous winners
     * @return new lottery with previous winner data
     */
    @Override
    public Lottery fromPreviousWinners(ArrayList<String> previousWinners) {
        HighPotLottery highPotLottery = new HighPotLottery();
        highPotLottery.setLotteryPreviousWinners(previousWinners);
        return highPotLottery;
    }

    /**
     * Choose the winners for the current Lottery
     *
     * @return the new winners
     */
    @Override
    public LinkedHashSet<PlayerTickets> chooseWinners() {
        ContestSimulator contestSimulator = new ContestSimulator(getStore().getContestTickets(), 3, getMinimumEntrees());
        return contestSimulator.getWinners();
    }

    /**
     * Pay the winners
     *
     * @param winningTickets the winners
     */
    @Override
    public void cashoutWinners(LinkedHashSet<PlayerTickets> winningTickets) {

        ArrayList<PlayerTickets> winnerList = new ArrayList<>(winningTickets);
        ArrayList<String> winnerNames = new ArrayList<>();

        for (int i = 0; i < winnerList.size(); i++) {
            PlayerTickets winningTicket = winnerList.get(i);
            UUID uuid = winningTicket.getUUID();
            String name = LotteryPlusCommon.getInstance().getPlayerUtils().getNameFromUUID(uuid);
            double winnings = getPrize(i, winnerList.size());

            LotteryPlusCommon.getInstance().getLogger().info("Paying " + name);
            LotteryPlusCommon.getInstance().getEconomy().deposit(uuid, winnings);
            LotteryPlusCommon.getInstance().getMessages().congratulate(uuid, winnings);
            LotteryPlusCommon.getInstance().getMessages().broadcastHighPotWinner(name, i);

            winnerNames.add(name + "&7, &3$" + Double.toString(winningTicket.getTicketPurchaseCount()));
        }

        //Save name
        setLotteryPreviousWinners(winnerNames);
    }

    /**
     * Get the number of player participants in the lottery
     *
     * @return the number of player participants in the lottery
     */
    private int getNumberOfParticipants() {
        return getStore().getContestTickets().size();
    }

    /**
     * Get 3rd place prize
     *
     * @return 3rd place prize value
     */
    private double getThirdPlacePrize() {
        return getPercentagePrizePoolCashValue(0.1D);
    }

    /**
     * Get 2nd place prize
     *
     * @return 2nd place prize value
     */
    private double getSecondPlacePrize(int participantCount) {
        if (participantCount == 2)
            return getPercentagePrizePoolCashValue(0.5D);
        else
            return getPercentagePrizePoolCashValue(0.2D);
    }

    /**
     * Get 1st place prize
     *
     * @return 1st place prize value
     */
    private double getFirstPlacePrize(int participantCount) {
        if (participantCount == 1 || participantCount == 2)
            return getPercentagePrizePoolCashValue(1.0D / participantCount);
        else
            return getPercentagePrizePoolCashValue(0.7D);
    }

    /**
     * Get the prize money based on the winners placement in the contest and the number of participants
     *
     * @param winnerIndex      0 = 1st, 1 = 2nd, etc...
     * @param participantCount number of participants in this contest, which changes 1st and 2nd place rewards
     * @return the cash prize for this winner
     */
    private double getPrize(int winnerIndex, int participantCount) {
        switch (winnerIndex) {
            case 0:
                return getFirstPlacePrize(participantCount);
            case 1:
                return getSecondPlacePrize(participantCount);
            default:
                return getThirdPlacePrize();
        }
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
