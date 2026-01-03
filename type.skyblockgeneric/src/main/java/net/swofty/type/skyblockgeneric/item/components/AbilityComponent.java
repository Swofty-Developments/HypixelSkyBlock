package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.ability.AbilityRegistry;
import net.swofty.type.skyblockgeneric.item.handlers.ability.RegisteredAbility;

import java.util.List;
import java.util.Objects;

public class AbilityComponent extends SkyBlockItemComponent {
    @Getter
    private final List<String> abilityIds;

    public AbilityComponent(List<String> abilityIds) {
        this.abilityIds = abilityIds;
    }

    public List<RegisteredAbility> getAbilities() {
        return abilityIds.stream()
                .map(AbilityRegistry::getAbility)
                .filter(Objects::nonNull)
                .toList();
    }

    public RegisteredAbility getAbility(RegisteredAbility.AbilityActivation type) {
        return getAbilities().stream()
                .filter(ability -> ability.getActivation() == type)
                .findFirst()
                .orElse(null);
    }
}