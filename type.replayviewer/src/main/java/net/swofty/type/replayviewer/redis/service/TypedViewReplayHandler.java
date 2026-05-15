package net.swofty.type.replayviewer.redis.service;

import net.swofty.commons.protocol.objects.game.ViewReplayPushProtocol;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypedViewReplayHandler implements TypedServiceHandler<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> {
    public static Map<UUID, String> replay = new HashMap<>();
    public static Map<UUID, String> shareCode = new HashMap<>();

    private static final ViewReplayPushProtocol PROTOCOL = new ViewReplayPushProtocol();

    @Override
    public ViewReplayPushProtocol getProtocol() {
        return PROTOCOL;
    }

    @Override
    public ViewReplayPushProtocol.Response onMessage(ViewReplayPushProtocol.Request request) {
        UUID uuid = request.uuid();
        String gameId = request.replayId();
        String code = request.shareCode();

        replay.put(uuid, gameId);
        if (code != null && !code.isEmpty()) {
            shareCode.put(uuid, code);
        }
        return new ViewReplayPushProtocol.Response(true);
    }

    @Nullable
    public static String getAndRemoveShareCode(UUID uuid) {
        return shareCode.remove(uuid);
    }
}
