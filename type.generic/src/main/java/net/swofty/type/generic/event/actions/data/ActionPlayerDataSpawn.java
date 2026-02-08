package net.swofty.type.generic.event.actions.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.GameDataHandlerRegistry;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.resourcepack.ResourcePackManager;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

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

        ResourcePackManager packManager = HypixelConst.getResourcePackManager();
        if (packManager != null) {
            packManager.sendPack(player);
            packManager.getActivePack().onPlayerJoin(player);
        }

        HypixelNPC.updateForPlayer(player);
        if (HypixelConst.isIslandServer()) return;
        PlayerHolograms.spawnAll(player);
    }
}