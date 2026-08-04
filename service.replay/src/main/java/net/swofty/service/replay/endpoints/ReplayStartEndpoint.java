package net.swofty.service.replay.endpoints;

import net.swofty.commons.protocol.objects.replay.ReplayStartProtocolObject;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.replay.ReplayService;
import net.swofty.type.game.replay.ReplayMetadata;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplayStartEndpoint implements RedisMessageHandler<
        ReplayStartProtocolObject.StartMessage,
        ReplayStartProtocolObject.StartResponse> {

    @Override
    public ReplayStartProtocolObject protocol() {
        return new ReplayStartProtocolObject();
    }

    @Override
    public ReplayStartProtocolObject.StartResponse handle(ReplayStartProtocolObject.StartMessage msg, RedisMessageContext context) {

        try {
            Map<String, ReplayMetadata.TeamInfo> teamInfo = new HashMap<>();
            msg.teamInfo().forEach((teamId, info) ->
                    teamInfo.put(teamId, new ReplayMetadata.TeamInfo(info.name(), info.colorCode(), info.color()))
            );

            Map<UUID, ReplayMetadata.PlayerInfo> playerInfo = new HashMap<>();
            if (msg.playerInfo() != null) {
                msg.playerInfo().forEach((uuid, info) -> playerInfo.put(uuid, new ReplayMetadata.PlayerInfo(
                        info.entityId(), info.textureValue(), info.textureSignature(), info.displayName(),
                        info.prefix(), info.suffix(), info.nameColor(), info.teamId()
                )));
            }

            ReplayService.getSessionManager().startSession(
                    msg.replayId(),
                    msg.gameId(),
                    msg.serverType(),
                    msg.serverId(),
                    msg.gameTypeName(),
                    msg.mapName(),
                    msg.mapHash(),
                    msg.startTime(),
                    msg.mapCenterX(),
                    msg.mapCenterZ(),
                    msg.players(),
                    msg.teams(),
                    teamInfo,
                    playerInfo
            );

            Logger.info("Started replay session {} for game {} with {} players",
                    msg.replayId(), msg.gameId(), msg.players().size());

            return new ReplayStartProtocolObject.StartResponse(true, null);

        } catch (Exception e) {
            Logger.error(e, "Failed to start replay session");
            return new ReplayStartProtocolObject.StartResponse(false, e.getMessage());
        }
    }
}
