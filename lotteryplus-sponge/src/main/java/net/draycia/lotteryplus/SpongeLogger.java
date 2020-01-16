package net.draycia.lotteryplus;

import net.draycia.lotteryplus.abstraction.interfaces.ILogger;
import org.slf4j.Logger;

public class SpongeLogger implements ILogger {

    private Logger logger;

    public SpongeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void severe(String message) {
        logger.error(message);
    }

}
