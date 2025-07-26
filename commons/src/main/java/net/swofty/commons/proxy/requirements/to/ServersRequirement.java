package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class ServersRequirement extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("servers_list") // JSON of all servers from the search
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("request_type") // Request type
                // -> ALL: Get all servers
                // -> TYPE: Get all servers of a specific type
                // -> UUID: Get all servers of a specific UUID
                // -> PLAYER_UUID: Get all servers of a specific player
        );
    }
}
