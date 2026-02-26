package net.swofty.type.skyblockgeneric.museum;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.museum.display.ItemMuseumDisplayHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public enum MuseumDisplays {
    ATRIUM_SLOTS(new ItemMuseumDisplayHandler(), List.of(MuseumableItemCategory.WEAPONS, MuseumableItemCategory.RARITIES),
        new Pos(33.5, 68.5, 23.5),
        new Pos(36.5, 68.5, 24.5),
        new Pos(38.5, 68.5, 26.5),
        new Pos(40.5, 68.5, 28.5),
        new Pos(41.5, 68.5, 31.5),
        new Pos(41.5, 68.5, 39.5),
        new Pos(40.5, 68.5, 42.5),
        new Pos(38.5, 68.5, 44.5),
        new Pos(36.5, 68.5, 46.5),
        new Pos(33.5, 68.5, 47.5),
        new Pos(25.5, 68.5, 47.5),
        new Pos(22.5, 68.5, 46.5),
        new Pos(20.5, 68.5, 44.5),
        new Pos(17.5, 68.5, 39.5),
        new Pos(17.5, 68.5, 31.5),
        new Pos(18.5, 68.5, 28.5),
        new Pos(20.5, 68.5, 26.5),
        new Pos(22.5, 68.5, 24.5),
        new Pos(25.5, 68.5, 23.5)
    );

    private static final Map<UUID, Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram>> displayEntities = new HashMap<>();

    private final MuseumDisplay displayHandler;
    private final List<MuseumableItemCategory> allowedItemCategories;
    private final List<Pos> positions;

    MuseumDisplays(MuseumDisplay displayHandler, List<MuseumableItemCategory> allowedItemCategories, Pos... positions) {
        this.displayHandler = displayHandler;
        this.allowedItemCategories = allowedItemCategories;
        this.positions = List.of(positions);
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name().substring(0, name().indexOf("_")));
    }

    public static List<Pos> getAllPositions() {
        List<Pos> allPositions = new ArrayList<>();
        for (MuseumDisplays display : values()) {
            allPositions.addAll(display.positions);
        }
        return allPositions;
    }

    public static void updateDisplay(@NotNull SkyBlockPlayer player) {
        DatapointMuseum.MuseumData playerMuseumData = player.getMuseumData();
        UUID museumPlayerToView = playerMuseumData.getCurrentlyViewing().playerUuid();
        UUID museumProfileToView = playerMuseumData.getCurrentlyViewing().profileUuid();

        DatapointMuseum.MuseumData museumDataViewing;
        if (museumPlayerToView.equals(player.getUuid())) {
            museumDataViewing = playerMuseumData;
        } else {
            museumDataViewing = SkyBlockDataHandler.createFromProfileOnly(
                    ProfilesDatabase.fetchDocument(museumProfileToView)
            ).get(SkyBlockDataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).getValue();
        }

        if (displayEntities.containsKey(player.getUuid())) {
            displayEntities.get(player.getUuid()).forEach((displayEntity, hologram) -> {
                if (hologram != null)
                    PlayerHolograms.removeExternalPlayerHologram(hologram);
                if (displayEntity != null)
                    displayEntity.remove();
            });
            displayEntities.remove(player.getUuid());
        }

        Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram> newDisplayEntities = new HashMap<>();

        for (MuseumDisplays display : values()) {
            int totalPositions = display.positions.size();
            int[] handledPositions = new int[totalPositions];
            for (Map.Entry<Integer, List<SkyBlockItem>> entry : museumDataViewing.getDisplayHandler().fetchAllDisplayedItemsBySlot().get(display).entrySet()) {
                int position = entry.getKey();

                MuseumDisplayEntityInformation displayInfo = display.displayHandler.display(player, display, false, position);

                // Add all entities from the display info
                for (LivingEntity entity : displayInfo.getEntities()) {
                    newDisplayEntities.put((MuseumDisplayEntityImpl) entity, displayInfo.getHologram());
                }

                handledPositions[position] = 1;
            }

            for (int i = 0; i < totalPositions; i++) {
                if (handledPositions[i] == 0) {
                    MuseumDisplayEntityInformation displayInfo = display.displayHandler.display(player, display, true, i);
                    // Add all entities from the display info
                    for (LivingEntity entity : displayInfo.getEntities()) {
                        newDisplayEntities.put((MuseumDisplayEntityImpl) entity, displayInfo.getHologram());
                    }
                }
            }
        }

        displayEntities.put(player.getUuid(), newDisplayEntities);
    }
}
