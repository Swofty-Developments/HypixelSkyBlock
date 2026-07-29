package net.swofty.type.replayviewer.redis.service;

import net.swofty.commons.protocol.objects.game.ViewReplayPushProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.protocol.RedisProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypedViewReplayHandler implements RedisMessageHandler<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> {
    public static Map<UUID, String> replay = new HashMap<>();
    public static Map<UUID, String> shareCode = new HashMap<>();

    private static final ViewReplayPushProtocol PROTOCOL = new ViewReplayPushProtocol();

    @Override
    public RedisProtocol<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public ViewReplayPushProtocol.Response handle(ViewReplayPushProtocol.Request request, RedisMessageContext context) {
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
