package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.fishing.ShipPartDefinition;

@Getter
public class FishingShipPartComponent extends net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent {
    private final String displayName;
    private final ShipPartDefinition.ShipPartSlot slot;
    private final String texture;

    public FishingShipPartComponent(String displayName, ShipPartDefinition.ShipPartSlot slot, String texture) {
        this.displayName = displayName;
        this.slot = slot;
        this.texture = texture;

        addInheritedComponent(new CustomDisplayNameComponent(ignored -> displayName));
        if (texture != null && !texture.isBlank()) {
            addInheritedComponent(new SkullHeadComponent(ignored -> texture));
        }
    }
}