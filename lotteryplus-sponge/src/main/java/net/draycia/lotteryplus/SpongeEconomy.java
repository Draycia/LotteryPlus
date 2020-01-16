package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.IEconomy;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SpongeEconomy implements IEconomy {

    private EconomyService economy;

    public SpongeEconomy() {
        setupEconomy();
    }

    private void setupEconomy() {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);

        if (!serviceOpt.isPresent()) {
            return;
        }

        economy = serviceOpt.get();
    }

    @Override
    public void deposit(UUID uuid, double amount) {
        economy.getOrCreateAccount(uuid).ifPresent(account -> {
            account.deposit(
                    economy.getDefaultCurrency(),
                    BigDecimal.valueOf(amount),
                    Cause.of(EventContext.empty(), this)
            );
        });
    }

    @Override
    public void withdraw(UUID uuid, double amount) {
        economy.getOrCreateAccount(uuid).ifPresent(account -> {
            account.withdraw(
                    economy.getDefaultCurrency(),
                    BigDecimal.valueOf(amount),
                    Cause.of(EventContext.empty(), this)
            );
        });
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        Optional<UniqueAccount> account = economy.getOrCreateAccount(uuid);

        return account.filter(uniqueAccount -> uniqueAccount.getBalance(economy.getDefaultCurrency()).compareTo(BigDecimal.valueOf(amount)) >= 0).isPresent();
    }

}
