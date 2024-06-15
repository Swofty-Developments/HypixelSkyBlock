package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import net.swofty.commons.item.ItemType;
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
    private Object instance = null;
    @Getter
    @Setter
    private int amount = 1;

    @SneakyThrows
    public SkyBlockItem(ItemTypeLinker typeLinker, int amount) {
        loadAsLinker(typeLinker);
        setAmount(amount);
    }

    public SkyBlockItem(UnderstandableSkyBlockItem item) {
        if (item.itemKey() == null) {
            loadAsMaterial(item.material());
        } else {
            ItemTypeLinker linker = ItemTypeLinker.fromType(item.itemKey());
            if (linker != null) {
                loadAsLinker(linker);
            } else {
                loadAsItemType(item.itemKey());
            }
        }
    }

    public SkyBlockItem(Material material) {
        loadAsMaterial(material);
    }

    public SkyBlockItem(ItemTypeLinker type) {
        this(type, 1);
    }

    public SkyBlockItem(ItemType type, int amount) {
        ItemTypeLinker linker = ItemTypeLinker.fromType(type);
        if (linker != null) {
            loadAsLinker(linker);
        } else {
            loadAsItemType(type);
        }
        setAmount(amount);
    }

    public SkyBlockItem(ItemType type) {
        ItemTypeLinker linker = ItemTypeLinker.fromType(type);
        if (linker != null) {
            loadAsLinker(linker);
        } else {
            loadAsItemType(type);
        }
    }

    public SkyBlockItem(String type) {
        try {
            ItemType itemType = ItemType.valueOf(type);
            ItemTypeLinker linker = ItemTypeLinker.fromType(itemType);
            if (linker != null) {
                loadAsLinker(linker);
            } else {
                loadAsItemType(itemType);
            }
        } catch (IllegalArgumentException e) {
            Material material = Material.fromNamespaceId(type.toLowerCase());
            loadAsMaterial(material);
        }
    }

    public SkyBlockItem(ItemStack item) {
        loadAsStack(item);
        setAmount(item.amount());
    }

    @SneakyThrows
    private void loadAsLinker(ItemTypeLinker linker) {
        clazz = linker.clazz;
        ItemStatistics statistics = clazz.newInstance().getStatistics(null);

        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            attribute.setValue(attribute.getDefaultValue(statistics));
            attributes.add(attribute);
        }

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        typeAttribute.setValue(linker.name());

        ItemAttributeRarity rarityAttribute = (ItemAttributeRarity) getAttribute("rarity");
        try {
            rarityAttribute.setValue(linker.type.rarity);
        } catch (IllegalArgumentException e) {
            rarityAttribute.setValue(Rarity.COMMON);
        }

        ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
        statisticsAttribute.setValue(linker.clazz.newInstance().getStatistics(this));
    }

    private void loadAsItemType(ItemType type) {
        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            attribute.setValue(attribute.getDefaultValue(null));
            attributes.add(attribute);
        }

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        typeAttribute.setValue(type.name());

        ItemAttributeRarity rarityAttribute = (ItemAttributeRarity) getAttribute("rarity");
        try {
            rarityAttribute.setValue(type.rarity);
        } catch (IllegalArgumentException e) {
            rarityAttribute.setValue(Rarity.COMMON);
        }
    }

    private void loadAsMaterial(Material material) {
        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            attribute.setValue(attribute.getDefaultValue(null));
            attributes.add(attribute);
        }

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        typeAttribute.setValue(material.namespace().asString());
    }

    @SneakyThrows
    private void loadAsStack(ItemStack item) {
        if (item.material() == Material.AIR) {
            loadAsMaterial(Material.AIR);
            return;
        }
        String itemType = item.getTag(Tag.String("item_type"));
        ItemStatistics statistics = ItemStatistics.empty();

        ItemType type = ItemType.get(itemType);
        ItemTypeLinker linker = null;
        if (type != null) {
            linker = ItemTypeLinker.fromType(type);
            if (linker != null) {
                loadAsLinker(linker);
                statistics = linker.clazz.newInstance().getStatistics(null);
            } else {
                loadAsItemType(type);
            }
        } else {
            Material material = item.material();
            loadAsMaterial(material);
        }

        for (ItemAttribute attribute : ItemAttribute.getPossibleAttributes()) {
            if (item.hasTag(Tag.String(attribute.getKey()))) {
                attribute.setValue(attribute.loadFromString(item.getTag(Tag.String(attribute.getKey()))));
                attributes.removeIf(a -> a.getKey().equals(attribute.getKey()));
                attributes.add(attribute);
            } else {
                attribute.setValue(attribute.getDefaultValue(statistics));
                attributes.removeIf(a -> a.getKey().equals(attribute.getKey()));
                attributes.add(attribute);
            }
        }

        if (linker != null) {
            // All items re-retrieve their base stats when loaded from an itemstack
            ItemAttributeStatistics statisticsAttribute = (ItemAttributeStatistics) getAttribute("statistics");
            statisticsAttribute.setValue(linker.clazz.newInstance().getStatistics(this));
        }
    }

    public ItemAttribute getAttribute(String key) {
        return attributes.stream().filter(attribute -> attribute.getKey().equals(key)).findFirst().orElse(null);
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
        if (data != null && data.getMaterial() != ItemType.AIR)
            return data.getMaterial().material;

        ItemAttributeType typeAttribute = (ItemAttributeType) getAttribute("item_type");
        try {
            return ItemType.valueOf(typeAttribute.getValue()).material;
        } catch (IllegalArgumentException e) {
            if (typeAttribute.getValue().equalsIgnoreCase("N/A"))
                return Material.BEDROCK;
            return Material.fromNamespaceId(typeAttribute.getValue());
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
                getAttributeHandler().getPotentialType(),
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
                ", itemType=" + getAttributeHandler().getTypeAsString() +
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
