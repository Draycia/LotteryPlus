package net.draycia.lotteryplus.permissions;

import net.draycia.lotteryplus.LotteryPlusCommon;

import java.util.UUID;

public class Permissions {

    /**
     * Checks a permissible (player) for a permission
     *
     * @param permission permission address
     * @return true if permissible has permission
     */
    public static boolean hasPermission(UUID uuid, String permission) {
        return LotteryPlusCommon.getInstance().getPlayerUtils().playerHasPermission(uuid, permission);
    }

    /**
     * Checks to see if permissible has the lottery.buy permission
     *
     * @return true if permissible has the permission
     */
    public static boolean hasBuyPermission(UUID uuid) {
        return hasPermission(uuid, "lottery.buy");
    }

    /**
     * Checks to see if permissible has the lottery.status permission
     *
     * @return true if permissible has the permission
     */
    public static boolean hasStatusPermission(UUID uuid) {
        return hasPermission(uuid,"lottery.status");
    }

    /**
     * Checks to see if permissible has the lottery.draw permission
     *
     * @return true if permissible has the permission
     */
    public static boolean hasDrawPermission(UUID uuid) {
        return hasPermission(uuid, "lottery.draw");
    }
}
