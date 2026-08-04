package net.swofty.type.ravengardgeneric.item.attribute;

import net.minestom.server.item.ItemStack;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;
import net.swofty.type.ravengardgeneric.item.RavengardRarity;
import net.swofty.type.ravengardgeneric.item.attribute.attributes.ItemAttributeItemId;
import net.swofty.type.ravengardgeneric.item.attribute.attributes.ItemAttributeStatBoost;
import net.swofty.type.ravengardgeneric.item.attribute.attributes.ItemAttributeUniqueTrackedId;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RavengardItemAttributeHandler {
    private final ItemStack stack;

    public RavengardItemAttributeHandler(ItemStack stack) {
        this.stack = stack;
    }

    public String getTrackedId() {
        return new ItemAttributeItemId().from(stack);
    }

    public boolean isTracked() {
        return !getTrackedId().isEmpty();
    }

    public @Nullable RavengardRarity getRarity() {
        String tracked = getTrackedId();
        int separator = tracked.indexOf('_');
        if (separator <= 0) {
            return null;
        }
        return RavengardRarity.fromKey(tracked.substring(0, separator));
    }

    public @Nullable String getTypeAsString() {
        String tracked = getTrackedId();
        int separator = tracked.indexOf('_');
        if (separator <= 0 || separator == tracked.length() - 1) {
            return null;
        }
        return tracked.substring(separator + 1);
    }

    public @Nullable RavengardItemType getType() {
        String type = getTypeAsString();
        return type == null ? null : RavengardItemRegistry.get(type);
    }

    public @Nullable UUID getUniqueTrackedId() {
        String value = new ItemAttributeUniqueTrackedId().from(stack);
        if (value.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public double getStatBoost() {
        return new ItemAttributeStatBoost().from(stack);
    }

    public boolean isBoosted() {
        return getStatBoost() > 1.0001;
    }
}
