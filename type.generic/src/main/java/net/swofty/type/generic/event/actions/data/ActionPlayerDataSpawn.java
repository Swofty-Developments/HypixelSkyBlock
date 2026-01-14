package net.swofty.type.generic.event.actions.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.GameDataHandlerRegistry;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.minestom.server.entity.Player;


import java.util.List;
import java.util.UUID;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        UUID uuid = player.getUuid();

        HypixelDataHandler handler = player.getDataHandler();
        handler.runOnLoad(player);

        // Run onLoad for additional game handlers
        List<Class<? extends GameDataHandler>> additionalHandlers =
                HypixelConst.getTypeLoader().getAdditionalDataHandlers();

        for (Class<? extends GameDataHandler> handlerClass : additionalHandlers) {
            GameDataHandler gameHandler = GameDataHandlerRegistry.get(handlerClass);
            if (gameHandler != null) {
                gameHandler.runOnLoad(uuid, player);
            }
        }

        // Handle cross-server teleport from /tpto command
        Object hook = player.getHookManager().getHook("tpto_target");
        if (hook != null) {
            String targetUUIDStr = (String) hook;
            player.getHookManager().removeHook("tpto_target");

            try {
                UUID targetUUID = UUID.fromString(targetUUIDStr);

                // Schedule teleport after a short delay to ensure player is fully loaded
                player.scheduler().buildTask(() -> {
                    if (player.getInstance() == null) {
                        player.sendMessage("§cFailed to teleport (instance not ready).");
                        return;
                    }

                    Player rawTarget = player.getInstance().getPlayers().stream()
                            .filter(p -> p.getUuid().equals(targetUUID))
                            .findFirst()
                            .orElse(null);

                    if (rawTarget != null) {
                        player.teleport(rawTarget.getPosition());
                        player.sendMessage("§2Teleported to " + HypixelPlayer.getColouredDisplayName(targetUUID) + "§2.");
                    } else {
                        player.sendMessage("§cTarget player is no longer on this server.");
                    }
                }).delay(java.time.Duration.ofMillis(500)).schedule();

            } catch (Exception e) {
                player.sendMessage("§cFailed to teleport to target player.");
            }
        }

        HypixelNPC.updateForPlayer(player);
        if (HypixelConst.isIslandServer()) return;
        PlayerHolograms.spawnAll(player);
    }
}