package net.swofty.types.generic.minion.extension;

import lombok.SneakyThrows;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.extension.extensions.MinionFuelExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionShippingExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionSkinExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionUpgradeExtension;
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
                clazz.getConstructor(ItemTypeLinker.class, Object.class).newInstance(null, null)
        );
    }

    public boolean hasMinionUpgrade(ItemTypeLinker type) {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .anyMatch(extension -> extension.getItemTypeLinkerPassedIn() == type);
    }

    public int getMinionUpgradeCount(ItemTypeLinker type) {
        return (int)extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .filter(extension -> extension.getItemTypeLinkerPassedIn() == type).count();
    }

    public SkyBlockItem[] getMinionUpgrades() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionUpgradeExtension)
                .filter(extension -> extension.getItemTypeLinkerPassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypeLinkerPassedIn()))
                .toArray(SkyBlockItem[]::new);
    }
    @Nullable
    public SkyBlockItem getMinionSkin() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionSkinExtension)
                .filter(extension -> extension.getItemTypeLinkerPassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypeLinkerPassedIn()))
                .findFirst().orElse(null);
    }
    @Nullable
    public SkyBlockItem getAutomatedShipping() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionShippingExtension)
                .filter(extension -> extension.getItemTypeLinkerPassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypeLinkerPassedIn()))
                .findFirst().orElse(null);
    }
    @Nullable
    public SkyBlockItem getFuel() {
        return extensionData.values().stream()
                .filter(extension -> extension instanceof MinionFuelExtension)
                .filter(extension -> extension.getItemTypeLinkerPassedIn() != null)
                .map(extension -> new SkyBlockItem(extension.getItemTypeLinkerPassedIn()))
                .findFirst().orElse(null);
    }

    @SneakyThrows
    public MinionExtension getMinionExtension(int slot) {
        if (!extensionData.containsKey(slot)) {
            MinionExtension extension = MinionExtensions.getFromSlot(slot).getInstance().getDeclaredConstructor(
                    ItemTypeLinker.class, Object.class
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
                    ItemTypeLinker.class, Object.class
            ).newInstance(null, null);

            extension.fromString(entryJson.getString("serialized"));

            data.setData(slot, extension);
        }
        return data;
    }
}
