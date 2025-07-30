package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class RegisterTestFlowRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("test_flow_name"),
                new RequiredKey("handler"),
                new RequiredKey("players"),
                new RequiredKey("server_configs")
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("test_flow_name"),
                new RequiredKey("handler"),
                new RequiredKey("players"),
                new RequiredKey("server_configs")
        );
    }
}