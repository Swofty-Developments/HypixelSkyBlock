package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.ItemCraftEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.CraftableComponent;
import net.swofty.type.skyblockgeneric.item.components.SellableComponent;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;

public class ActionSkillCarpentryGain implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
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

