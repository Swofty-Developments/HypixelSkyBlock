package net.swofty.commons.proxy.requirements.from;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class PingServerRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of();
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of();
    }
}
