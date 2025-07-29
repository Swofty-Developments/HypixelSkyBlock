package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.ItemCraftEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.CraftableComponent;
import net.swofty.types.generic.item.components.SellableComponent;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;

public class ActionSkillCarpentryGain implements SkyBlockEventClass {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(ItemCraftEvent event) {
        SkyBlockPlayer player = event.getPlayer();
        if (!player.getMissionData().hasCompleted("give_wool_to_carpenter")) return;

        SkyBlockItem craftedItem = event.getCraftedItem();

        if (craftedItem == null || craftedItem.isAir() || craftedItem.isNA()) return;
        if (ItemType.fromMaterial(craftedItem.getMaterial()) != null &&
                ItemType.fromMaterial(craftedItem.getMaterial()) == craftedItem.getAttributeHandler().getPotentialType()) return;

        Map<String, Integer> rawItems = new HashMap<>();
        double npcSellValue = 0;

        collectRawItems(craftedItem, craftedItem.getAmount(), rawItems);

        for (Map.Entry<String, Integer> entry : rawItems.entrySet()) {
            String itemId = entry.getKey();
            int amount = entry.getValue();

            SkyBlockItem resolvedItem = new SkyBlockItem(ItemType.get(itemId));
            if (resolvedItem.hasComponent(SellableComponent.class)) {
                double sellPrice = resolvedItem.getComponent(SellableComponent.class).getSellValue();
                npcSellValue += sellPrice * amount;
            }
        }

        double carpentryXP = 0.03 * npcSellValue;

        player.getSkills().increase(player, SkillCategories.CARPENTRY, carpentryXP);
    }

    private void collectRawItems(SkyBlockItem item, int multiplier, Map<String, Integer> rawItems) {
        if (!item.hasComponent(CraftableComponent.class)) {
            String key = item.getAttributeHandler().getTypeAsString();
            rawItems.merge(key, multiplier, Integer::sum);
            return;
        }

        SkyBlockRecipe<?> recipe = item.getComponent(CraftableComponent.class).getRecipes().getFirst();
        SkyBlockItem[] ingredients = recipe.getRecipeDisplay();

        int recipeResultAmount = recipe.getResult().getAmount();
        double scaleFactor = (double) multiplier / recipeResultAmount;

        for (SkyBlockItem ingredient : ingredients) {
            if (ingredient == null || ingredient.isAir() || ingredient.isNA()) continue;

            int totalAmount = (int) Math.ceil(ingredient.getAmount() * scaleFactor);
            collectRawItems(ingredient, totalAmount, rawItems);
        }
    }
}

