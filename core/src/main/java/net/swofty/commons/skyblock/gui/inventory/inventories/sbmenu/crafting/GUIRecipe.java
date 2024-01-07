package net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu.crafting;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.gui.inventory.ItemStackCreator;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.commons.skyblock.gui.inventory.item.GUIClickableItem;
import net.swofty.commons.skyblock.gui.inventory.item.GUIItem;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.Craftable;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.item.updater.PlayerItemUpdater;

import java.util.ArrayList;

public class GUIRecipe extends SkyBlockInventoryGUI {
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};

    ItemType type;
    SkyBlockInventoryGUI previousGUI;

    public GUIRecipe(ItemType type, SkyBlockInventoryGUI previousGUI) {
        super(type.getDisplayName() + " Recipe", InventoryType.CHEST_6_ROW);

        this.type = type;
        this.previousGUI = previousGUI;
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        if (previousGUI != null)
            set(GUIClickableItem.getGoBackItem(48, previousGUI));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aCrafting Table", Material.CRAFTING_TABLE, 1,
                        "§7Craft this recipe by using a crafting",
                        "§7table."
                );
            }
        });

        SkyBlockRecipe recipe = ((Craftable) type.clazz.newInstance()).getRecipe();

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 25;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, null, recipe.getResult().getItemStack());
            }
        });

        SkyBlockItem[] ingredients = recipe.getRecipeDisplay();
        int slot = 0;

        for (int craftSlot : CRAFT_SLOTS) {
            if (slot < ingredients.length) {
                SkyBlockItem ingredient = ingredients[slot];
                if (ingredient != null) {
                    set(new GUIClickableItem() {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                            if (!(ingredient.getGenericInstance() instanceof Craftable))
                                return;

                            new GUIRecipe(
                                    ((Craftable) ingredient.getGenericInstance()).getRecipe().getResult().getAttributeHandler().getItemTypeAsType(),
                                    GUIRecipe.this).open(player);
                        }

                        @Override
                        public int getSlot() {
                            return craftSlot;
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            ItemStack.Builder builder = PlayerItemUpdater.playerUpdate(player, null, ingredient.getItemStack());

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
