package net.swofty.commons.skyblock.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

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
