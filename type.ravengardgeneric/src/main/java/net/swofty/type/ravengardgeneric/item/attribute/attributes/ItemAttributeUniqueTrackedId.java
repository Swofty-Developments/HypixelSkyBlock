package net.swofty.type.ravengardgeneric.item.attribute.attributes;

import net.minestom.server.tag.Tag;
import net.swofty.type.ravengardgeneric.item.attribute.RavengardItemAttribute;

public class ItemAttributeUniqueTrackedId extends RavengardItemAttribute<String> {
    public static final Tag<String> TAG = Tag.String("uuid");

    @Override
    public String getKey() {
        return "uuid";
    }

    @Override
    public Tag<String> getTag() {
        return TAG;
    }

    @Override
    public String getDefaultValue() {
        return "";
    }
}
