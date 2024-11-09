package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.ability.AbilityRegistry;
import net.swofty.types.generic.item.handlers.ability.RegisteredAbility;

import java.util.List;
import java.util.Objects;

public class AbilityComponent extends SkyBlockItemComponent {
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
}