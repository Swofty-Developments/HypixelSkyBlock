package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.*;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;

import java.util.List;

public enum PetHandler {
    BEE(List.of(
            at(HiveAbility.create(), Rarity.COMMON),
            at(BusyBuzzBuzzAbility.create(), Rarity.RARE),
            at(HoneyHarvesterAbility.create(), Rarity.LEGENDARY),
            at(PoweredByPollenAbility.create(), Rarity.MYTHIC)
    )),
    GRANDMA_WOLF(List.of(
            at(KillComboAbility.create(), Rarity.COMMON)
    )),
    CHICKEN(List.of(
            at(FreeRangeAbility.create(), Rarity.COMMON),
            at(EggstraLootAbility.create(), Rarity.RARE),
            at(LightFeetAbility.create(), Rarity.LEGENDARY)
    ));

    private final List<AbilityEntry> abilities;

    PetHandler(List<AbilityEntry> abilities) {
        this.abilities = abilities;
    }

    public List<PetAbility> getAbilities(SkyBlockItem item) {
        Rarity rarity = item.getAttributeHandler().getRarity();
        return abilities.stream()
                .filter(e -> rarity.isAtLeast(e.minimumRarity()))
                .map(AbilityEntry::ability)
                .toList();
    }

    public record AbilityEntry(PetAbility ability, Rarity minimumRarity) {
    }

    private static AbilityEntry at(PetAbility ability, Rarity rarity) {
        return new AbilityEntry(ability, rarity);
    }
}
