package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class DecorationHeadComponent extends SkyBlockItemComponent {
    public DecorationHeadComponent(String texture) {
        addInheritedComponent(new SkullHeadComponent((item) -> texture));
        addInheritedComponent(new PlaceableComponent("DECORATION"));
    }
}
