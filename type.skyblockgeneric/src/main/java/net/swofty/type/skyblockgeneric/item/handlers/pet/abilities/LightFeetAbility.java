package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.FallDamageEventPetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

import static net.swofty.commons.StringUtility.commaify;

public class LightFeetAbility implements PetAbility, FallDamageEventPetAbility {

    @Override
    public String getName() {
        return "Light Feet";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);

        return List.of("§7Reduces fall damage by §a" + commaify(level) + "%§7.");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        return ItemStatistics.empty();
    }

    @Override
    public double onPlayerFallDamage(SkyBlockPlayer player, SkyBlockItem pet, double damage) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double reduction = level;
        return damage * (1 - reduction / 100);
    }
}
