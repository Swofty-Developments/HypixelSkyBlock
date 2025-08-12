package net.swofty.type.skyblockgeneric.museum;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.swofty.commons.StringUtility;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.museum.display.ArmorMuseumDisplayHandler;
import net.swofty.type.skyblockgeneric.museum.display.ItemMuseumDisplayHandler;
import SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public enum MuseumDisplays {
    ATRIUM_SLOTS(new ItemMuseumDisplayHandler(),
            List.of(MuseumableItemCategory.WEAPONS, MuseumableItemCategory.RARITIES),
            new Pos(-34.5, 65, 75.5), new Pos(-33.5, 65, 73.5), new Pos(-31.5, 65, 71.5),
            new Pos(-29.5, 65, 69.5), new Pos(-27.5, 65, 68.5), new Pos(-17.5, 65, 68.5),
            new Pos(-15.5, 65, 69.5), new Pos(-13.5, 65, 71.5), new Pos(-13.5, 65, 89.5),
            new Pos(-15.5, 65, 91.5), new Pos(-17.5, 65, 92.5), new Pos(-27.5, 65, 92.5),
            new Pos(-29.5, 65, 91.5), new Pos(-31.5, 65, 89.5), new Pos(-33.5, 65, 87.5),
            new Pos(-34.5, 65, 85.5)),
    RARITIES_SLOTS(new ItemMuseumDisplayHandler(),
            List.of(MuseumableItemCategory.RARITIES),
            new Pos(-22.5, 55, 128.5), new Pos(-19.5, 55, 125.5), new Pos(-25.5, 55, 125.5),
            new Pos(-18.5, 52, 115.5), new Pos(-15.5, 52, 116.5), new Pos(-13.5, 52, 118.5),
            new Pos(-12.5, 52, 121.5), new Pos(-9.5, 52, 125.5), new Pos(-12.5, 52, 129.5),
            new Pos(-13.5, 52, 132.5), new Pos(-15.5, 52, 134.5), new Pos(-18.5, 52, 135.5),
            new Pos(-22.5, 52, 138.5), new Pos(-26.5, 52, 135.5), new Pos(-29.5, 52, 134.5),
            new Pos(-31.5, 52, 132.5), new Pos(-32.5, 52, 129.5), new Pos(-35.5, 52, 125.5),
            new Pos(-31.5, 52, 118.5), new Pos(-29.5, 52, 116.5), new Pos(-26.5, 52, 115.5)),
    WEAPONS_WING_SLOTS(new ItemMuseumDisplayHandler(),
            List.of(MuseumableItemCategory.WEAPONS),
            new Pos(-27.5, 53, 45.5), new Pos(-30.5, 53, 44.5), new Pos(-32.5, 53, 41.5),
            new Pos(-33.5, 53, 38.5), new Pos(-32.5, 53, 35.5), new Pos(-30.5, 53, 32.5),
            new Pos(-27.5, 53, 31.5), new Pos(-17.5, 53, 31.5), new Pos(-14.5, 53, 32.5),
            new Pos(-12.5, 53, 35.5), new Pos(-11.5, 53, 38.5), new Pos(-12.5, 53, 41.5),
            new Pos(-14.5, 53, 44.5), new Pos(-17.5, 53, 45.5), new Pos(-33.5, 55, 23.5),
            new Pos(-27.5, 55, 16.5), new Pos(-22.5, 55, 14.5), new Pos(-17.5, 55, 16.5),
            new Pos(-13.5, 55, 19.5), new Pos(-11.5, 55, 23.5), new Pos(-31.5, 55, 19.5)
    ),
    ARMOR_SETS(new ArmorMuseumDisplayHandler(),
            List.of(MuseumableItemCategory.ARMOR_SETS),
            new Pos(-10.5, 66, 84.5, 90, 0), new Pos(-11.5, 66, 87.5, 90, 0),
            new Pos(-10.5, 66, 76.5, 90, 0), new Pos(-11.5, 66, 73.5, 90, 0)
    ),
    ;

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
