package net.swofty.types.generic.event.actions.player.fall;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerVoid implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.getPosition().y() < 0 && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.damage(DamageType.OUT_OF_WORLD, Float.MAX_VALUE);
        }
    }
}
