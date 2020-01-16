package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class VaultEconomy implements IEconomy {

    private Economy economy;

    public VaultEconomy() {
        setupEconomy();
    }

    private void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return;
        }

        economy = rsp.getProvider();
    }

    @Override
    public void deposit(UUID uuid, double amount) {
        economy.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
    }

    @Override
    public void withdraw(UUID uuid, double amount) {
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        return economy.has(Bukkit.getOfflinePlayer(uuid), amount);
    }
}
