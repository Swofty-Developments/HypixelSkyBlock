package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class ServerNameRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("server-name"),
                new RequiredKey("shortened-server-name")
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of();
    }
}
