package net.swofty.types.generic.museum;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.display.ItemMuseumDisplay;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public enum MuseumDisplays {
    ATRIUM_SLOTS(new ItemMuseumDisplay(),
            List.of("§7Atrium Slot #{number}", "§eCLICK TO EDIT"),
            List.of(MuseumableItemCategory.WEAPONS),
            new Pos(-34.5, 66, 75.5),
            new Pos(-33.5, 66, 73.5),
            new Pos(-31.5, 66, 71.5),
            new Pos(-29.5, 66, 69.5),
            new Pos(-27.5, 66, 68.5),
            new Pos(-17.5, 66, 68.5),
            new Pos(-15.5, 66, 69.5),
            new Pos(-13.5, 66, 71.5),
            new Pos(-13.5, 66, 89.5),
            new Pos(-15.5, 66, 91.5),
            new Pos(-17.5, 66, 92.5),
            new Pos(-27.5, 66, 92.5),
            new Pos(-29.5, 66, 91.5),
            new Pos(-31.5, 66, 89.5),
            new Pos(-33.5, 66, 87.5),
            new Pos(-34.5, 66, 89.5)),
    ;

    private static final Map<UUID, Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram>> displayEntities = new HashMap<>();

    private final MuseumDisplay displayHandler;
    private final List<String> hologramDisplay;
    private final List<MuseumableItemCategory> allowedItemCategories;
    private final List<Pos> positions;

    MuseumDisplays(MuseumDisplay displayHandler, List<String> hologramDisplay, List<MuseumableItemCategory> allowedItemCategories, Pos... positions) {
        this.displayHandler = displayHandler;
        this.hologramDisplay = hologramDisplay;
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
        UUID museumPlayerToView = playerMuseumData.getCurrentlyViewing().getKey();
        UUID museumProfileToView = playerMuseumData.getCurrentlyViewing().getValue();

        DatapointMuseum.MuseumData museumDataViewing;
        if (museumPlayerToView.equals(player.getUuid())) {
            museumDataViewing = playerMuseumData;
        } else {
            museumDataViewing = DataHandler.getProfileOfOfflinePlayer(museumPlayerToView, museumProfileToView)
                    .get(DataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).getValue();
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
            if (museumDataViewing.getInDisplay(display).isEmpty()) {
                continue;
            }

            int totalPositions = display.positions.size();
            int[] handledPositions = new int[totalPositions];
            for (Map.Entry<SkyBlockItem, Integer> entry : museumDataViewing.getInDisplay(display).entrySet()) {
                SkyBlockItem item = entry.getKey();
                int position = entry.getValue();

                Map.Entry<LivingEntity, PlayerHolograms.ExternalPlayerHologram> displays =
                        display.displayHandler.display(player, display, item, position);
                newDisplayEntities.put((MuseumDisplayEntityImpl) displays.getKey(), displays.getValue());

                handledPositions[position] = 1;
            }

            for (int i = 0; i < totalPositions; i++) {
                if (handledPositions[i] == 0) {
                    Map.Entry<LivingEntity, PlayerHolograms.ExternalPlayerHologram> displays =
                            display.displayHandler.display(player, display, null, i);
                    newDisplayEntities.put((MuseumDisplayEntityImpl) displays.getKey(), displays.getValue());
                }
            }
        }
    }
}
