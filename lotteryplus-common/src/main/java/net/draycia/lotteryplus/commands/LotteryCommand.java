package net.draycia.lotteryplus.commands;

import net.draycia.lotteryplus.permissions.Permissions;

import java.util.UUID;

/**
 * The parent class for the /lottery command
 */
public class LotteryCommand {

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param uuid Source of the command
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public boolean onCommand(UUID uuid, String[] args) {

        if (args.length <= 0) {
            return false;
        }

        //Buy Command
        if (args[0].toLowerCase().equals("buy")) {
            if (!Permissions.hasBuyPermission(uuid)) {
                return true;
            }

            return new LotteryBuyCommand().onCommand(uuid, args);
        }

        //Status Command
        if (args[0].toLowerCase().equals("status")) {
            if (!Permissions.hasStatusPermission(uuid))
                return true;

            return new LotteryStatusCommand().onCommand(uuid, args);
        }

        //Draw Command
        if (args[0].toLowerCase().equals("draw")) {
            if (!Permissions.hasDrawPermission(uuid))
                return true;

            return new LotteryDrawCommand().onCommand(uuid, args);
        }

        return false;
    }

}
