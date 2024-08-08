package net.swofty.types.generic.item.items.accessories.zombie;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombieArtifact extends SkyBlockValueEvent implements TieredTalisman, DefaultCraftable, SkullHead {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c3fb4e5db97f479c66a42bbd8a7d781daf201a8ddaf77afcf4aef87779aa8b4";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Reduces the damage taken from",
                "§7§7Zombies by §a15%§7.");
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamagedByMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent tempEvent) {
        PlayerDamagedByMobValueUpdateEvent event = (PlayerDamagedByMobValueUpdateEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();

        if (!player.hasTalisman(this)) return;

        if (event.getMob().getEntityType() == EntityType.ZOMBIE) {
            event.setValue((float) (((float) event.getValue()) * 0.85));
        }
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.ENCHANTED_IRON_INGOT, 8));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.ENCHANTED_DIAMOND, 8));
        ingredientMap.put('C', new ItemQuantifiable(ItemTypeLinker.REVENANT_VISCERA, 24));
        ingredientMap.put('D', new ItemQuantifiable(ItemTypeLinker.ZOMBIE_RING, 1));
        List<String> pattern = List.of(
                "ABA",
                "CDC",
                "ABA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemTypeLinker.ZOMBIE_ARTIFACT), ingredientMap, pattern);
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.ZOMBIE_TALISMAN;
    }

    @Override
    public Integer getTier() {
        return 3;
    }
}
