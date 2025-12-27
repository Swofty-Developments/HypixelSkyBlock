package net.swofty.type.skyblockgeneric.region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.minestom.server.world.biome.Biome;

@Getter
@AllArgsConstructor
public abstract class SkyBlockBiomeConfiguration {
	private final Key key;

	public abstract Biome getBiome();
}
