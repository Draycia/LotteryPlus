package net.draycia.lotteryplus.messaging;

import net.draycia.lotteryplus.LotteryManager;
import net.draycia.lotteryplus.LotteryPlusCommon;
import net.draycia.lotteryplus.datatypes.LotteryType;
import net.draycia.lotteryplus.datatypes.store.Store;
import net.draycia.lotteryplus.interfaces.IChatProcessor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Helper class to handle messages for this plugin
 */
public class Messages {

    private LotteryPlusCommon plugin;
    private IChatProcessor chatProcessor;

    public static final String PREFIX = "&7[&3Lottery&7]";

    public Messages(LotteryPlusCommon plugin) {
        this.plugin = plugin;
        this.chatProcessor = plugin.getChatProcessor();
    }

    /**
     * Gets the player join message
     *
     * @return the player join message
     */
    public String getPlayerJoinMessage() {
        int volume = getTicketVolume();

        if (volume < 0) {
            return "There's no active lottery!";
        }

        System.out.println(volume);

        return PREFIX + " There are " + volume + " total tickets and a pot of " + formatToUSD(getPrizePool());
    }

    /**
     * Announce a new lottery starting
     *
     * @param lotteryType the type of lottery starting
     */
    public void broadcastNewLotteryStart(LotteryType lotteryType) {
        chatProcessor.broadcastMessage(PREFIX + " a new lottery has started! Pot: " + lotteryType.toString());
    }

    /**
     * The prize pool
     *
     * @return the prize pool
     */
    private double getPrizePool() {
        Store store = LotteryPlusCommon.getInstance().getLotteryManager().getStore();

        if (store == null) {
            return -1;
        }

        return store.calculatePrizePool();
    }

    /**
     * The volume of tickets
     *
     * @return the volume of tickets
     */
    private int getTicketVolume() {
        Store store = LotteryPlusCommon.getInstance().getLotteryManager().getStore();

        if (store == null) {
            return -1;
        }

        return store.getTicketVolume();
    }

    /**
     * Broadcasts the small pot winner
     */
    public void broadcastLowPotWinner(String playerName) {
        chatProcessor.broadcastMessage(PREFIX + " &e" + playerName + " &3has won the lottery!");
    }

    /**
     * Broadcasts the small pot winner
     *
     * @param playerName  players name
     * @param winnerIndex contest winning placement
     */
    public void broadcastHighPotWinner(String playerName, int winnerIndex) {
        String placementString = "has placed " + getPlacementString(winnerIndex) + " in the lottery!";

        chatProcessor.broadcastMessage(PREFIX + " &e" + playerName + " &3" + placementString);
    }

    /**
     * Returns a hardcoded string based on the placement of the winner
     *
     * @param winnerIndex contest winning placement index
     * @return hardcoded string based on the placement of the winner
     */
    private String getPlacementString(int winnerIndex) {
        switch (winnerIndex + 1) {
            case 1:
                return "first";
            case 2:
                return "second";
            case 3:
                return "third";
            default:
                return String.valueOf(winnerIndex);
            //TODO: Feel free to hardcode more LOL
        }
    }

    /**
     * Congratulate a player based on their position
     *
     * @param uuid target player UUID
     * @param winnings   their cash prize
     */
    public void congratulate(UUID uuid, double winnings) {
        //IPlayer player = plugin.getPlayerUtils().getPlayerByUUID(uuid);

        if (plugin.getPlayerUtils().playerIsOnline(uuid)) {
            chatProcessor.sendMessage(uuid,PREFIX + " &3You have received &e"
                    + formatToUSD(winnings) + " &3from the lottery!");
        }
    }

    /**
     * Broadcast the amount of tickets a player just purchased
     *
     * @param uuid target player
     * @param amount the amount they have just purchased
     */
    public void broadcastPurchase(UUID uuid, int amount) {
        String name = LotteryPlusCommon.getInstance().getPlayerUtils().getNameFromUUID(uuid);

        chatProcessor.broadcastMessage(PREFIX + " " + name + " has bought "
                + amount + " tickets. New total: "
                + formatToUSD(getPrizePool()));
    }

    /**
     * Converts a double into an en_US locale currency
     *
     * @param value value to convert to USD
     * @return the value converted to USD format
     */
    public String formatToUSD(double value) {
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        return numberFormat.format(value);
    }

    /**
     * Tell a player that they cannot purchase currently
     *
     * @param uuid target player
     */
    public void purchaseNotAllowedNoLotteryRunning(UUID uuid) {
        chatProcessor.sendMessage(uuid,PREFIX + " You cannot purchase lottery tickets at this time.");
    }

    /**
     * Prints the output of the status command to the sender
     *
     * @param uuid target sender, or null if console
     */
    public void printStatus(UUID uuid) {
        Store store = LotteryPlusCommon.getInstance().getLotteryManager().getStore();

        if (uuid != null) {
            chatProcessor.sendMessage(uuid, "&aYour tickets: &3" + store.getPlayerTicketsAmount(uuid));
        }

        chatProcessor.sendMessage(uuid, "&aTicket Limit: &3" + store.getTicketCap());
        chatProcessor.sendMessage(uuid, "&aTotal Tickets: &3" + store.getTicketVolume());
        chatProcessor.sendMessage(uuid, "&aTotal Pot: &3" +  formatToUSD(store.calculatePrizePool()));
        chatProcessor.sendMessage(uuid, "&aDraw In: &3" + LotteryPlusCommon.getInstance().getLotteryManager().getFormattedTimeLotteryEnds());
        chatProcessor.sendMessage(uuid, "&aLast Winner: &3" + getLastWinner());
    }

    /**
     * Gets the last winner, if wasn't one it will return 'No winner :('
     *
     * @return the last winner
     */
    public String getLastWinner() {
        String lastWinners = "No winner :(";

        LotteryManager manager = LotteryPlusCommon.getInstance().getLotteryManager();
        ArrayList<String> winners = manager.getPreviousLotteryWinners();

        if (winners != null && !winners.isEmpty()) {
            lastWinners = LotteryPlusCommon.getInstance().getLotteryManager().getPreviousLotteryWinners().get(0);
        }

        return lastWinners;
    }

}
