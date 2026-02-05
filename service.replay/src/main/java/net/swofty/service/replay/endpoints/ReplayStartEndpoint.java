package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayStartProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import net.swofty.service.replay.session.RecordingSession;
import net.swofty.type.game.replay.ReplayMetadata;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;

public class ReplayStartEndpoint implements ServiceEndpoint<
        ReplayStartProtocolObject.StartMessage,
        ReplayStartProtocolObject.StartResponse> {

    @Override
    public ReplayStartProtocolObject associatedProtocolObject() {
        return new ReplayStartProtocolObject();
    }

    @Override
    public ReplayStartProtocolObject.StartResponse onMessage(
            ServiceProxyRequest message,
            ReplayStartProtocolObject.StartMessage msg) {

        try {
            Map<String, ReplayMetadata.TeamInfo> teamInfo = new HashMap<>();
            msg.teamInfo().forEach((teamId, info) ->
                    teamInfo.put(teamId, new ReplayMetadata.TeamInfo(info.name(), info.colorCode(), info.color()))
            );

            // TODO: Start session - REPLAYTODO
            RecordingSession session = ReplayService.getSessionManager().startSession(
                    msg.replayId(),
                    msg.gameId(),
                    msg.serverType(),
                    msg.gameTypeName(),
                    msg.mapName(),
                    msg.mapHash(),
                    msg.startTime(),
                    msg.mapCenterX(),
                    msg.mapCenterZ(),
                    msg.players(),
                    msg.teams(),
                    teamInfo
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
