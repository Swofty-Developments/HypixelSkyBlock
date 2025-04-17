package net.swofty.types.generic.newitem.events;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class RuneItemEvent implements SkyBlockEventClass {
    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true , isAsync = true)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        SkyBlockItem runedItem = player.getStatistics().getItemWithRune(ItemType.BLOOD_RUNE);
        if (runedItem == null) return;
        int level = runedItem.getAttributeHandler().getRuneData().getLevel();
        int amountOfParticles = level * 3;

        player.sendPacket(new ParticlePacket(Particle.DRIPPING_LAVA, true, false,
                event.getKilledMob().getPosition().x(), event.getKilledMob().getPosition().y(), event.getKilledMob().getPosition().z(),
                0, 0, 0, 0, amountOfParticles));
    }
}
