package net.swofty.type.generic.block;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Set;

public class PlayerHeadBlockHandler implements BlockHandler {
	public static final Key KEY = Key.key("minecraft:skull");

	@Override
	public @NonNull Key getKey() {
		return KEY;
	}

	@Override
	public @NonNull Collection<Tag<?>> getBlockEntityTags() {
		return Set.of(Tag.NBT("profile"));
	}
}
