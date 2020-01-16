package net.draycia.lotteryplus.abstraction;

import java.util.HashMap;

public class LotteryServiceManager {

    private HashMap<Class, Object> registrations = new HashMap<>();

    public <T> void register(Class<T> service, T provider) {
        registrations.put(service, provider);
    }

    public <T> T getRegistration(Class<T> service) {
        return (T)registrations.get(service);
    }

}
