package net.swofty.types.generic.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.data.mongodb.AuthenticationDatabase;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class RedisAuthenticate implements ProxyToClient {
    public static ArrayList<UUID> toAuthenticate = new ArrayList<>();

    @Override
    public String onMessage(String message) {
        toAuthenticate.add(UUID.fromString(message));
        MinecraftServer.getSchedulerManager().scheduleTask(() -> toAuthenticate.remove(UUID.fromString(message)),
                TaskSchedule.seconds(1), TaskSchedule.stop());
        return "true";
    }

    public static void promptAuthentication(SkyBlockPlayer player) {
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
}
