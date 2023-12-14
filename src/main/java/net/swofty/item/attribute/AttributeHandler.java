package net.swofty.item.attribute;

import net.swofty.item.Rarity;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.item.attribute.attributes.ItemAttributeRecombobulated;
import net.swofty.item.attribute.attributes.ItemAttributeStatistics;
import net.swofty.item.attribute.attributes.ItemAttributeType;
import net.swofty.user.statistics.ItemStatistics;

public class AttributeHandler {
    SkyBlockItem item;

    public AttributeHandler(SkyBlockItem item) {
        this.item = item;
    }

    public String getItemType() {
        return ((ItemAttributeType) item.getAttribute("item_type")).getValue();
    }

    public Rarity getRarity() {
        return ((ItemAttributeRarity) item.getAttribute("rarity")).getValue();
    }

    public void setRarity(Rarity rarity) {
        ((ItemAttributeRarity) item.getAttribute("rarity")).setValue(rarity);
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

    public SkyBlockItem asSkyBlockItem() {
        return item;
    }
}
