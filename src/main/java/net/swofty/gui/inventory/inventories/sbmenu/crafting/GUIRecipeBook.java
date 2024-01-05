package net.swofty.gui.inventory.inventories.sbmenu.crafting;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.impl.recipes.ShapedRecipe;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIRecipeBook extends SkyBlockInventoryGUI {
    private final int[] borderSlots = {
            20, 21, 22, 23, 24,
            30, 31, 32, 33,
    };

    public GUIRecipeBook() {
        super("Recipe Book", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7Through your adventure, you will",
                        "§7unlock recipes for all kinds of",
                        "§7special items! You can view how to",
                        "§7craft these items here.",
                        " "
                ));

                SkyBlockRecipe.getMissionDisplay(lore, player.getUuid());

                return ItemStackCreator.getStack("§aRecipe Book", Material.BOOK, (short) 0, 1, lore.toArray(new String[0]));
            }
        });

        Arrays.stream(borderSlots).forEach(slot -> {
            SkyBlockRecipe.RecipeType tempType;
            try {
                tempType = SkyBlockRecipe.RecipeType.values()[slot - 20];
            } catch (ArrayIndexOutOfBoundsException exception) {
                tempType = SkyBlockRecipe.RecipeType.values()[slot - 25];
            }
            SkyBlockRecipe.RecipeType type = tempType;

            ArrayList<SkyBlockRecipe> typeRecipes = new ArrayList<>();
            ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
            allRecipes.forEach(recipe -> {
                if (recipe.getRecipeType() == type) {
                    typeRecipes.add(recipe);
                }
            });

            set(new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipeCategory(type).open(player);
                }

                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§7View all of the " + StringUtility.toNormalCase(type.name()) + " Recipes that",
                            "§7you have unlocked!", " "));

                    typeRecipes.forEach(recipe -> {
                        SkyBlockRecipe.CraftingResult result =
                                (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(player);

                        if (result.allowed()) {
                            allowedRecipes.add(recipe);
                        }
                    });

                    String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) typeRecipes.size()) * 100);
                    lore.add("§7Recipes Unlocked: §e" + unlockedPercentage + "§6%");

                    String baseLoadingBar = "─────────────────";
                    int maxBarLength = baseLoadingBar.length();
                    int completedLength = (int) ((allowedRecipes.size() / (double) typeRecipes.size()) * maxBarLength);

                    String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
                    int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
                    String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                            completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                            maxBarLength
                    ));

                    lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + typeRecipes.size());
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(type.name()) + " Recipe",
                            type.getMaterial(), (short) 0, 1, lore);
                }
            });
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
