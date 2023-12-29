package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.minestom.server.recipe.Recipe;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

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
