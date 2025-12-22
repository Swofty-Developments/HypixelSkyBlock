package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AnvilCombinableComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIAnvil extends HypixelInventoryGUI {

    private static final int[] BORDER_SLOTS = {45, 46, 47, 48, 50, 51, 52, 53};
    private static final int[] UPGRADE_INDICATOR_SLOTS = {11, 12, 20};
    private static final int[] SACRIFICE_INDICATOR_SLOTS = {14, 15, 24};

    private static final int UPGRADE_ITEM_SLOT = 29;
    private static final int SACRIFICE_ITEM_SLOT = 33;
    private static final int RESULT_SLOT = 13;
    private static final int COMBINE_BUTTON_SLOT = 22;

    private SkyBlockItem upgradeItem = null;
    private SkyBlockItem sacrificeItem = null;

    public GUIAnvil() {
        super("Anvil", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        fill(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE, ""), 45, 53);

        set(GUIClickableItem.getCloseItem(49));

        setupCombineButton();
        setupResultSlot();
        setupUpgradeSlot();
        setupSacrificeSlot();

        updateIndicators();
        updateItemStacks(getInventory(), getPlayer());
    }

    private void setupCombineButton() {
        set(new GUIItem(COMBINE_BUTTON_SLOT) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1,
                        "§7Combine the items in the slots to the",
                        "§7left and right below.");
            }
        });
    }

    private void setupResultSlot() {
        set(new GUIItem(RESULT_SLOT) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§cAnvil", Material.BARRIER, 1,
                        "§7Place a target item in the left slot",
                        "§7and a sacrifice item in the right slot",
                        "§7to combine them!");
            }
        });
    }

    private void setupUpgradeSlot() {
        if (upgradeItem == null) {
            set(new GUIClickableItem(UPGRADE_ITEM_SLOT) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockItem cursorItem = new SkyBlockItem(p.getInventory().getCursorItem());
                    if (cursorItem.isNA() || cursorItem.isAir()) return;

                    e.setCancelled(false);
                }

                @Override
                public void runPost(InventoryClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                    ItemStack slotItem = e.getInventory().getItemStack(UPGRADE_ITEM_SLOT);
                    if (slotItem.isAir()) {
                        upgradeItem = null;
                    } else {
                        upgradeItem = new SkyBlockItem(slotItem);
                    }

                    giveResultIfPresent(player);
                    updateCraftingState();
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStack.builder(Material.AIR);
                }
            });
        } else {
            set(new GUIClickableItem(UPGRADE_ITEM_SLOT) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return PlayerItemUpdater.playerUpdate(player, upgradeItem.getItemStack());
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStack cursorItem = p.getInventory().getCursorItem();

                    if (cursorItem.isAir()) {
                        e.setCancelled(false);
                    } else {
                        SkyBlockItem newItem = new SkyBlockItem(cursorItem);
                        if (!newItem.isNA() && !newItem.isAir()) {
                            e.setCancelled(false);
                        }
                    }
                }

                @Override
                public void runPost(InventoryClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                    ItemStack slotItem = e.getInventory().getItemStack(UPGRADE_ITEM_SLOT);
                    if (slotItem.isAir()) {
                        upgradeItem = null;
                    } else {
                        upgradeItem = new SkyBlockItem(slotItem);
                    }

                    giveResultIfPresent(player);
                    updateCraftingState();
                }

                @Override
                public boolean canPickup() {
                    return true;
                }
            });
        }
    }

    private void setupSacrificeSlot() {
        if (sacrificeItem == null) {
            set(new GUIClickableItem(SACRIFICE_ITEM_SLOT) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockItem cursorItem = new SkyBlockItem(p.getInventory().getCursorItem());
                    if (cursorItem.isNA() || cursorItem.isAir()) return;

                    e.setCancelled(false);
                }

                @Override
                public void runPost(InventoryClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                    ItemStack slotItem = e.getInventory().getItemStack(SACRIFICE_ITEM_SLOT);
                    if (slotItem.isAir()) {
                        sacrificeItem = null;
                    } else {
                        sacrificeItem = new SkyBlockItem(slotItem);
                    }

                    giveResultIfPresent(player);
                    updateCraftingState();
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStack.builder(Material.AIR);
                }
            });
        } else {
            set(new GUIClickableItem(SACRIFICE_ITEM_SLOT) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return PlayerItemUpdater.playerUpdate(player, sacrificeItem.getItemStack());
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStack cursorItem = p.getInventory().getCursorItem();

                    if (cursorItem.isAir()) {
                        e.setCancelled(false);
                    } else {
                        SkyBlockItem newItem = new SkyBlockItem(cursorItem);
                        if (!newItem.isNA() && !newItem.isAir()) {
                            e.setCancelled(false);
                        }
                    }
                }

                @Override
                public void runPost(InventoryClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;

                    ItemStack slotItem = e.getInventory().getItemStack(SACRIFICE_ITEM_SLOT);
                    if (slotItem.isAir()) {
                        sacrificeItem = null;
                    } else {
                        sacrificeItem = new SkyBlockItem(slotItem);
                    }

                    giveResultIfPresent(player);
                    updateCraftingState();
                }

                @Override
                public boolean canPickup() {
                    return true;
                }
            });
        }
    }

    private void updateCraftingState() {
        boolean isUpgradeValid = upgradeItem != null && !upgradeItem.isAir() && !upgradeItem.isNA();
        boolean isSacrificeValid = sacrificeItem != null && !sacrificeItem.isAir() && !sacrificeItem.isNA();

        boolean canCraft = isUpgradeValid && isSacrificeValid
                && sacrificeItem.hasComponent(AnvilCombinableComponent.class)
                && sacrificeItem.getComponent(AnvilCombinableComponent.class)
                .canApply((SkyBlockPlayer) getPlayer(), upgradeItem, sacrificeItem);

        updateIndicators(isUpgradeValid, isSacrificeValid, canCraft);
        updateBorder(canCraft);
        updateResultPreview(isUpgradeValid, isSacrificeValid, canCraft);
        updateCombineButton(canCraft);

        setupUpgradeSlot();
        setupSacrificeSlot();

        updateItemStacks(getInventory(), getPlayer());
    }

    private void updateIndicators() {
        updateIndicators(false, false, false);
    }

    private void updateIndicators(boolean isUpgradeValid, boolean isSacrificeValid, boolean canCraft) {
        Material upgradeMaterial = (canCraft || (isUpgradeValid && !isSacrificeValid))
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE;

        Material sacrificeMaterial = (canCraft || (isSacrificeValid && !isUpgradeValid))
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE;

        ItemStack.Builder upgradeIndicator = ItemStackCreator.getStack("§6Item to Upgrade", upgradeMaterial, 1,
                "§7The item you want to upgrade should",
                "§7be placed in the slot on this side.");

        ItemStack.Builder sacrificeIndicator = ItemStackCreator.getStack("§6Item to Sacrifice", sacrificeMaterial, 1,
                "§7The item you are sacrificing in order",
                "§7to upgrade the item on the left",
                "§7should be placed in the slot on this",
                "§7side.");

        for (int slot : UPGRADE_INDICATOR_SLOTS) {
            set(slot, upgradeIndicator);
        }
        for (int slot : SACRIFICE_INDICATOR_SLOTS) {
            set(slot, sacrificeIndicator);
        }
    }

    private void updateBorder(boolean canCraft) {
        Material borderMaterial = canCraft ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        ItemStack.Builder borderItem = ItemStackCreator.createNamedItemStack(borderMaterial);

        for (int slot : BORDER_SLOTS) {
            set(slot, borderItem);
        }
    }

    private void updateResultPreview(boolean isUpgradeValid, boolean isSacrificeValid, boolean canCraft) {
        if (!canCraft) {
            if (isUpgradeValid && isSacrificeValid) {
                set(new GUIItem(RESULT_SLOT) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return ItemStackCreator.getStack("§cError!", Material.BARRIER, 1,
                                "§7You can not combine those Items");
                    }
                });
            } else {
                set(new GUIItem(RESULT_SLOT) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return ItemStackCreator.getStack("§cAnvil", Material.BARRIER, 1,
                                "§7Place a target item in the left slot",
                                "§7and a sacrifice item in the right slot",
                                "§7to combine them!");
                    }
                });
            }
            return;
        }

        SkyBlockItem resultPreview = new SkyBlockItem(upgradeItem.getItemStack());
        sacrificeItem.getComponent(AnvilCombinableComponent.class).apply(resultPreview, sacrificeItem);

        set(new GUIItem(RESULT_SLOT) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, resultPreview.getItemStack());
            }
        });
    }

    private void updateCombineButton(boolean canCraft) {
        if (!canCraft) {
            set(new GUIItem(COMBINE_BUTTON_SLOT) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1,
                            "§7Combine the items in the slots to the",
                            "§7left and right below.");
                }
            });
            return;
        }

        int levelCost = sacrificeItem.getComponent(AnvilCombinableComponent.class)
                .applyCostLevels(upgradeItem, sacrificeItem, (SkyBlockPlayer) getPlayer());

        List<String> lore = new ArrayList<>();
        lore.add("§7Combine the items in the slots to the");
        lore.add("§7left and right below.");

        if (levelCost > 0) {
            lore.add("");
            lore.add("§7Cost");
            lore.add("§9" + levelCost + " Exp Levels");
        }

        lore.add("");
        lore.add("§eClick to combine!");

        set(new GUIClickableItem(COMBINE_BUTTON_SLOT) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                performCraft(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1, lore);
            }
        });
    }

    private void performCraft(SkyBlockPlayer player) {
        if (upgradeItem == null || sacrificeItem == null) return;
        if (!sacrificeItem.hasComponent(AnvilCombinableComponent.class)) return;

        AnvilCombinableComponent component = sacrificeItem.getComponent(AnvilCombinableComponent.class);

        if (!component.canApply(player, upgradeItem, sacrificeItem)) return;

        int requiredLevels = component.applyCostLevels(upgradeItem, sacrificeItem, player);

        if (player.getLevel() < requiredLevels) {
            player.sendMessage("§cYou don't have enough Experience Levels!");
            return;
        }

        // Check onCraft (e.g., Bits cost) before deducting anything
        if (!component.onCraft(player, upgradeItem, sacrificeItem)) {
            return; // onCraft returned false, cancel the craft
        }

        player.setLevel(player.getLevel() - requiredLevels);

        SkyBlockItem result = new SkyBlockItem(upgradeItem.getItemStack());
        component.apply(result, sacrificeItem);

        upgradeItem = null;
        sacrificeItem = null;

        getInventory().setItemStack(UPGRADE_ITEM_SLOT, ItemStack.AIR);
        getInventory().setItemStack(SACRIFICE_ITEM_SLOT, ItemStack.AIR);

        setupResultClaimable(result);
        setupUpgradeSlot();
        setupSacrificeSlot();

        set(new GUIItem(COMBINE_BUTTON_SLOT) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aAnvil", Material.OAK_SIGN, 1,
                        "§7Claim the result item above!");
            }
        });

        updateIndicators();
        updateBorder(false);
        updateItemStacks(getInventory(), getPlayer());
    }

    private void setupResultClaimable(SkyBlockItem result) {
        set(new GUIClickableItem(RESULT_SLOT) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.addAndUpdateItem(result);
                // Clear result slot before opening new GUI to prevent duplicate on close
                getInventory().setItemStack(RESULT_SLOT, ItemStack.AIR);
                new GUIAnvil().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, result.getItemStack());
            }
        });
    }

    private void giveResultIfPresent(SkyBlockPlayer player) {
        if (get(RESULT_SLOT) instanceof GUIClickableItem) {
            ItemStack resultStack = getInventory().getItemStack(RESULT_SLOT);
            if (!resultStack.isAir()) {
                player.addAndUpdateItem(new SkyBlockItem(resultStack));
                setupResultSlot();
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        returnItemsToPlayer(player, (Inventory) e.getInventory());
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        returnItemsToPlayer(player, inventory);
    }

    private void returnItemsToPlayer(SkyBlockPlayer player, Inventory inventory) {
        ItemStack upgradeStack = inventory.getItemStack(UPGRADE_ITEM_SLOT);
        if (!upgradeStack.isAir()) {
            player.addAndUpdateItem(new SkyBlockItem(upgradeStack));
        }

        ItemStack sacrificeStack = inventory.getItemStack(SACRIFICE_ITEM_SLOT);
        if (!sacrificeStack.isAir()) {
            player.addAndUpdateItem(new SkyBlockItem(sacrificeStack));
        }

        if (get(RESULT_SLOT) instanceof GUIClickableItem) {
            ItemStack resultStack = inventory.getItemStack(RESULT_SLOT);
            if (!resultStack.isAir()) {
                player.addAndUpdateItem(new SkyBlockItem(resultStack));
            }
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}