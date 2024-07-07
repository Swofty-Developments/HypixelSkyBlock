package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class PlayerCountRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("player-count")
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("lookup-type"), // ALL, a server type, or a UUID
                new RequiredKey("lookup-value") // The value to lookup, if the type is ALL then this can be empty
        );
    }

    public enum LookupType {
        ALL,
        TYPE,
        UUID
    }
}
