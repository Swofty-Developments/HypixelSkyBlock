package net.swofty.commons.proxy.requirements.from;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class TeleportRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("uuid"), // The uuid of the player
                new RequiredKey("x"), // The x coordinate
                new RequiredKey("y"), // The y coordinate
                new RequiredKey("z"), // The z coordinate
                new RequiredKey("yaw"), // The yaw
                new RequiredKey("pitch") // The pitch
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of();
    }
}
