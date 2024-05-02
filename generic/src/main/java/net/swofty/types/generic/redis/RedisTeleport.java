package net.swofty.types.generic.redis;

import net.minestom.server.coordinate.Pos;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisTeleport implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        JSONObject json = new JSONObject(message);

        UUID playerUuid = UUID.fromString(json.getString("uuid"));
        Double x = json.getDouble("x");
        Double y = json.getDouble("y");
        Double z = json.getDouble("z");
        Float yaw = json.getFloat("yaw");
        Float pitch = json.getFloat("pitch");

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(playerUuid);
        player.teleport(new Pos(x, y, z, yaw, pitch));

        return "ok";
    }
}