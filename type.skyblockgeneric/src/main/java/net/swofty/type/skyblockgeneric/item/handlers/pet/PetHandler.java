package net.swofty.type.skyblockgeneric.item.handlers.pet;

import lombok.Builder;
import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItem;

import java.util.List;
import java.util.function.Function;

@Getter
@Builder
public class PetHandler {
    private final Function<SkyBlockItem, List<PetAbility>> abilitiesProvider;

    public List<PetAbility> getAbilities(SkyBlockItem item) {
        return abilitiesProvider.apply(item);
    }
}