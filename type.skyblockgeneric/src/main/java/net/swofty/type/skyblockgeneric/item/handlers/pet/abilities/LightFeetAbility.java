package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetDsl;
import net.swofty.type.skyblockgeneric.item.handlers.pet.dsl.PetEvent;

import java.util.List;

import static net.swofty.commons.StringUtility.commaify;

public final class LightFeetAbility {
    private LightFeetAbility() {
    }

    public static PetAbility create() {
        return PetDsl.ability("Light Feet")
                .description(LightFeetAbility::descriptionFor)
                .on(PetEvent.FallDamage.class, context -> context.damage(
                        context.damage() * (1 - reductionFor(context.pet()) / 100)))
                .build();
    }

    private static List<String> descriptionFor(SkyBlockItem instance) {
        return List.of("§7Reduces fall damage by §a" + commaify(reductionFor(instance)) + "%§7.");
    }

    private static double reductionFor(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        return pet.getAttributeHandler().getPetData().getAsLevel(rarity);
    }
}
