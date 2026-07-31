package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetDsl;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

public final class HoneyHarvesterAbility {
    private static final double CHANCE_PER_LEVEL = 0.0002;

    private HoneyHarvesterAbility() {
    }

    public static PetAbility create() {
        return PetDsl.ability("Honey Harvester")
                .description(HoneyHarvesterAbility::descriptionFor)
                .onCropHarvested(HoneyHarvesterAbility::onCropHarvested)
                .unimplemented("no game hook for CropHarvested yet")
                .build();
    }

    private static List<String> descriptionFor(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double chance = CHANCE_PER_LEVEL * level;

        return Arrays.asList(
                "§7You have a §a" + decimalify(chance, 3) + "% §7chance to find a",
                "§aHoney Jar §7when farming crops."
        );
    }

    private static void onCropHarvested(PetEvent.CropHarvested event) {
        SkyBlockPlayer player = event.player();
        Rarity rarity = event.pet().getAttributeHandler().getRarity();
        int level = event.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double chance = CHANCE_PER_LEVEL * level;
        if (Math.random() * 100 >= chance) return;

        player.addAndUpdateItem(new SkyBlockItem(ItemType.HONEY_JAR));
    }
}
