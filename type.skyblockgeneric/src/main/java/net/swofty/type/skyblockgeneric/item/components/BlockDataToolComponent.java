package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class BlockDataToolComponent extends SkyBlockItemComponent {

    public BlockDataToolComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }

}
