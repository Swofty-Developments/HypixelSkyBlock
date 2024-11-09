package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.crafting.ShapedRecipe;
import net.swofty.types.generic.minion.MinionIngredient;
import net.swofty.types.generic.minion.MinionRecipe;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.item.SkyBlockItemComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MinionComponent extends SkyBlockItemComponent {
    private MinionRegistry minionRegistry;
    private ItemType baseItem;

    public MinionComponent(String minionType, String baseItem,
                           boolean isByDefaultCraftable, List<MinionIngredient> ingredients) {
        this.minionRegistry = MinionRegistry.valueOf(minionType);
        this.baseItem = minionRegistry.getItemType();

        addInheritedComponent(new SkullHeadComponent((item -> {
            int tier = item.getAttributeHandler().getMinionData().tier();

            return minionRegistry.asSkyBlockMinion().getTiers().get(tier - 1).texture();
        })));
        addInheritedComponent(new PlaceEventComponent("minion"));
        addInheritedComponent(new TrackedUniqueComponent());

        if (isByDefaultCraftable) {
            List<SkyBlockRecipe<?>> toReturn = new ArrayList<>();

            getMinionRegistry().asSkyBlockMinion().getTiers().forEach(tier -> {
                if (!tier.craftable()) return; // Skip non-craftable tiers

                List<String> pattern = new ArrayList<>(Arrays.asList(
                        "AAA",
                        "ABA",
                        "AAA"
                ));

                SkyBlockItem item = new SkyBlockItem(getMinionRegistry().getItemType());
                item.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(tier.tier(), 0));

                ShapedRecipe recipe = new ShapedRecipe(
                        SkyBlockRecipe.RecipeType.MINION,
                        item,
                        MinionRecipe.fromNumber(tier.tier() - 1).getRecipeFunction().apply(new MinionRecipe.MinionRecipeData(
                                ingredients,
                                this.baseItem,
                                getMinionRegistry().getItemType()
                        )),
                        pattern
                );
                recipe.addExtraRequirement('B', (minionItem) -> {
                    if (minionItem.hasComponent(MinionComponent.class))
                        return minionItem.getAttributeHandler().getMinionData().tier() == Math.max(1, tier.tier() - 1);
                    return true;
                });

                toReturn.add(recipe);
            });

            addInheritedComponent(new CraftableComponent(toReturn.toArray(new SkyBlockRecipe[0])));
        }
    }
}
