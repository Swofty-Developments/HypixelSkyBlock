package net.swofty.commons.proxy.requirements.from;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class PlayerHasSwitchedFromHereRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("uuid") // The uuid of the player
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of();
    }
}
