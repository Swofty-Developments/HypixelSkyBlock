package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.commons.item.attribute.attributes.ItemAttributeSandboxItem;
import net.swofty.commons.item.attribute.attributes.ItemAttributeStatistics;
import net.swofty.commons.item.attribute.attributes.ItemAttributeType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.commons.StringUtility;

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

        ItemStatistics statistics = null;
        try {
            clazz = ItemTypeLinker.valueOf(itemType).clazz;
            statistics = clazz.newInstance().getStatistics(null);
        } catch (Exception e) {}

        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            attribute.setValue(attribute.getDefaultValue(statistics));
            attributes.add(attribute);
        }

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        typeAttribute.setValue(itemType);

        ItemAttributeRarity rarityAttribute = (ItemAttributeRarity) getAttribute("rarity");
        try {
            rarityAttribute.setValue(ItemTypeLinker.valueOf(itemType).rarity);
        } catch (IllegalArgumentException e) {
            rarityAttribute.setValue(Rarity.COMMON);
        }

        ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
        try {
            if (clazz != null) {
                statisticsAttribute.setValue(ItemTypeLinker.valueOf(itemType).clazz.newInstance().getStatistics(this));
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

    public SkyBlockItem(UnderstandableSkyBlockItem item) {
        this(item.itemKey(), item.amount(), item.material());
        attributes = item.attributes();
    }

    public SkyBlockItem(Material material) {
        this(material.name(), 1);
    }

    public SkyBlockItem(ItemTypeLinker type) {
        this(type.name(), 1);
    }

    public SkyBlockItem(ItemTypeLinker type, int amount) {
        this(type.name(), amount);
    }

    public SkyBlockItem(ItemStack item) {
        amount = item.amount();
        try {
            clazz = ItemTypeLinker.valueOf(item.getTag(Tag.String("item_type"))).clazz.newInstance().getClass();
        } catch (IllegalArgumentException | InstantiationException | NullPointerException | IllegalAccessException e) {}

        EntityType.AREA_EFFECT_CLOUD

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
            statisticsAttribute.setValue(ItemTypeLinker.valueOf(itemType).clazz.newInstance().getStatistics(this));
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
            instance = getAttributeHandler().getPotentialClassLinker().clazz.newInstance();
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
        if (data != null && data.getMaterial() != ItemTypeLinker.AIR)
            return data.getMaterial().material;

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        try {
            return ItemTypeLinker.valueOf(typeAttribute.getValue()).material;
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

        itemStackBuilder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        return itemStackBuilder;
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

    public UnderstandableSkyBlockItem toUnderstandable() {
        return new UnderstandableSkyBlockItem(
                getAttributeHandler().getItemType(),
                attributes, amount, getMaterial()
        );
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
        return StringUtility.getTextFromComponent(new NonPlayerItemUpdater(this).getUpdatedItem().build().get(ItemComponent.CUSTOM_NAME));
    }

    public List<String> getLore() {
        return new NonPlayerItemUpdater(this).getUpdatedItem().build().get(ItemComponent.LORE).stream().map(
                StringUtility::getTextFromComponent
        ).toList();
    }
}
