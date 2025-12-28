package net.swofty.type.skyblockgeneric.region.biome;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import net.swofty.type.skyblockgeneric.region.SkyBlockBiomeConfiguration;

public class SpruceWoodsBiome extends SkyBlockBiomeConfiguration {

	public SpruceWoodsBiome() {
		super(Key.key("skyblock:spruce_woods"));
	}

	@Override
	public Biome getBiome() {
		return Biome.builder()
				.effects(
						BiomeEffects.builder()
								.grassColor(TextColor.color(16777215))
								.foliageColor(TextColor.color(14417889))
								.waterColor(TextColor.color(11590647))
								.build()
				)
				.build();
	}

}
