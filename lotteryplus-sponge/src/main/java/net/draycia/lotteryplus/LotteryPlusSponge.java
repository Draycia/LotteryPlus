package net.draycia.lotteryplus;

import com.google.inject.Inject;
import net.draycia.lotteryplus.abstraction.LotteryServiceManager;
import net.draycia.lotteryplus.abstraction.interfaces.*;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.util.HashMap;

@Plugin(
        id = "lotteryplus",
        name = "LotteryPlus",
        description = "LotteryPlus"
)
public class LotteryPlusSponge {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDirectory;

    private LotteryPlusCommon common;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.common = new LotteryPlusCommon();

        LotteryServiceManager manager = common.getServiceManager();

        manager.register(IEconomy.class, new SpongeEconomy());
        manager.register(IScheduler.class, new SpongeScheduler(this));
        manager.register(IChatProcessor.class, new SpongeChatProcessor());
        manager.register(IPlayerUtils.class, new SpongePlayerUtils());
        manager.register(ILogger.class, new SpongeLogger(logger));

        common.setup(configDirectory);

        HashMap<String, String> choices = new HashMap<>();
        choices.put("buy", "buy");
        choices.put("draw", "draw");
        choices.put("status", "status");

        CommandSpec lotteryCommand = CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("subcommand"), choices),
                        GenericArguments.optional(GenericArguments.string(Text.of("subargument")))
                )
                .permission("lotteryplus.lottery")
                .executor(new SpongeLotteryCommand())
                .build();

        Sponge.getCommandManager().register(this, lotteryCommand, "lottery");
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        event.getTargetEntity().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
                common.getMessages().getPlayerJoinMessage()));
    }

}
