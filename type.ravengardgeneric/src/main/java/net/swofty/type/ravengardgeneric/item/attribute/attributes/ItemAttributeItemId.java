package net.swofty.type.ravengardgeneric.item.attribute.attributes;

import net.minestom.server.tag.Tag;
import net.swofty.type.ravengardgeneric.item.attribute.RavengardItemAttribute;

public class ItemAttributeItemId extends RavengardItemAttribute<String> {
    public static final Tag<String> TAG = Tag.String("id");

    @Override
    public String getKey() {
        return "id";
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
