package net.swofty.item;

import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.item.attribute.attributes.ItemAttributeStatistics;
import net.swofty.item.attribute.attributes.ItemAttributeType;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class SkyBlockItem {
    public List<ItemAttribute> attributes = new ArrayList<>();
    public Class<? extends CustomSkyBlockItem> clazz = null;

    public SkyBlockItem(String itemType) {
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
    }

    public Object getAttribute(String key) {
        return attributes.stream().filter(attribute -> attribute.getKey().equals(key)).findFirst().orElse(null);
    }

    public SkyBlockItem(Material material) {
        this(material.name());
    }

    public SkyBlockItem(ItemType type) {
        this(type.name());
    }

    public SkyBlockItem(ItemStack item) {
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
        } catch (IllegalArgumentException | InstantiationException | NullPointerException | IllegalAccessException e) {}
    }

    public ItemStack getItemStack() {
        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        ItemStack itemStack = null;

        try {
            ItemType type = ItemType.valueOf(typeAttribute.getValue());
            itemStack = ItemStack.builder(type.material)
                    .meta(meta -> {
                        meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
                    }).build();
        } catch (IllegalArgumentException e) {
            if (!typeAttribute.getValue().equalsIgnoreCase("N/A")) {
                itemStack = ItemStack.builder(Material.values().stream().
                        filter(material -> material.name().equalsIgnoreCase("minecraft:" + typeAttribute.getValue().toLowerCase()))
                        .findFirst().get())
                        .meta(meta -> {
                            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
                        }).build();
            }
        }

        for (ItemAttribute attribute : attributes) {
            itemStack = itemStack.withTag(Tag.String(attribute.getKey()), attribute.saveIntoString());
        }

        return itemStack;
    }

    public AttributeHandler getAttributeHandler() {
        return new AttributeHandler(this);
    }

    public static boolean isSkyBlockItem(ItemStack item) {
        return item.hasTag(Tag.String("item_type"));
    }
}
