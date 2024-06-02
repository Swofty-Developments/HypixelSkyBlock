package net.swofty.types.generic.museum.display;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplay;
import net.swofty.types.generic.museum.MuseumDisplayEntityImpl;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Map;

public class ItemMuseumDisplay extends MuseumDisplay {

    @Override
    public Map.Entry<LivingEntity,
            PlayerHolograms.ExternalPlayerHologram> display(SkyBlockPlayer player,
                                                            MuseumDisplays category,
                                                            @Nullable SkyBlockItem item,
                                                            int position) {
        Pos pos = category.getPositions().get(position);
        PlayerHolograms.ExternalPlayerHologram hologram;
        LivingEntity entity;

        if (item == null) {
            hologram = PlayerHolograms.ExternalPlayerHologram.builder()
                    .player(player)
                    .text(new String[]{"§7" + category + " Slot #" + (position + 1), "§e§lCLICK TO EDIT"})
                    .pos(pos.add(0, 1, 0))
                    .build();
            PlayerHolograms.addExternalPlayerHologram(hologram);

            entity = new MuseumDisplayEntityImpl(EntityType.ARMOR_STAND, category, position, true);
            entity.setAutoViewable(false);
            entity.setNoGravity(true);
            entity.setInvisible(true);
            entity.setInstance(player.getInstance(), pos);
            entity.addViewer(player);
        } else {
            hologram = PlayerHolograms.ExternalPlayerHologram.builder()
                    .player(player)
                    .text(new String[]{item.getDisplayName()})
                    .pos(pos.add(0, 1, 0))
                    .build();
            PlayerHolograms.addExternalPlayerHologram(hologram);

            entity = new MuseumDisplayEntityImpl(EntityType.ITEM, category, position, false);
            ItemEntityMeta itemDisplayMeta = (ItemEntityMeta) entity.getEntityMeta();
            itemDisplayMeta.setItem(item.getItemStack());

            entity.setAutoViewable(false);
            entity.setNoGravity(true);
            entity.setInvisible(false);
            entity.setInstance(player.getInstance(), pos);
            entity.addViewer(player);
        }

        return new AbstractMap.SimpleEntry<>(entity, hologram);
    }
}
