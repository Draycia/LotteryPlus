package net.draycia.lotteryplus;

import net.draycia.lotteryplus.interfaces.IScheduler;
import org.spongepowered.api.Sponge;

public class SpongeScheduler implements IScheduler {

    private LotteryPlusSponge plugin;

    public SpongeScheduler(LotteryPlusSponge plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable runnable, long delay) {
        Sponge.getScheduler().createTaskBuilder()
                .execute(runnable)
                .delayTicks(delay)
                .submit(plugin);
    }

}
