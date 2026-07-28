package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.BusyBuzzBuzzAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.HiveAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.WeaponizedHoneyAbility;

import java.util.List;

public enum PetType {
    BEE(List.of(
            at(new HiveAbility(), Rarity.COMMON),
            at(new BusyBuzzBuzzAbility(), Rarity.RARE),
            at(new WeaponizedHoneyAbility(), Rarity.LEGENDARY)
    ));

    private final List<AbilityEntry> abilities;

    PetType(List<AbilityEntry> abilities) {
        this.abilities = abilities;
    }

    public List<PetAbility> getAbilities(SkyBlockItem item) {
        var rarity = item.getAttributeHandler().getRarity();
        return abilities.stream()
                .filter(e -> rarity.isAtLeast(e.minimumRarity()))
                .map(AbilityEntry::ability)
                .toList();
    }

    public record AbilityEntry(PetAbility ability, Rarity minimumRarity) {}

    private static AbilityEntry at(PetAbility ability, Rarity rarity) {
        return new AbilityEntry(ability, rarity);
    }
}
