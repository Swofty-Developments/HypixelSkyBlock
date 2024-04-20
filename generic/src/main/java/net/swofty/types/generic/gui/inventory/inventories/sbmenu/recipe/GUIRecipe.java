package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIRecipe extends SkyBlockInventoryGUI {
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};

    SkyBlockItem item;
    SkyBlockInventoryGUI previousGUI;
    int recipeIndex;

    public GUIRecipe(ItemType type, SkyBlockInventoryGUI previousGUI) {
        this(new SkyBlockItem(type), previousGUI, 0);
    }

    public GUIRecipe(SkyBlockItem item, SkyBlockInventoryGUI previousGUI) {
        this(item, previousGUI, 0);
    }

    public GUIRecipe(SkyBlockItem item, SkyBlockInventoryGUI previousGUI, int recipeIndex) {
        super(item.getAttributeHandler().getItemTypeAsType().getDisplayName(item) + " Recipe", InventoryType.CHEST_6_ROW);

        this.item = item;
        this.previousGUI = previousGUI;
        this.recipeIndex = recipeIndex;
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        if (previousGUI != null)
            set(GUIClickableItem.getGoBackItem(48, previousGUI));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aCrafting Table", Material.CRAFTING_TABLE, 1,
                        "§7Craft this recipe by using a",
                        "§7crafting table."
                );
            }
        });

        ItemType itemType = item.getAttributeHandler().getItemTypeAsType();
        if (itemType == null) {
            getPlayer().closeInventory();
            getPlayer().sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }
        List<SkyBlockRecipe<?>> recipes = SkyBlockRecipe.getFromType(itemType);
        if (recipes.isEmpty()) {
            getPlayer().closeInventory();
            getPlayer().sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }

        if (recipeIndex >= recipes.size())
            recipeIndex = 0;
        SkyBlockRecipe recipe = recipes.get(recipeIndex);

        if (recipes.size() > recipeIndex + 1) {
            set(new GUIClickableItem(32) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(
                            item,
                            GUIRecipe.this,
                            recipeIndex + 1
                    ).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aNext Recipe", Material.ARROW, 1,
                            "§7Click to view the next recipe!"
                    );
                }
            });
        }
        if (recipeIndex > 0) {
            set(new GUIClickableItem(14) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(
                            item,
                            GUIRecipe.this,
                            recipeIndex - 1
                    ).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aPrevious Recipe", Material.ARROW, 1,
                            "§7Click to view the previous recipe!"
                    );
                }
            });
        }

        set(new GUIItem(25) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, recipe.getResult().getItemStack())
                        .amount(recipe.getResult().getAmount());
            }
        });

        SkyBlockItem[] ingredients = recipe.getRecipeDisplay();
        int slot = 0;

        for (int craftSlot : CRAFT_SLOTS) {
            if (slot < ingredients.length) {
                SkyBlockItem ingredient = ingredients[slot];
                if (ingredient != null) {
                    set(new GUIClickableItem(craftSlot) {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                            if (!(ingredient.getGenericInstance() instanceof DefaultCraftable))
                                return;

                            new GUIRecipe(
                                    ingredient,
                                    GUIRecipe.this,
                                    0
                            ).open(player);
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, ingredient.getItemStack());

                            if (ingredient.getGenericInstance() instanceof DefaultCraftable) {
                                ArrayList<Component> lore = new ArrayList<>(builder.build().getLore());
                                lore.add(Component.text(" "));
                                lore.add(Component.text("§eClick to view recipe!"));
                                builder.lore(lore);
                            }

                            if (ingredient.getAttributeHandler().shouldBeEnchanted())
                                ItemStackCreator.enchant(builder);

                            return builder;
                        }
                    });
                } else {
                    set(craftSlot, ItemStack.builder(Material.AIR));
                }
            }
            slot++;
        }

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
