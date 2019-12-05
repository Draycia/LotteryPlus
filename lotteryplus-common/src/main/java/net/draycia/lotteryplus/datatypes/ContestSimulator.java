package net.draycia.lotteryplus.datatypes;

import net.draycia.lotteryplus.LotteryPlusCommon;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * Simulates the results of the contest
 * In this context the contest is the random chance lottery
 */
public class ContestSimulator {
    private ArrayList<PlayerTickets> playerTickets;
    private ArrayList<PlayerTickets> contestants;
    private LinkedHashSet<PlayerTickets> winners;
    private int winnerLimit;
    private int minimumParticipants;

    public ContestSimulator(ArrayList<PlayerTickets> playerTickets, int winnerLimit, int minimumParticipants) {
        this.winners = null;
        this.playerTickets = playerTickets;
        this.winnerLimit = winnerLimit;
        this.minimumParticipants = minimumParticipants;

        //Process the winners
        fillContestants();
    }

    /**
     * Fills the contestants collection with one entry from a player per ticket
     */
    private void fillContestants() {
        contestants = new ArrayList<>();
        for (PlayerTickets tickets : playerTickets) {
            //Tickets can be 0, ignore those
            if (tickets.getTicketPurchaseCount() > 0) {
                for (int i = 0; i < tickets.getTicketPurchaseCount(); i++) {
                    //Add a contestant entry per ticket purchased
                    contestants.add(tickets);
                }
            }
        }

        //Print some info
        LotteryPlusCommon.getInstance().getLogger().info("Total contest entries: " + getContestEntryCount());
    }

    /**
     * Determines who the winners are and returns them in order
     * First entry in this collection would be first place, second entry would be 2nd place... and so on...
     *
     * @return the winners
     */
    private @Nullable LinkedHashSet<PlayerTickets> determineWinners() {
        //TODO: This is a bit messy..
        if (winners != null)
            return winners;

        Random random = new Random(System.currentTimeMillis()); //Use current time as seed
        LinkedHashSet<PlayerTickets> winningTickets = new LinkedHashSet<>();

        if (contestants.size() < minimumParticipants) {
            //Not enough entries return null
            return null;
        }

        //Bound the number of winners by whichever is smaller, the winner limit or the number of participants
        int numberOfWinners = Math.min(playerTickets.size(), winnerLimit);

        //TODO: This isn't efficient, but it only gets done once every 24 hours so..
        for (int i = 0; i < numberOfWinners; i++) {
            int winnerIndex = random.nextInt(getContestEntryCount());

            //Only add winners who haven't already won
            while (winningTickets.contains(contestants.get(winnerIndex))) {
                winnerIndex = random.nextInt(getContestEntryCount());
            }

            //Add the next winner
            winningTickets.add(contestants.get(winnerIndex));
        }

        winners = winningTickets;
        return winners;
    }

    /**
     * Gets the number of contest entries in this lottery
     *
     * @return the number of contest entries for this lottery
     */
    private int getContestEntryCount() {
        return contestants.size();
    }

    /**
     * Get the winners of this contest
     *
     * @return the winners
     */
    public LinkedHashSet<PlayerTickets> getWinners() {
        return determineWinners();
    }

    /**
     * Get the maximum number of winners allowed by this contest
     *
     * @return the maximum allowed number of winners
     */
    public int getWinnerLimit() {
        return winnerLimit;
    }
}
