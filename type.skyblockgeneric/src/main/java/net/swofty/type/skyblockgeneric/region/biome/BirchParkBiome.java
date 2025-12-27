package net.swofty.type.skyblockgeneric.region.biome;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import net.swofty.type.skyblockgeneric.region.SkyBlockBiomeConfiguration;

public class BirchParkBiome extends SkyBlockBiomeConfiguration {

	public BirchParkBiome() {
		super(Key.key("skyblock:birch_park"));
	}

	@Override
	public Biome getBiome() {
		return Biome.builder()
				.effects(
						BiomeEffects.builder()
								.grassColor(TextColor.color(16765696))
								.foliageColor(TextColor.color(16762880))
								.waterColor(TextColor.color(8250111))
								.build()
				)
				.build();
	}

}
