package net.swofty.types.generic.museum.display;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.entity.metadata.other.InteractionMeta;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplay;
import net.swofty.types.generic.museum.MuseumDisplayEntityImpl;
import net.swofty.types.generic.museum.MuseumDisplayEntityInformation;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemMuseumDisplay extends MuseumDisplay {

    @Override
    public MuseumDisplayEntityInformation display(SkyBlockPlayer player,
                                                  MuseumDisplays category,
                                                  @Nullable SkyBlockItem item,
                                                  int position) {
        Pos pos = category.getPositions().get(position);
        PlayerHolograms.ExternalPlayerHologram hologram;
        LivingEntity itemEntity;
        ArrayList<LivingEntity> entities = new ArrayList<>();

        if (item == null) {
            hologram = PlayerHolograms.ExternalPlayerHologram.builder()
                    .player(player)
                    .text(new String[]{"§7" + category + " Slot #" + (position + 1), "§e§lCLICK TO EDIT"})
                    .pos(pos.add(0, 1, 0))
                    .build();
            PlayerHolograms.addExternalPlayerHologram(hologram);

            itemEntity = new MuseumDisplayEntityImpl(EntityType.ARMOR_STAND, category, position, true, null);
            itemEntity.setAutoViewable(false);
            itemEntity.setNoGravity(true);
            itemEntity.setInvisible(true);
            itemEntity.setInstance(player.getInstance(), pos);
            itemEntity.addViewer(player);
        } else {
            hologram = PlayerHolograms.ExternalPlayerHologram.builder()
                    .player(player)
                    .text(new String[]{item.getDisplayName()})
                    .pos(pos.add(0, 0.6, 0))
                    .build();
            PlayerHolograms.addExternalPlayerHologram(hologram);

            itemEntity = new MuseumDisplayEntityImpl(EntityType.ITEM, category, position, false, item);
            ItemEntityMeta itemDisplayMeta = (ItemEntityMeta) itemEntity.getEntityMeta();
            itemDisplayMeta.setItem(item.getItemStack());

            itemEntity.setAutoViewable(false);
            itemEntity.setInvisible(false);
            itemEntity.setInstance(player.getInstance(), pos.add(0, 1, 0));
            itemEntity.addViewer(player);

            LivingEntity interactableEntity = new MuseumDisplayEntityImpl(EntityType.INTERACTION, category, position, false, item);
            InteractionMeta interactionMeta = (InteractionMeta) interactableEntity.getEntityMeta();
            interactionMeta.setWidth(1);
            interactionMeta.setHeight(2);

            interactableEntity.setAutoViewable(false);
            interactableEntity.setInvisible(false);
            interactableEntity.setInstance(player.getInstance(), pos.add(0, 0.5, 0));
            interactableEntity.addViewer(player);
            entities.add(interactableEntity);
        }

        entities.add(itemEntity);
        return new MuseumDisplayEntityInformation(entities, hologram);
    }
}
