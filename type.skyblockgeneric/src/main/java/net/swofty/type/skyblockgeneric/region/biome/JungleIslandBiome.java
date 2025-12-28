package net.swofty.type.skyblockgeneric.region.biome;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import net.swofty.type.skyblockgeneric.region.SkyBlockBiomeConfiguration;

public class JungleIslandBiome extends SkyBlockBiomeConfiguration {

	public JungleIslandBiome() {
		super(Key.key("skyblock:jungle_island"));
	}

	@Override
	public Biome getBiome() {
		return Biome.builder()
				.effects(
						BiomeEffects.builder()
								.waterColor(TextColor.color(-12618012))
								.build()
				)
				.build();
	}

}
