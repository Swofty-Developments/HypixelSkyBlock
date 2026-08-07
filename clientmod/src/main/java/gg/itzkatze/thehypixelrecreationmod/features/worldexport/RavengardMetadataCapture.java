package gg.itzkatze.thehypixelrecreationmod.features.worldexport;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Display;

import java.util.Locale;
import java.util.Optional;

public final class RavengardMetadataCapture {
    private static final String PREFIX = "hypixel_ravengard:item/gameplay/";

    private RavengardMetadataCapture() {
    }

    public static Optional<CapturedObject> capture(Display.ItemDisplay display) {
        String model = model(display);
        if (model == null) return Optional.empty();
        if (!model.startsWith(PREFIX)) return Optional.empty();

        Classification classification = classify(model.substring(PREFIX.length()));
        if (classification == null) return Optional.empty();

        CompoundTag data = new CompoundTag();
        data.putString("category", classification.category());
        data.putString("type", classification.type());
        data.putDouble("x", display.getX());
        data.putDouble("y", display.getY());
        data.putDouble("z", display.getZ());
        data.putFloat("yaw", display.getYRot());
        data.putFloat("pitch", display.getXRot());
        return Optional.of(new CapturedObject(data));
    }

    public static boolean isExcluded(Display.ItemDisplay display) {
        String model = model(display);
        return model != null && model.startsWith(PREFIX + "traps/spikes/");
    }

    private static String model(Display.ItemDisplay display) {
        Identifier itemModel = display.getItemStack().get(DataComponents.ITEM_MODEL);
        return itemModel == null ? null : itemModel.toString().toLowerCase(Locale.ROOT);
    }

    private static Classification classify(String path) {
        if (path.startsWith("lootbag") || path.startsWith("lootbags/")) return null;
        if (path.startsWith("trinkets/")) return classified("trinket", leaf(path, "trinkets/"));
        if (path.startsWith("consumables/")) return classified("consumable", leaf(path, "consumables/"));
        if (path.startsWith("doors/")) return classified("door", leaf(path, "doors/"));
        if (path.startsWith("altar/")) return classified("altar", leaf(path, "altar/"));
        if (path.startsWith("traps/")) return null;
        if (path.startsWith("portals/")) {
            String type = leaf(path, "portals/");
            if (type.contains("exit") || type.contains("deeper") || type.contains("fake_light")
                    || type.endsWith("_animated") || path.contains("miniboss/")) return null;
            return classified("revive_portal", type);
        }
        if (path.startsWith("entity/")) return classified("entity_spawn", leaf(path, "entity/"));
        return null;
    }

    private static Classification classified(String category, String type) {
        return type.isBlank() ? null : new Classification(category, type);
    }

    private static String leaf(String path, String prefix) {
        String value = path.substring(prefix.length());
        if (value.endsWith("/model") || value.endsWith("/sprite")) value = value.substring(0, value.lastIndexOf('/'));
        return value.replace('/', '.');
    }

    public record CapturedObject(CompoundTag data) {
    }

    private record Classification(String category, String type) {
    }
}
