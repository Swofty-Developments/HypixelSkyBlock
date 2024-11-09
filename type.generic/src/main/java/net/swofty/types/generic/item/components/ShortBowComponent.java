package net.swofty.types.generic.item.components;

import lombok.Getter;

@Getter
public class ShortBowComponent extends BowComponent {
    private final float cooldown;

    public ShortBowComponent(float cooldown, String bowHandlerId) {
        super(bowHandlerId);

        this.cooldown = cooldown;
    }
}