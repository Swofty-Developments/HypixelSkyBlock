package net.swofty.service.guild.endpoints;

import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.guild.events.*;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.guild.SendGuildEventToServiceProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.guild.GuildCache;

public class GuildEventToServiceEndpoint implements ServiceEndpoint<
        SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage,
        SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {

    @Override
    public SendGuildEventToServiceProtocolObject associatedProtocolObject() {
        return new SendGuildEventToServiceProtocolObject();
    }

    @Override
    public SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse onMessage(
            ServiceProxyRequest message,
            SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage messageObject) {
        GuildEvent event = messageObject.event();

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
            default -> throw new IllegalArgumentException("Unknown guild event type: " + event.getClass().getSimpleName());
        }

        return new SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse(true);
    }
}
