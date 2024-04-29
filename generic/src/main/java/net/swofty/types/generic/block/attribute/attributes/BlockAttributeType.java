package net.swofty.types.generic.block.attribute.attributes;

import net.swofty.types.generic.block.attribute.BlockAttribute;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import org.jetbrains.annotations.Nullable;

public class BlockAttributeType extends BlockAttribute<String> {
    @Override
    public String getKey() {
        return "block_type";
    }

    @Override
    public String getDefaultValue(@Nullable Class<? extends CustomSkyBlockBlock> itemClass) {
        return "N/A";
    }

    @Override
    public String loadFromString(String string) {
        return string;
    }

    @Override
    public String saveIntoString() {
        return this.value;
    }
}
