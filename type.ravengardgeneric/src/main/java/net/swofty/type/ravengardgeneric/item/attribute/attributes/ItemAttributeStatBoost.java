package net.swofty.type.ravengardgeneric.item.attribute.attributes;

import net.minestom.server.tag.Tag;
import net.swofty.type.ravengardgeneric.item.attribute.RavengardItemAttribute;

public class ItemAttributeStatBoost extends RavengardItemAttribute<Double> {
    public static final Tag<Double> TAG = Tag.Double("stat_boost");

    @Override
    public String getKey() {
        return "stat_boost";
    }

    @Override
    public Tag<Double> getTag() {
        return TAG;
    }

    @Override
    public Double getDefaultValue() {
        return 1.0;
    }
}
