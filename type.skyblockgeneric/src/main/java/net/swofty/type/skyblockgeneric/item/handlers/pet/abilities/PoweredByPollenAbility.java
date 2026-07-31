package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetDsl;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

public final class PoweredByPollenAbility {
    private static final double PER_LEVEL = 1.6;

    private PoweredByPollenAbility() {
    }

    public static PetAbility create() {
        return PetDsl.ability("Powered by Pollen")
                .description(PoweredByPollenAbility::descriptionFor)
                .statistics(context -> statisticsFor(context.player(), context.pet()))
                .unimplemented("The Garden region check not implemented")
                .build();
    }

    private static List<String> descriptionFor(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double fortune = PER_LEVEL * level;

        return List.of(
                "§7Grants §6+" + decimalify(fortune, 1) + "☘ Sunflower§7,",
                "§6Moonflower§7, and §6Wild Rose Fortune",
                "§7while in §aThe Garden§7."
        );
    }

    private static ItemStatistics statisticsFor(SkyBlockPlayer player, SkyBlockItem pet) {
        // TODO: Implement region check — only grant fortune while player is in The Garden
        return ItemStatistics.empty();
    }
}
