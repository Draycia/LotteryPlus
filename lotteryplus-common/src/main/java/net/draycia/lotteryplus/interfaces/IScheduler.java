package net.draycia.lotteryplus.interfaces;

public interface IScheduler {

    public void scheduleSyncDelayedTask(Runnable runnable, long delay);

}
