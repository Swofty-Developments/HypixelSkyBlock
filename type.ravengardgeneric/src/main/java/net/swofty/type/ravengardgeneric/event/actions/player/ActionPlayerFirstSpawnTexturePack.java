package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.texturepack.TexturePackManager;
import org.tinylog.Logger;

public class ActionPlayerFirstSpawnTexturePack implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }

        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelConst.getResourcePackManager() == null) {
            Logger.warn("Resource pack manager is null during Ravengard first spawn for {}", player.getUsername());
        }

        TexturePackManager manager = TexturePackManager.getInstance();
        if (manager != null) {
            manager.enableFor(player);
            Logger.info("Enabled texture-pack HUD for {}", player.getUsername());
        } else {
            Logger.warn("Texture pack manager is null while joining {}", player.getUsername());
        }
    }
}
