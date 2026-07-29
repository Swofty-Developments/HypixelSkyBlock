package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

public class HoneyHarvesterAbility implements PetAbility {
    private static final double CHANCE_PER_LEVEL = 0.0002;

    @Override
    public String getName() {
        return "Honey Harvester";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double chance = CHANCE_PER_LEVEL * level;

        return Arrays.asList(
                "§7You have a §a" + decimalify(chance, 3) + "% §7chance to find a",
                "§aHoney Jar §7when farming crops."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        // TODO: after adding farming api could implement this.
        return ItemStatistics.empty();
    }
}
