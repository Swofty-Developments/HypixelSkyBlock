package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class BackpackComponent extends SkyBlockItemComponent {
    @Getter
    private final int rows;

    public BackpackComponent(int rows, String skullTexture) {
        this.rows = rows;
        addInheritedComponent(new SkullHeadComponent((item) -> skullTexture));
        addInheritedComponent(new InteractableComponent(
                (player, item) -> {
                    player.sendMessage("§cBackpacks cannot be opened on their own.");
                    player.sendMessage("§cInstead, use the §6Storage §cmenu in your §aSkyBlock Menu §cto store backpacks.");
                },
                null,
                null
        ));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
