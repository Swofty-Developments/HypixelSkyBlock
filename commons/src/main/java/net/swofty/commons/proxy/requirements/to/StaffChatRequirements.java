package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class StaffChatRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of();
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("type"), // "message", "join", or "leave"
                new RequiredKey("formatted_message") // Pre-formatted message (for "message" type) or empty (for join/leave)
        );
    }
}
