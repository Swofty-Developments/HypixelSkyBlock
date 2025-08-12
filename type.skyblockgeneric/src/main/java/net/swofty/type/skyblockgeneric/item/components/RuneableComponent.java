package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;

@Getter
public class RuneableComponent extends SkyBlockItemComponent {
    private final RuneApplicableTo runeApplicableTo;

    public RuneableComponent(RuneApplicableTo runeApplicableTo) {
        this.runeApplicableTo = runeApplicableTo;

        addInheritedComponent(new ExtraRarityComponent("COSMETIC"));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    public enum RuneApplicableTo {
        WEAPONS,
        BOWS,
        HELMETS,
        CHESTPLATES,
        LEGGINGS,
        BOOTS,
        HOES,
    }
}