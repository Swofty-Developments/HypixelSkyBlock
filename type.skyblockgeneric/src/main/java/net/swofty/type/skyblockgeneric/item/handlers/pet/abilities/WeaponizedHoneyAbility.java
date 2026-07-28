package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.DamageEventPetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

public class WeaponizedHoneyAbility implements PetAbility, DamageEventPetAbility {

    @Override
    public String getName() {
        return "Weaponized Honey";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        ItemAttributePetData.PetData petData = instance.getAttributeHandler().getPetData();
        var rarity = instance.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);

        return Arrays.asList(
                "§7Gain §a" + decimalify(level * 0.2, 1) + "% §7of received damage as §6❤",
                "§6Absorption"
        );
    }

    @Override
    public void onPlayerDamagedByMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob, double damage) {
        ItemAttributePetData.PetData petData = pet.getAttributeHandler().getPetData();
        int level = petData.getAsLevel(pet.getAttributeHandler().getRarity());

        float absorption = (float) (damage * level * 0.002);
        player.setAdditionalHearts(player.getAdditionalHearts() + absorption);
    }
}
