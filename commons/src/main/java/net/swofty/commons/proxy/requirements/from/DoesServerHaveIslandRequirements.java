package net.swofty.commons.proxy.requirements.from;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class DoesServerHaveIslandRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("island-uuid") // The uuid of the island
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of();
    }
}
