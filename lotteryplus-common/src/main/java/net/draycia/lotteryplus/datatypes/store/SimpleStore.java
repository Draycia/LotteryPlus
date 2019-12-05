package net.draycia.lotteryplus.datatypes.store;

/**
 * Concrete implementation of AbstractStore
 */
public class SimpleStore extends AbstractStore {
    public SimpleStore(int maximumPurchaseAmount, int ticketPrice) {
        super(maximumPurchaseAmount, ticketPrice);
    }
}
