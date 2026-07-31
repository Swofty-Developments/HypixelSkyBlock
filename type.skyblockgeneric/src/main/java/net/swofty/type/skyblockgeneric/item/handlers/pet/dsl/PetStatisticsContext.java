package net.swofty.type.skyblockgeneric.item.handlers.pet.dsl;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public record PetStatisticsContext(SkyBlockPlayer player, SkyBlockItem pet) {
}
