package net.swofty.commons.skyblock.item;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.skyblock.item.attribute.AttributeHandler;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeBreakingPower;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeStatistics;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeType;
import net.swofty.commons.skyblock.item.impl.CustomSkyBlockItem;
import net.swofty.commons.skyblock.item.impl.MiningTool;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class SkyBlockItem {
    public List<ItemAttribute> attributes = new ArrayList<>();
    public Class<? extends CustomSkyBlockItem> clazz = null;
    @Getter
    @Setter
    private int amount = 1;

    public SkyBlockItem(String itemType, int amount) {
        itemType = itemType.replace("minecraft:", "").toUpperCase();

        ItemAttribute.getPossibleAttributes().forEach(attribute -> {
            attribute.setValue(attribute.getDefaultValue());
            attributes.add(attribute);
        });

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        typeAttribute.setValue(itemType);

        ItemAttributeRarity rarityAttribute = (ItemAttributeRarity) getAttribute("rarity");
        try {
            rarityAttribute.setValue(ItemType.valueOf(itemType).rarity);
        } catch (IllegalArgumentException e) {
            rarityAttribute.setValue(Rarity.COMMON);
        }

        ItemAttributeBreakingPower breakingPower = (ItemAttributeBreakingPower) getAttribute("breaking_power");
        try {
            MiningTool t = (MiningTool) ItemType.valueOf(itemType).clazz.newInstance();
            breakingPower.setValue(t.getBreakingPower());
        } catch (ClassCastException castEx) {
            breakingPower.setValue(0);

            // Any other exception must be ignored.
        } catch (Exception ignored) {
        }

        ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
        try {
            Class<? extends CustomSkyBlockItem> clazz = ItemType.valueOf(itemType).clazz;
            if (clazz != null) {
                statisticsAttribute.setValue(ItemType.valueOf(itemType).clazz.newInstance().getStatistics());
                clazz = ItemType.valueOf(itemType).clazz.newInstance().getClass();
            } else {
                statisticsAttribute.setValue(ItemStatistics.builder().build());
            }
        } catch (IllegalArgumentException e) {
            statisticsAttribute.setValue(ItemStatistics.builder().build());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        setAmount(amount);
    }

    public Object getAttribute(String key) {
        return attributes.stream().filter(attribute -> attribute.getKey().equals(key)).findFirst().orElse(null);
    }

    public SkyBlockItem(Material material) {
        this(material.name(), 1);
    }

    public SkyBlockItem(ItemType type) {
        this(type.name(), 1);
    }

    public SkyBlockItem(ItemType type, int amount) {
        this(type.name(), amount);
    }

    public SkyBlockItem(ItemStack item) {
        amount = item.getAmount();

        ItemAttribute.getPossibleAttributes().forEach(attribute -> {
            if (item.hasTag(Tag.String(attribute.getKey()))) {
                attribute.setValue(attribute.loadFromString(item.getTag(Tag.String(attribute.getKey()))));
                attributes.add(attribute);
            } else {
                attribute.setValue(attribute.getDefaultValue());
                attributes.add(attribute);
            }
        });

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        String itemType = typeAttribute.getValue();
        try {
            clazz = ItemType.valueOf(itemType).clazz.newInstance().getClass();

            // All items re-retrieve their base stats when loaded from an itemstack
            ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
            statisticsAttribute.setValue(ItemType.valueOf(itemType).clazz.newInstance().getStatistics());
        } catch (IllegalArgumentException | InstantiationException | NullPointerException | IllegalAccessException e) {
        }
    }

    public Object getGenericInstance() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
        }

        try {
            return getAttributeHandler().getItemTypeAsType().clazz.newInstance();
        } catch (Exception e) {
        }

        return null;
    }

    public Material getMaterial() {
        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        try {
            return ItemType.valueOf(typeAttribute.getValue()).material;
        } catch (IllegalArgumentException e) {
            if (typeAttribute.getValue().equalsIgnoreCase("N/A"))
                return Material.BEDROCK;
            return Material.values().stream().
                    filter(material -> material.name().equalsIgnoreCase("minecraft:" + typeAttribute.getValue().toLowerCase()))
                    .findFirst().get();
        }
    }

    public ItemStack getItemStack() {
        return getItemStackBuilder().build();
    }

    public ItemStack.Builder getItemStackBuilder() {
        ItemStack.Builder itemStackBuilder = ItemStack.builder(getMaterial()).amount(amount);

        for (ItemAttribute attribute : attributes) {
            itemStackBuilder.setTag(Tag.String(attribute.getKey()), attribute.saveIntoString());
        }

        return itemStackBuilder.meta(meta -> meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES));
    }

    public AttributeHandler getAttributeHandler() {
        return new AttributeHandler(this);
    }

    public static boolean isSkyBlockItem(ItemStack item) {
        return item.hasTag(Tag.String("item_type"));
    }

    @Override
    public String toString() {
        return "SkyBlockItem{" +
                "type=" + getMaterial().name() +
                ", clazz=" + clazz +
                ", amount=" + amount +
                '}';
    }
}
