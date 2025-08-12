package net.swofty.type.skyblockgeneric.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.generic.data.mongodb.AuthenticationDatabase;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class RedisAuthenticate implements ProxyToClient {
    public static ArrayList<UUID> toAuthenticate = new ArrayList<>();

    public static void promptAuthentication(HypixelPlayer player) {
        player.sendMessage("§aHey! You need to authenticate your account to play on this server.");
        player.sendMessage("§aAll passwords are encrypted using modern standards, so you're safe!");
        player.sendMessage(" ");

        AuthenticationDatabase.AuthenticationData data = new AuthenticationDatabase(player.getUuid()).getAuthenticationData();
        if (data == null) {
            player.sendMessage("§eYou must first sign-up to play this server!");
            player.sendMessage("§eIn the Minecraft chat, type §6signup <password> <password>§e.");
            player.sendMessage("§eIt is not a command, it's just a message. Nobody else can see it.");
        } else {
            player.sendMessage("§eIn the Minecraft chat, type §6login <password>§e.");
            player.sendMessage("§eIt is not a command, it's just a message. Nobody else can see it.");
        }

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (player.isOnline()) {
                player.kick("§cYou have been kicked for not authenticating your account.");
            }
        }, TaskSchedule.seconds(30), TaskSchedule.stop());
    }

    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.PROMPT_PLAYER_FOR_AUTHENTICATION;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        toAuthenticate.add(uuid);
        return new JSONObject();
    }
}
