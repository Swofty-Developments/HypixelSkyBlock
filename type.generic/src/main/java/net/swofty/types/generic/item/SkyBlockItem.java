package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.attribute.ItemAttributeHandler;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeSandboxItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeStatistics;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class SkyBlockItem {
    public List<ItemAttribute> attributes = new ArrayList<>();
    public Class<? extends CustomSkyBlockItem> clazz = null;
    public Object instance = null;
    @Getter
    @Setter
    private int amount = 1;

    @SneakyThrows
    public SkyBlockItem(String itemType, int amount) {
        itemType = itemType.replace("minecraft:", "").toUpperCase();
        Class<? extends CustomSkyBlockItem> clazz = null;

        try {
            clazz = ItemType.valueOf(itemType).clazz;
        } catch (Exception e) {}

        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            attribute.setValue(attribute.getDefaultValue(clazz));
            attributes.add(attribute);
        }

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
            if (clazz != null) {
                statisticsAttribute.setValue(ItemType.valueOf(itemType).clazz.newInstance().getStatistics(this));
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

    public ItemAttribute getAttribute(String key) {
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
        try {
            clazz = ItemType.valueOf(item.getTag(Tag.String("item_type"))).clazz.newInstance().getClass();
        } catch (IllegalArgumentException | InstantiationException | NullPointerException | IllegalAccessException e) {}

        ItemAttribute.getPossibleAttributes().forEach(attribute -> {
            if (item.hasTag(Tag.String(attribute.getKey()))) {
                attribute.setValue(attribute.loadFromString(item.getTag(Tag.String(attribute.getKey()))));
                attributes.add(attribute);
            } else {
                attribute.setValue(attribute.getDefaultValue(clazz));
                attributes.add(attribute);
            }
        });

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        String itemType = typeAttribute.getValue();
        try {
            // All items re-retrieve their base stats when loaded from an itemstack
            ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
            statisticsAttribute.setValue(ItemType.valueOf(itemType).clazz.newInstance().getStatistics(this));
        } catch (IllegalArgumentException | InstantiationException | NullPointerException | IllegalAccessException e) {
        }
    }

    public Object getGenericInstance() {
        if (instance != null)
            return instance;

        try {
            instance = clazz.newInstance();
            return instance;
        } catch (Exception e) {}

        try {
            instance = getAttributeHandler().getItemTypeAsType().clazz.newInstance();
            return instance;
        } catch (Exception e) {}

        return null;
    }

    @Override
    public SkyBlockItem clone() {
        SkyBlockItem item = new SkyBlockItem(getMaterial());
        List<ItemAttribute> attributesForClone = new ArrayList<>();
        item.clazz = clazz;
        item.amount = amount;
        ItemAttribute.getPossibleAttributes().forEach(attribute -> {
            attribute.setValue(getAttribute(attribute.getKey()).getValue());
            attributesForClone.add(attribute);
        });
        item.attributes = attributesForClone;
        return item;
    }

    public Material getMaterial() {
        ItemAttributeSandboxItem.SandboxData data = getAttributeHandler().getSandboxData();
        if (data != null && data.getMaterial() != ItemType.AIR)
            return data.getMaterial().material;

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

    public boolean isSimilar(SkyBlockItem item) {
        boolean allAttributesMatch = true;

        for (ItemAttribute attribute : attributes) {
            // If both are null, skip
            if (attribute.getValue() == null && item.getAttribute(attribute.getKey()).getValue() == null)
                continue;

            if (attribute.getValue() != null && item.getAttribute(attribute.getKey()).getValue() != null) {
                if (!attribute.saveIntoString().equals(item.getAttribute(attribute.getKey()).saveIntoString())) {
                    allAttributesMatch = false;
                    break;
                }
            } else {
                allAttributesMatch = false;
                break;
            }
        }

        boolean sameAmount = item.getAmount() == amount;
        boolean sameMaterial = item.getMaterial() == getMaterial();

        return allAttributesMatch && sameAmount && sameMaterial;
    }

    public ItemAttributeHandler getAttributeHandler() {
        return new ItemAttributeHandler(this);
    }

    public boolean isNA() {
        return getMaterial() == Material.BEDROCK;
    }

    public boolean isAir() {
        return getMaterial() == Material.AIR;
    }

    public static boolean isSkyBlockItem(ItemStack item) {
        return item.hasTag(Tag.String("item_type"));
    }

    @Override
    public String toString() {
        return "SkyBlockItem{" +
                "type=" + getMaterial().name() +
                ", itemType=" + getAttributeHandler().getItemType() +
                ", clazz=" + clazz +
                ", amount=" + amount +
                ", attributes=" + attributes.stream().map(attribute -> attribute.getKey() + "=" + attribute.getValue()).reduce((s, s2) -> s + ", " + s2).orElse("null") +
                '}';
    }

    public String getDisplayName() {
        return StringUtility.getTextFromComponent(new NonPlayerItemUpdater(this).getUpdatedItem().build().getDisplayName());
    }

    public List<String> getLore() {
        return new NonPlayerItemUpdater(this).getUpdatedItem().build().getLore().stream().map(
                StringUtility::getTextFromComponent
        ).toList();
    }
}
