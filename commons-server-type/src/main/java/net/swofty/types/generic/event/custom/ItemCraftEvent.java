package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class ItemCraftEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkyBlockItem craftedItem;
    private final SkyBlockRecipe<?> recipe;

    public ItemCraftEvent(SkyBlockPlayer player, SkyBlockItem craftedItem, SkyBlockRecipe<?> recipe) {
        this.player = player;
        this.craftedItem = craftedItem;
        this.recipe = recipe;
    }
}
