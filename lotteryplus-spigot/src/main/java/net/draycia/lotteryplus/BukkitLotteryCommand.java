package net.draycia.lotteryplus;

import net.draycia.lotteryplus.commands.LotteryCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitLotteryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }

        new LotteryCommand().onCommand(((Player)sender).getUniqueId(), args);

        return true;
    }

}
