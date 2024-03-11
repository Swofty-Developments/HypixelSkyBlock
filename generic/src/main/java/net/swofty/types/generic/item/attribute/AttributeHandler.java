package net.swofty.types.generic.item.attribute;

import net.minestom.server.color.Color;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.*;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AttributeHandler {
    SkyBlockItem item;

    public AttributeHandler(SkyBlockItem item) {
        this.item = item;
    }

    public String getItemType() {
        return ((ItemAttributeType) item.getAttribute("item_type")).getValue();
    }

    public boolean shouldBeEnchanted() {
        if (item.getGenericInstance() == null)
            return false;
        return item.getGenericInstance() instanceof Enchanted;
    }

    public Color getLeatherColour() {
        if (item.getGenericInstance() == null)
            return null;
        if (item.getGenericInstance() instanceof LeatherColour colour)
            return colour.getLeatherColour();
        return null;
    }

    public ItemAttributeGemData.GemData getGemData() {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a gemstone");
        if (item.getGenericInstance() instanceof GemstoneItem) {
            return ((ItemAttributeGemData) item.getAttribute("gems")).getValue();
        } else {
            throw new RuntimeException("Item is not a gemstone");
        }
    }

    public ItemAttributeBackpackData.BackpackData getBackpackData() {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a backpack");
        if (item.getGenericInstance() instanceof Backpack) {
            return ((ItemAttributeBackpackData) item.getAttribute("backpack_data")).getValue();
        } else {
            throw new RuntimeException("Item is not a backpack");
        }
    }

    public void setBackpackData(ItemAttributeBackpackData.BackpackData data) {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a backpack");
        if (item.getGenericInstance() instanceof Backpack) {
            ((ItemAttributeBackpackData) item.getAttribute("backpack_data")).setValue(data);
        } else {
            throw new RuntimeException("Item is not a backpack");
        }
    }

    public @Nullable ItemType getItemTypeAsType() {
        try {
            return ItemType.valueOf(((ItemAttributeType) item.getAttribute("item_type")).getValue());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Rarity getRarity() {
        return ((ItemAttributeRarity) item.getAttribute("rarity")).getValue();
    }

    public String getStackable() {
        return ((ItemAttributeStackable) item.getAttribute("stackable")).getValue();
    }

    public void setStackable(String stackableID) {
        ((ItemAttributeStackable) item.getAttribute("stackable")).setValue(stackableID);
    }

    public void setRarity(Rarity rarity) {
        ((ItemAttributeRarity) item.getAttribute("rarity")).setValue(rarity);
    }

    public boolean hasEnchantment(EnchantmentType type) {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue()
                .enchantments()
                .stream()
                .anyMatch(enchantment -> enchantment.type() == type);
    }

    public void removeEnchantment(EnchantmentType type) {
        ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue()
                .enchantments()
                .removeIf(enchantment -> enchantment.type() == type);
    }

    public @Nullable ReforgeType.Reforge getReforge() {
        return ((ItemAttributeReforge) item.getAttribute("reforge")).getValue();
    }

    public MinionRegistry getMinionType() {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a minion");
        if (item.getGenericInstance() instanceof Minion minion) {
            return minion.getMinionRegistry();
        } else {
            throw new RuntimeException("Item is not a minion");
        }
    }

    public ItemAttributeMinionData.MinionData getMinionData() {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a minion");
        if (item.getGenericInstance() instanceof Minion) {
            return ((ItemAttributeMinionData) item.getAttribute("minion_tier")).getValue();
        } else {
            throw new RuntimeException("Item is not a minion");
        }
    }

    public void setMinionData(ItemAttributeMinionData.MinionData data) {
        if (item.getGenericInstance() == null) throw new RuntimeException("Item is not a minion");
        if (item.getGenericInstance() instanceof Minion) {
            ((ItemAttributeMinionData) item.getAttribute("minion_tier")).setValue(data);
        } else {
            throw new RuntimeException("Item is not a minion");
        }
    }

    public void setReforge(ReforgeType.Reforge reforge) throws IllegalArgumentException {
        if (!item.getAttributeHandler().getRarity().isReforgable())
            throw new IllegalArgumentException("The rarity " + item.getAttributeHandler().getRarity().name() + " is not reforgable.");
        ((ItemAttributeReforge) item.getAttribute("reforge")).setValue(reforge);
    }

    public SkyBlockEnchantment getEnchantment(EnchantmentType type) {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue()
                .enchantments()
                .stream()
                .filter(enchantment -> enchantment.type() == type)
                .findFirst()
                .orElse(null);
    }

    public Stream<SkyBlockEnchantment> getEnchantments() {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue().enchantments().stream();
    }

    public void addEnchantment(SkyBlockEnchantment enchantment) {
        ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue().addEnchantment(enchantment);
    }

    public ItemStatistics getStatistics() {
        return ((ItemAttributeStatistics) item.getAttribute("statistics")).getValue();
    }

    public void setStatistics(ItemStatistics statistics) {
        ((ItemAttributeStatistics) item.getAttribute("statistics")).setValue(statistics);
    }

    public void setRecombobulated(boolean value) {
        ((ItemAttributeRecombobulated) item.getAttribute("recombobulated")).setValue(value);
    }

    public boolean isRecombobulated() {
        return ((ItemAttributeRecombobulated) item.getAttribute("recombobulated")).getValue();
    }

    public int getBreakingPower() {
        return ((ItemAttributeBreakingPower) item.getAttribute("breaking_power")).getValue();
    }

    public boolean isMiningTool() {
        return getBreakingPower() != 0;
    }

    public SkyBlockItem asSkyBlockItem() {
        return item;
    }
}
