package net.swofty.type.skyblockgeneric.item.events;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class RuneItemEvent implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, isAsync = true)
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
