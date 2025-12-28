package net.swofty.type.skyblockgeneric.region.biome;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import net.swofty.type.skyblockgeneric.region.SkyBlockBiomeConfiguration;

public class DarkThicketBiome extends SkyBlockBiomeConfiguration {

	public DarkThicketBiome() {
		super(Key.key("skyblock:dark_thicket"));
	}

	@Override
	public Biome getBiome() {
		return Biome.builder()
				.effects(
						BiomeEffects.builder()
								.grassColor(TextColor.color(4742700))
								.foliageColor(TextColor.color(1206296))
								.waterColor(TextColor.color(2910286))
								.build()
				)
				.build();
	}

}
