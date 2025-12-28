package net.swofty.type.skyblockgeneric.minion.extension;

import lombok.SneakyThrows;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.minion.extension.extensions.MinionFuelExtension;
import net.swofty.type.skyblockgeneric.minion.extension.extensions.MinionShippingExtension;
import net.swofty.type.skyblockgeneric.minion.extension.extensions.MinionSkinExtension;
import net.swofty.type.skyblockgeneric.minion.extension.extensions.MinionUpgradeExtension;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MinionExtensionData {
    private final Map<Integer, MinionExtension> extensionData = new HashMap<>();

    public void setData(int slot, MinionExtension extension) {
        extensionData.put(slot, extension);
    }

    @SneakyThrows
    public MinionExtension getOfType(Class<? extends MinionExtension> clazz) {
        return extensionData.values().stream().filter(clazz::isInstance).findFirst().orElse(
                clazz.getConstructor(ItemType.class, Object.class).newInstance(null, null)
        );
    }

    public boolean hasMinionUpgrade(ItemType type) {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .anyMatch(extension -> extension.getItemTypePassedIn() == type);
    }

    public int getMinionUpgradeCount(ItemType type) {
        return (int)extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .filter(extension -> extension.getItemTypePassedIn() == type).count();
    }

    public SkyBlockItem[] getMinionUpgrades() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .filter(extension -> extension.getItemTypePassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypePassedIn()))
                .toArray(SkyBlockItem[]::new);
    }
    @Nullable
    public SkyBlockItem getMinionSkin() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionSkinExtension)
                .filter(extension -> extension.getItemTypePassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypePassedIn()))
                .findFirst().orElse(null);
    }
    @Nullable
    public SkyBlockItem getAutomatedShipping() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionShippingExtension)
                .filter(extension -> extension.getItemTypePassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypePassedIn()))
                .findFirst().orElse(null);
    }
    @Nullable
    public SkyBlockItem getFuel() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionFuelExtension)
                .filter(extension -> extension.getItemTypePassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypePassedIn()))
                .findFirst().orElse(null);
    }

    @SneakyThrows
    public MinionExtension getMinionExtension(int slot) {
        if (!extensionData.containsKey(slot)) {
            MinionExtension extension = MinionExtensions.getFromSlot(slot).getInstance().getDeclaredConstructor(
                    ItemType.class, Object.class
            ).newInstance(null, null);

            extensionData.put(slot, extension);
        }

        return extensionData.get(slot);
    }

    public String toString() {
        JSONObject json = new JSONObject();
        extensionData.forEach((slot, entry) -> {
            JSONObject entryJson = new JSONObject();
            entryJson.put("serialized", entry.toString());
            json.put(String.valueOf(slot), entryJson);
        });
        return json.toString();
    }
    @SneakyThrows
    public static MinionExtensionData fromString(String string) {
        MinionExtensionData data = new MinionExtensionData();
        JSONObject json = new JSONObject(string);

        for (String key : json.keySet()) {
            JSONObject entryJson = json.getJSONObject(key);
            int slot = Integer.parseInt(key);

            MinionExtension extension = MinionExtensions.getFromSlot(slot).getInstance().getDeclaredConstructor(
                    ItemType.class, Object.class
            ).newInstance(null, null);

            extension.fromString(entryJson.getString("serialized"));

            data.setData(slot, extension);
        }
        return data;
    }
}
