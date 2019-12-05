package net.draycia.lotteryplus.commands;

import net.draycia.lotteryplus.LotteryPlusCommon;

import java.util.UUID;

public class LotteryDrawCommand {

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
        String name = LotteryPlusCommon.getInstance().getPlayerUtils().getNameFromUUID(uuid);

        LotteryPlusCommon.getInstance().getLogger().info(name + " has ended the lottery!");
        LotteryPlusCommon.getInstance().getLotteryManager().endLottery();

        return true;
    }

}
