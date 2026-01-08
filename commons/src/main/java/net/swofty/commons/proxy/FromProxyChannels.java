package net.swofty.commons.proxy;

import lombok.Getter;
import net.swofty.commons.proxy.requirements.from.*;
import net.swofty.commons.proxy.requirements.from.BroadcastStaffChatRequirements;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Getter
public enum FromProxyChannels {
    TELEPORT("teleport", new TeleportRequirements()),
    PROMPT_PLAYER_FOR_AUTHENTICATION("authenticate", new PromptPlayerForAuthenticationRequirements()),
    PLAYER_HAS_SWITCHED_FROM_HERE("player-has-switched-from-here", new PlayerHasSwitchedFromHereRequirements()),
    DOES_SERVER_HAVE_ISLAND("does-server-have-island", new DoesServerHaveIslandRequirements()),
    REFRESH_COOP_DATA_ON_SERVER("refresh-coop-data", new RefreshCoopDataOnServerRequirements()),
    RUN_EVENT_ON_SERVER("run-event", new RunEventRequirements()),
    PING_SERVER("ping-server", new PingServerRequirements()),
    GIVE_PLAYERS_ORIGIN_TYPE("give-players-origin-type", new GivePlayersOriginTypeRequirements()),
    BROADCAST_STAFF_CHAT("broadcast-staff-chat", new BroadcastStaffChatRequirements())
    ;

    private final String channelName;
    private final ProxyChannelRequirements requirements;

    FromProxyChannels(String channelName, ProxyChannelRequirements requirements) {
        this.channelName = channelName;
        this.requirements = requirements;
    }

    FromProxyChannels(String channelName) {
        this(channelName, null);
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

    public static FromProxyChannels getChannelName(String channel) {
        for (FromProxyChannels proxyChannel : FromProxyChannels.values()) {
            if (proxyChannel.channelName.equalsIgnoreCase(channel)) {
                return proxyChannel;
            }
        }
        throw new IllegalArgumentException("Unknown channelName: " + channel);
    }
}
