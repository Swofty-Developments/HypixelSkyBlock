package net.swofty.type.skyblockgeneric.event.actions.player.authentication;

import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.mongodb.AuthenticationDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChatAuthentication implements HypixelEventClass {
    private static Map<UUID, Long> cooldowns = new HashMap<>();

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerChatEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (player.hasAuthenticated) return;

        if (cooldowns.containsKey(player.getUuid())) {
            if (System.currentTimeMillis() - cooldowns.get(player.getUuid()) < 1000) {
                player.sendMessage("§cPlease wait before sending another message.");
                return;
            }
        }
        cooldowns.put(player.getUuid(), System.currentTimeMillis());

        event.setCancelled(true);
        String[] args = event.getRawMessage().split(" ");

        AuthenticationDatabase.AuthenticationData data = new AuthenticationDatabase(player.getUuid()).getAuthenticationData();
        if (data == null) {
            if (args.length != 3) {
                player.sendMessage("§cYou must first sign-up to play this server!");
                player.sendMessage("§cIn the Minecraft chat, type §6signup <password> <password>§c.");
                player.sendMessage("§cIt is not a command, it's just a message. Nobody else can see it.");
                return;
            }

            if (!args[1].equals(args[2])) {
                player.sendMessage("§cYour passwords do not match.");
                return;
            }

            AuthenticationDatabase.AuthenticationData newData = AuthenticationDatabase.makeFromPassword(args[1]);
            new AuthenticationDatabase(player.getUuid()).setAuthenticationData(newData);

            player.sendMessage("§aYou have successfully signed up!");
            player.sendMessage("§aNow, in the Minecraft chat, type §6login <password>§a.");
            player.sendMessage("§aIt is not a command, it's just a message. Nobody else can see it.");
            player.sendMessage("§8Salt: §7" + newData.salt());
        } else {
            if (args.length != 2) {
                player.sendMessage("§cIn the Minecraft chat, type §6login <password>§c.");
                player.sendMessage("§cIt is not a command, it's just a message. Nobody else can see it.");
                return;
            }

            if (data.matches(args[1])) {
                player.sendMessage("§aYou have successfully logged in!");
                player.sendTo(ServerType.SKYBLOCK_ISLAND, true, true);
            } else {
                player.sendMessage("§cYour password is incorrect.");
                player.sendMessage("§8Salt: §7" + data.salt());
            }
        }
    }
}
