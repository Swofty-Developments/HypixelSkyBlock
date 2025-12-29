package net.swofty.commons.service;

import lombok.Getter;

@Getter
public enum FromServiceChannels {
    GET_SKYBLOCK_DATA("get-skyblock-player-data"),
    UPDATE_PLAYER_DATA("update-player-data"),
    LOCK_PLAYER_DATA("lock-player-data"),
    UNLOCK_PLAYER_DATA("unlock-player-data"),
    KICK_FROM_GUI("kick-from-gui"),
    PROPAGATE_BAZAAR_TRANSACTION("propagate-bazaar-transaction"),
    SEND_MESSAGE("send-message"),
    PROPAGATE_PARTY_EVENT("propagate_party_event"),
	GAME_INFORMATION("game-information"),
    INSTANTIATE_GAME("instantiate-game"),
    DARK_AUCTION_EVENT("dark-auction-event"),
    TRIGGER_DARK_AUCTION("trigger-dark-auction"),
    PROPAGATE_FRIEND_EVENT("propagate_friend_event"),
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