package net.draycia.lotteryplus.datatypes;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a collection of tickets belonging to a player
 */
public class PlayerTickets {
    private UUID uuid;
    private int ticketCount;

    /**
     * Represents a collection of purchased Tickets
     *
     * @param uuid player who owns these tickets
     */
    public PlayerTickets(@NonNull UUID uuid) {
        this.uuid = uuid;
        this.ticketCount = 0;
    }

    private PlayerTickets(@NonNull UUID uuid, int ticketCount) {
        this.uuid = uuid;
        this.ticketCount = ticketCount;
    }

    /**
     * Creates a PlayerTickets matching provided state from data
     *
     * @param uuid       uuid from data
     * @param amount     ticket amount from data
     * @return new PlayerTickets instance with state representing the provided data
     */
    public static PlayerTickets fromData(@NonNull UUID uuid, Integer amount) {
        return new PlayerTickets(uuid, amount);
    }

    /*
     * GETTER
     * SETTER
     * BOILER PLATE
     */

    /**
     * Add to the amount of tickets currently purchased
     *
     * @param addAmount the amount to add to the existing total
     */
    public void addTickets(int addAmount) {
        ticketCount += addAmount;
    }

    /**
     * Get the Ticket's owner's UUID
     *
     * @return owner's UUID
     */
    public @NonNull UUID getUUID() {
        return uuid;
    }

    /**
     * Get the amount of Tickets purchased
     *
     * @return the amount of Tickets purchased
     */
    public int getTicketPurchaseCount() {
        return ticketCount;
    }

    @Override
    public String toString() {
        return "PlayerTickets{" +
                "UUID=" + uuid +
                ", ticketCount=" + ticketCount +
                '}';
    }
}
