package net.swofty.types.generic.gui.inventory.inventories.sbmenu.crafting;

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
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIRecipe extends SkyBlockInventoryGUI {
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};

    SkyBlockItem item;
    SkyBlockInventoryGUI previousGUI;

    public GUIRecipe(ItemType type, SkyBlockInventoryGUI previousGUI) {
        this(new SkyBlockItem(type), previousGUI);
    }

    public GUIRecipe(SkyBlockItem item, SkyBlockInventoryGUI previousGUI) {
        super(item.getAttributeHandler().getItemTypeAsType().getDisplayName() + " Recipe", InventoryType.CHEST_6_ROW);

        this.item = item;
        this.previousGUI = previousGUI;
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
                        "§7Craft this recipe by using a crafting",
                        "§7table."
                );
            }
        });

        List<SkyBlockRecipe<?>> recipes = null;
        try {
            recipes = ((Craftable) item.getGenericInstance()).getRecipes();
        } catch (ClassCastException e2) {
            getPlayer().closeInventory();
            getPlayer().sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }
        SkyBlockRecipe recipe = null;
        if (recipes.size() == 1) {
            recipe = recipes.get(0);
        } else {
            for (SkyBlockRecipe<?> r : recipes) {
                if (r.getResult().toString().equals(item.toString())) {
                    recipe = r;
                    break;
                }
            }
        }

        if (recipe == null) {
            getPlayer().closeInventory();
            getPlayer().sendMessage("§cThis item has no associated crafting recipes!");
            return;
        }

        SkyBlockRecipe finalRecipe = recipe;
        set(new GUIItem(25) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, finalRecipe.getResult().getItemStack());
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
                            if (!(ingredient.getGenericInstance() instanceof Craftable))
                                return;

                            new GUIRecipe(
                                    ((Craftable) ingredient.getGenericInstance()).getRecipes().get(0).getResult().getAttributeHandler().getItemTypeAsType(),
                                    GUIRecipe.this).open(player);
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, ingredient.getItemStack());

                            if (ingredient.getGenericInstance() instanceof Craftable) {
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
