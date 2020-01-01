package net.draycia.lotteryplus;

import net.draycia.lotteryplus.commands.LotteryCommand;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SpongeLotteryCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof ConsoleSource) {
            return CommandResult.success();
        }

        String[] arguments = null;

        Optional<String> subCommand = args.getOne(Text.of("subcommand"));
        Optional<String> subArguments = args.getOne(Text.of("subargument"));

        if (subCommand.isPresent()) {
            if (subArguments.isPresent()) {
                arguments = new String[] {subCommand.get(), subArguments.get()};
            } else {
                arguments = new String[] {subCommand.get()};
            }
        }

        new LotteryCommand().onCommand(((Player)src).getUniqueId(), arguments);

        return CommandResult.success();
    }

}
