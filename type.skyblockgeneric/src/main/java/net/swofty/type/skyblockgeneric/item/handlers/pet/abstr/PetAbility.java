package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(SkyBlockItem instance);

    ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet);

    default void onEvent(PetEvent event) {
    }
}