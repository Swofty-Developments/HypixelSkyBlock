package net.swofty.service.guild.endpoints;

import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.guild.events.*;
import net.swofty.commons.protocol.objects.guild.SendGuildEventToServiceProtocolObject;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.service.guild.GuildCache;

public class GuildEventToServiceEndpoint implements RedisMessageHandler<
    SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage,
    SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {

    @Override
    public SendGuildEventToServiceProtocolObject protocol() {
        return new SendGuildEventToServiceProtocolObject();
    }

    @Override
    public SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse handle(SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage messageObject, RedisMessageContext context) {
        GuildEvent template = GuildEvent.findFromType(messageObject.eventType());
        GuildEvent event = template.getSerializer().deserialize(messageObject.eventData());

        switch (event) {
            case GuildCreateRequestEvent e -> GuildCache.handleCreateRequest(e);
            case GuildInviteRequestEvent e -> GuildCache.handleInviteRequest(e);
            case GuildAcceptInviteRequestEvent e -> GuildCache.handleAcceptInvite(e);
            case GuildLeaveRequestEvent e -> GuildCache.handleLeaveRequest(e);
            case GuildKickRequestEvent e -> GuildCache.handleKickRequest(e);
            case GuildDisbandRequestEvent e -> GuildCache.handleDisbandRequest(e);
            case GuildPromoteRequestEvent e -> GuildCache.handlePromoteRequest(e);
            case GuildDemoteRequestEvent e -> GuildCache.handleDemoteRequest(e);
            case GuildTransferRequestEvent e -> GuildCache.handleTransferRequest(e);
            case GuildChatRequestEvent e -> GuildCache.handleChatRequest(e);
            case GuildSettingRequestEvent e -> GuildCache.handleSettingRequest(e);
            case GuildMuteRequestEvent e -> GuildCache.handleMuteRequest(e);
            case GuildUnmuteRequestEvent e -> GuildCache.handleUnmuteRequest(e);
            case GuildSetRankRequestEvent e -> GuildCache.handleSetRankRequest(e);
            case GuildProgressRequestEvent e -> GuildCache.handleProgressRequest(e);
            default ->
                throw new IllegalArgumentException("Unknown guild event type: " + event.getClass().getSimpleName());
        }

        return new SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse(true);
    }
}
