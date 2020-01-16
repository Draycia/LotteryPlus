package net.draycia.lotteryplus.abstraction.interfaces;

public interface IScheduler {

    public void scheduleSyncDelayedTask(Runnable runnable, long delay);

}
