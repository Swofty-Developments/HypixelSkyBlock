package net.swofty.commons.proxy;

import java.util.List;

public abstract class ProxyChannelRequirements {
    public abstract List<RequiredKey> getRequiredKeysForProxy();
    public abstract List<RequiredKey> getRequiredKeysForServer();

    public record RequiredKey(String key) {}
}
