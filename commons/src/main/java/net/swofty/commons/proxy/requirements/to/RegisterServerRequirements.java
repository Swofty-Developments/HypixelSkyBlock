package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class RegisterServerRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("port") // Port for the server
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("type") // Type of the server
                // OPTIONALLY YOU CAN PASS THROUGH A PORT TO FORCE IT
        );
    }
}
