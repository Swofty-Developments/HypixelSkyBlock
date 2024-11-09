package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;

public class DecorationHeadComponent extends SkyBlockItemComponent {
    public DecorationHeadComponent(String texture) {
        addInheritedComponent(new SkullHeadComponent((item) -> texture));
        addInheritedComponent(new PlaceableComponent("DECORATION"));
    }
}
