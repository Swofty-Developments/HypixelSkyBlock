package net.swofty.types.generic.item.items.runes;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RuneItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BloodRune implements CustomSkyBlockItem, RuneItem, SkyBlockEventClass {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>(List.of("§7Create a blood effect when you", "§7kill mobs!", ""));
        lore.addAll(defaultRuneLores());

        return lore;
    }

    @Override
    public int getRequiredRuneLevel() {
        return 1;
    }

    @Override
    public @NotNull String getColor() {
        return "§c";
    }

    @Override
    public RuneApplicableTo getRuneApplicableTo() {
        return RuneApplicableTo.WEAPONS;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e02677053dc54245dac4b399d14aae21ee71a010bd9c336c8ecee1a0dbe8f58b";
    }


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true , isAsync = true)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        SkyBlockItem runedItem = player.getStatistics().getItemWithRune(ItemType.BLOOD_RUNE);
        if (runedItem == null) return;
        int level = runedItem.getAttributeHandler().getRuneData().getLevel();
        int amountOfParticles = level * 3;

        player.sendPacket(new ParticlePacket(Particle.DRIPPING_LAVA.id(), true,
                event.getKilledMob().getPosition().x(), event.getKilledMob().getPosition().y(), event.getKilledMob().getPosition().z(),
                0, 0, 0, 0, amountOfParticles, null));
    }
}
