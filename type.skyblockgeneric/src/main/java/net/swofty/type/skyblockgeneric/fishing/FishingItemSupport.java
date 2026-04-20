package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingBaitComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodMetadataComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingShipPartComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FishingItemSupport {
    private FishingItemSupport() {
    }

    public static @Nullable FishingRodMetadataComponent getRodMetadata(@Nullable String itemId) {
        return getComponent(itemId, FishingRodMetadataComponent.class);
    }

    public static @Nullable FishingBaitComponent getBait(@Nullable String itemId) {
        return getComponent(itemId, FishingBaitComponent.class);
    }

    public static @Nullable FishingRodPartComponent getRodPart(@Nullable String itemId) {
        return getComponent(itemId, FishingRodPartComponent.class);
    }

    public static @Nullable FishingShipPartComponent getShipPart(@Nullable String itemId) {
        return getComponent(itemId, FishingShipPartComponent.class);
    }

    public static @Nullable SkyBlockItem getItem(@Nullable String itemId) {
        if (itemId == null || itemId.isBlank()) {
            return null;
        }
        ItemType type = ItemType.get(itemId);
        return type == null ? null : new SkyBlockItem(type);
    }

    public static List<SkyBlockItem> getBaits() {
        return getItemsWith(FishingBaitComponent.class);
    }

    public static List<SkyBlockItem> getRodParts() {
        return getItemsWith(FishingRodPartComponent.class);
    }

    public static List<SkyBlockItem> getShipParts() {
        return getItemsWith(FishingShipPartComponent.class);
    }

    private static <T extends SkyBlockItemComponent> @Nullable T getComponent(@Nullable String itemId, Class<T> componentClass) {
        if (itemId == null || itemId.isBlank()) {
            return null;
        }

        ItemType type = ItemType.get(itemId);
        if (type == null) {
            return null;
        }

        SkyBlockItem item = new SkyBlockItem(type);
        if (!item.hasComponent(componentClass)) {
            return null;
        }
        return item.getComponent(componentClass);
    }

    private static List<SkyBlockItem> getItemsWith(Class<? extends net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent> componentClass) {
        List<SkyBlockItem> items = new ArrayList<>();
        for (String id : ConfigurableSkyBlockItem.getIDs()) {
            ItemType type = ItemType.get(id);
            if (type == null) {
                continue;
            }

            SkyBlockItem item = new SkyBlockItem(type);
            if (!item.hasComponent(componentClass)) {
                continue;
            }
            items.add(item);
        }

        items.sort(Comparator.<SkyBlockItem>comparingInt(item -> item.getAttributeHandler().getRarity().ordinal())
            .thenComparing(SkyBlockItem::getDisplayName));
        return List.copyOf(items);
    }
}
