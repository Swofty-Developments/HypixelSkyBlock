package net.swofty.commons.proxy;

import lombok.Getter;
import net.swofty.commons.proxy.requirements.to.*;
import net.swofty.commons.proxy.requirements.to.StaffChatRequirements;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public enum ToProxyChannels {
    REQUEST_SERVERS_NAME("server-name", new ServerNameRequirements()),
    PLAYER_COUNT("player-count", new PlayerCountRequirements()),
    PLAYER_HANDLER("player-handler", new PlayerHandlerRequirements()),
    PROXY_IS_ONLINE("proxy-online", new ProxyIsOnlineRequirements()),
    REGISTER_SERVER("register-server", new RegisterServerRequirements()),
    FINISHED_WITH_PLAYER("finished-with-player", new FinishedWithPlayerRequirements()),
    REQUEST_SERVERS("servers", new ServersRequirement()),
    REGISTER_TEST_FLOW("register-test-flow", new RegisterTestFlowRequirements()),
    TEST_FLOW_SERVER_READY("test-flow-server-ready", new TestFlowServerReadyRequirements()),
    STAFF_CHAT("staff-chat", new StaffChatRequirements()),
    PUNISH_PLAYER("punish-player", new KickPlayerRequirements())
    ;

    @Getter
    private final String channelName;
    private final ProxyChannelRequirements requirements;

    ToProxyChannels(String channel, ProxyChannelRequirements requirements) {
        this.channelName = channel;
        this.requirements = requirements;
    }

    ToProxyChannels(String channel) {
        this(channel, null);
    }

    public boolean matchesRequirementsServerSide(@Nullable JSONObject message) {
        if (message == null) return true;
        if (requirements == null) return true;
        return requirements.getRequiredKeysForServer().stream().allMatch(key -> message.has(key.key()));
    }

    public boolean matchesRequirementsProxySide(@Nullable JSONObject message) {
        if (message == null) return true;
        if (requirements == null) return true;
        return requirements.getRequiredKeysForProxy().stream().allMatch(key -> message.has(key.key()));
    }

    public static ToProxyChannels getChannelName(String channel) {
        for (ToProxyChannels proxyChannel : ToProxyChannels.values()) {
            if (proxyChannel.channelName.equalsIgnoreCase(channel)) {
                return proxyChannel;
            }
        }
        throw new IllegalArgumentException("Unknown channelName: " + channel);
    }
}