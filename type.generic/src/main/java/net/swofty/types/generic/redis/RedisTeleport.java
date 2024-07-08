package net.swofty.types.generic.redis;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisTeleport implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.TELEPORT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        Double x = message.getDouble("x");
        Double y = message.getDouble("y");
        Double z = message.getDouble("z");
        Float yaw = message.getFloat("yaw");
        Float pitch = message.getFloat("pitch");

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new JSONObject();
        player.teleport(new Pos(x, y, z, yaw, pitch));

        return new JSONObject();
    }
}