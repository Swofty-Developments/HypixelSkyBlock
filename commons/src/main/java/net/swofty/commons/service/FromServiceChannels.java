package net.swofty.commons.service;

import lombok.Getter;
import net.swofty.commons.proxy.ProxyChannelRequirements;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@Getter
public enum FromServiceChannels {
    GET_PLAYER_DATA("get-player-data"),
    UPDATE_PLAYER_DATA("update-player-data"),
    LOCK_PLAYER_DATA("lock-player-data"),
    UNLOCK_PLAYER_DATA("unlock-player-data"),
    KICK_FROM_GUI("kick-from-gui"),
    ;

    private final String channelName;

    FromServiceChannels(String channelName) {
        this.channelName = channelName;
    }

    public static FromServiceChannels getChannelName(String channel) {
        for (FromServiceChannels serviceChannel : FromServiceChannels.values()) {
            if (serviceChannel.channelName.equalsIgnoreCase(channel)) {
                return serviceChannel;
            }
        }
        throw new IllegalArgumentException("Unknown channelName: " + channel);
    }
}