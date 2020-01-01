package net.draycia.lotteryplus.datatypes.store;

import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.datatypes.PlayerTickets;
import net.draycia.lotteryplus.interfaces.IChatProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Abstract class implementation of Store
 */
public abstract class AbstractStore implements Store {

    private HashMap<UUID, PlayerTickets> playerTicketMap;
    private int maximumPurchaseAmount;
    private int ticketPrice;
    private ArrayList<PlayerTickets> lotteryTickets;

    private IChatProcessor chatProcessor = LotteryPlusCommon.getInstance().getChatProcessor();

    public AbstractStore(int maximumPurchaseAmount, int ticketPrice) {
        this.ticketPrice = ticketPrice;
        this.maximumPurchaseAmount = maximumPurchaseAmount;

        //Init collections/maps
        lotteryTickets = new ArrayList<>();
        playerTicketMap = new HashMap<>();
    }

    /**
     * Processes the purchase for the player
     *
     * @param uuid target player
     * @param amount desired purchase amount
     * @return true if purchase succeeds
     */
    @Override
    public boolean processPurchase(UUID uuid, int amount) {
        if (isPurchaseWithinLimits(uuid, amount)) {
            //Make sure they are not using negative numbers AND they have enough balance to purchase
            if (hasAvailableFunds(uuid, amount)) {
                completeTransaction(uuid, amount);
                return true;
            } else {
                //TODO: Message?
                chatProcessor.sendMessage(uuid, "&cInsufficient funds!");
                //Insufficient funds
            }
        }

        //Purchase fails
        return false;
    }

    /**
     * Complete the transaction for a player
     * This will charge the player, add to their tickets, and broadcast the purchase.
     *
     * @param uuid target player
     * @param amount purchase amount
     */
    private void completeTransaction(UUID uuid, int amount) {
        chargePlayer(uuid, amount);
        addTicketsToPlayer(uuid, amount);
        LotteryPlusCommon.getInstance().getLotteryManager().saveLottery();
    }

    /**
     * Charge player for the amount of tickets they have just purchased
     *
     * @param uuid target player
     * @param amount the quantity purchased
     */
    private void chargePlayer(UUID uuid, int amount) {
        LotteryPlusCommon.getInstance().getEconomy().withdraw(uuid, calculateCost(amount));
    }

    /**
     * Calculate the cost for the transaction
     *
     * @param quantity the purchase amount
     * @return the calculated cost for purchasing this quantity
     */
    @Override
    public double calculateCost(int quantity) {
        return ticketPrice * quantity;
    }

    /**
     * Checks to make sure the player has the correct amount of funds for this purchase
     * Also checks to make sure the purchase is not negative
     *
     * @param uuid player making the purchase
     * @param amount the requested purchase amount
     * @return true if player has funds
     */
    private boolean hasAvailableFunds(UUID uuid, int amount) {
        return LotteryPlusCommon.getInstance().getEconomy().hasAmount(uuid, calculateCost(amount));
    }

    /**
     * Gets the maximum volume of tickets a player can purchase for the lottery
     *
     * @return the max tickets purchasable per player
     */
    @Override
    public int getPlayerPurchaseCap() {
        return maximumPurchaseAmount;
    }

    /**
     * Determines whether or not a transaction from the Store is valid
     *
     * @param uuid                player making the purchase
     * @param purchaseRequestAmount the amount they wish to purchase
     * @return true if valid
     */
    @Override
    public boolean isPurchaseWithinLimits(UUID uuid, int purchaseRequestAmount) {
        PlayerTickets playerTickets = getTargetPlayerTickets(uuid);

        return (purchaseRequestAmount + playerTickets.getTicketPurchaseCount()) <= getPlayerPurchaseCap();
    }

    /**
     * Gets the current volume of tickets purchased, which is the sum of all tickets each player owns for the current lottery
     *
     * @return the volume of tickets purchased currently
     */
    @Override
    public int getTicketVolume() {
        return lotteryTickets.stream()
                .mapToInt(PlayerTickets::getTicketPurchaseCount)
                .sum();
    }

    /**
     * Set the lottery ticket state to be a carbon copy of another
     * Typically this is used to copy data from serialization
     *
     * @param tickets the tickets to copy
     */
    @Override
    public void copyTickets(ArrayList<PlayerTickets> tickets) {
        this.lotteryTickets = tickets;
        playerTicketMap.clear(); //Clear any existing values

        //Map values
        for (PlayerTickets playerTickets : lotteryTickets) {
            playerTicketMap.put(playerTickets.getUUID(), playerTickets);
        }
    }

    /**
     * Add the specified amount of tickets to a player
     *
     * @param amount the amount of tickets to add
     */
    @Override
    public void addTicketsToPlayer(UUID uuid, int amount) {
        PlayerTickets playerTickets;

        playerTickets = getTargetPlayerTickets(uuid);

        playerTickets.addTickets(amount);
        LotteryPlusCommon.getInstance().getMessages().broadcastPurchase(uuid, amount);

        String name = LotteryPlusCommon.getInstance().getPlayerUtils().getNameFromUUID(uuid);
        LotteryPlusCommon.getInstance().getLogger().info(name + " has purchased " + amount + " tickets.");
    }

    /**
     * Gets the PlayerTickets for this player
     *
     * @param uuid target player
     * @return PlayerTickets for this player
     * @see PlayerTickets
     */
    @Override
    public PlayerTickets getTargetPlayerTickets(UUID uuid) {
        if (playerTicketMap.get(uuid) != null) {
            return playerTicketMap.get(uuid);
        } else {
            PlayerTickets newTickets = new PlayerTickets(uuid);
            lotteryTickets.add(newTickets);
            playerTicketMap.put(uuid, newTickets);
            return playerTicketMap.get(uuid);
        }
    }

    /**
     * Get the amount of tickets a player currently owns for this lottery
     *
     * @param uuid target player
     * @return the amount of tickets this player has
     */
    @Override
    public int getPlayerTicketsAmount(UUID uuid) {
        return getTargetPlayerTickets(uuid).getTicketPurchaseCount();
    }

    /**
     * The maximum amount of tickets a player can purchase for this lottery
     *
     * @return the maximum amount of tickets a player can purchase for this lottery
     */
    @Override
    public int getTicketCap() {
        return maximumPurchaseAmount;
    }

    /**
     * Gets the tickets for the contest
     *
     * @return the PlayerTickets for the contest
     */
    @Override
    public ArrayList<PlayerTickets> getContestTickets() {
        return lotteryTickets;
    }

    /**
     * Gets the price of a single ticket
     *
     * @return the price of a single ticket
     */
    @Override
    public int getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Calculate the prize pools total cash value
     *
     * @return the total prize pool
     */
    @Override
    public double calculatePrizePool() {
        return 100 * getTicketVolume();
    }
}
