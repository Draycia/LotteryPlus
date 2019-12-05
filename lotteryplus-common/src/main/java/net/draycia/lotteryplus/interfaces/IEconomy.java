package net.draycia.lotteryplus.interfaces;

import java.util.UUID;

public interface IEconomy {

    public void deposit(UUID uuid, double amount);

    public void withdraw(UUID uuid, double amount);

    public boolean hasAmount(UUID uuid, double amount);

}
