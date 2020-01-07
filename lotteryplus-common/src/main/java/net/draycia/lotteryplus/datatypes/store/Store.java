package net.draycia.lotteryplus.datatypes.store;

import net.draycia.lotteryplus.datatypes.PlayerTickets;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Store is an interface which defines behaviours related to purchasing
 * See the concrete classes for implementation
 */
public interface Store {

    /**
     * Processes the purchase for the player
     *
     * @param uuid target player
     * @param amount desired purchase amount
     * @return true if purchase succeeds
     */
    boolean processPurchase(UUID uuid, int amount);

    /**
     * Gets the maximum volume of tickets a player can purchase for the lottery
     *
     * @return the max tickets purchasable per player
     */
    int getPlayerPurchaseCap();

    /**
     * Gets the price of a single ticket
     *
     * @return the price of a single ticket
     */
    int getTicketPrice();

    /**
     * Determines whether or not a transaction from the Store is valid
     *
     * @param uuid player making the purchase
     * @param purchaseRequestAmount the amount they wish to purchase
     * @return true if valid
     */
    boolean isPurchaseWithinLimits(UUID uuid, int purchaseRequestAmount);

    /**
     * Gets the PlayerTickets for this player
     *
     * @param uuid target player
     * @return PlayerTickets for this player
     * @see PlayerTickets
     */
    PlayerTickets getTargetPlayerTickets(UUID uuid);

    /**
     * Gets the tickets for the contest
     *
     * @return the PlayerTickets for the contest
     */
    ArrayList<PlayerTickets> getContestTickets();

    /**
     * Get the amount of tickets a player currently owns for this lottery
     *
     * @param uuid target player
     * @return the amount of tickets this player has
     */
    int getPlayerTicketsAmount(UUID uuid);

    /**
     * Gets the current volume of tickets purchased, which is the sum of all tickets each player owns for the current lottery
     *
     * @return the volume of tickets purchased currently
     */
    int getTicketVolume();

    /**
     * The maximum amount of tickets a player can purchase for this lottery
     *
     * @return the maximum amount of tickets a player can purchase for this lottery
     */
    int getTicketCap();

    /**
     * The maximum amount of winners for this Lottery
     *
     * @return the maximum amount of winners for this Lottery
     */
    int getMaximumWinners();

    /**
     * Set the lottery ticket state to be a carbon copy of another
     * Typically this is used to copy data from serialization
     *
     * @param tickets the tickets to copy
     */
    void copyTickets(ArrayList<PlayerTickets> tickets);

    /**
     * Add the specified amount of tickets to a player
     *
     * @param uuid target player
     * @param amount the amount of tickets to add
     */
    void addTicketsToPlayer(UUID uuid, int amount);

    /**
     * Calculate the cost for the transaction
     *
     * @param quantity the purchase amount
     * @return the calculated cost for purchasing this quantity
     */
    double calculateCost(int quantity);

    /**
     * Calculate the prize pools total cash value
     *
     * @return the total prize pool
     */
    double calculatePrizePool();
}
