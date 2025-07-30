package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class TestFlowServerReadyRequirements extends ProxyChannelRequirements {
    @Override
    public List<RequiredKey> getRequiredKeysForProxy() {
        return List.of(
                new RequiredKey("test_flow_name"),
                new RequiredKey("server_type"),
                new RequiredKey("server_index")
        );
    }

    @Override
    public List<RequiredKey> getRequiredKeysForServer() {
        return List.of(
                new RequiredKey("test_flow_name"),
                new RequiredKey("server_type"),
                new RequiredKey("server_index")
        );
    }
}