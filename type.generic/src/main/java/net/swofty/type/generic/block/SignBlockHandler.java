package net.swofty.type.generic.block;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public final class SignBlockHandler implements BlockHandler {
    public static final Key KEY = Key.key("minecraft:sign");

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return Set.of(
                Tag.Byte("is_waxed"),
                Tag.NBT("front_text"),
                Tag.NBT("back_text"));
    }
}