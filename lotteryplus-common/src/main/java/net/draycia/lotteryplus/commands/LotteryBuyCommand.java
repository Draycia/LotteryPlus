package net.draycia.lotteryplus.commands;

import net.draycia.lotteryplus.LotteryPlusCommon;

import java.util.UUID;

public class LotteryBuyCommand {

    private static final String INVALID_PURCHASE = "&cNot a valid purchase amount!";

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param uuid  Source of the command
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public boolean onCommand(UUID uuid, String[] args) {
        int purchaseAmount;
        if (args.length == 1) {
            //Buy 1 ticket
            purchaseAmount = 1;
        } else {
            try {
                Integer buyAmount = Integer.valueOf(args[1]);

                if (buyAmount <= 0) {
                    LotteryPlusCommon.getInstance().getChatProcessor().sendMessage(uuid, INVALID_PURCHASE);
                    return false;
                }

                purchaseAmount = buyAmount;

            } catch (NumberFormatException e) {
                LotteryPlusCommon.getInstance().getChatProcessor().sendMessage(uuid, INVALID_PURCHASE);
                return false;
            }
        }

        LotteryPlusCommon.getInstance().getLotteryManager().processPurchaseRequest(uuid, purchaseAmount);
        return true;
    }
}
