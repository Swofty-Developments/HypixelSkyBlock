package net.swofty.types.generic.block.attribute;

import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BlockAttribute<T> {
    private static final ArrayList<BlockAttribute> attributes = new ArrayList<>();

    public T value;

    public BlockAttribute() {
        value = getDefaultValue(null);
    }

    public abstract String getKey();

    public abstract T getDefaultValue(@Nullable Class<? extends CustomSkyBlockBlock> blockClass);

    public abstract T loadFromString(String string);

    public abstract String saveIntoString();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void registerBlockAttributes() {
        SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.types.generic.block.attribute.attributes", BlockAttribute.class
        ).forEach(attributes::add);
    }

    public static Collection<BlockAttribute> getPossibleAttributes() {
        return (Collection<BlockAttribute>) attributes.stream().map(attributeClass -> {
            try {
                return attributeClass.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
