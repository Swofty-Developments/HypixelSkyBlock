package net.swofty.type.dwarvenmines.gui;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ChatColor;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeGemData;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gems.Gemstone;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.GemstoneComponent;
import net.swofty.type.skyblockgeneric.item.components.GemstoneImplComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGemstoneGrinder extends HypixelInventoryGUI {
    private final static int[] PRE_SLOTS = {28, 29, 30, 31, 32, 33, 34};
    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            0, new int[]{},
            1, new int[]{31},
            2, new int[]{30, 32},
            3, new int[]{30, 31, 32},
            4, new int[]{29, 30, 32, 33},
            5, new int[]{29, 30, 31, 32, 33}
    ));
    private SkyBlockItem item = null;

    public GUIGemstoneGrinder() {
        super("Gemstone Grinder", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGemstoneGuide().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGemstone Guide",
                        Material.REDSTONE_TORCH,
                        1,
                        "§7Many items can have §dGemstones",
                        "§7applied to them. Gemstones increase",
                        "§7the stats of an item based on the",
                        "§7type of Gemstone used.",
                        "",
                        "§7There are several §aqualities §7of",
                        "§7Gemstones, ranging from §fRough §7to",
                        "§6Perfect§7. The higher the quality, the",
                        "§7better the stat!",
                        "",
                        "§7This guide shows the items that can",
                        "§7have Gemstones applied to them.",
                        "",
                        "§eClick to view!"
                );
            }
        });

        updateFromItem(null);
    }

    public void updateFromItem(SkyBlockItem item) {
        this.item = item;

        if (item == null) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStack stack = player.getInventory().getCursorItem();
                    SkyBlockItem item = new SkyBlockItem(stack);

                    if (stack.get(DataComponents.CUSTOM_NAME) == null) return;
                    if (!item.hasComponent(GemstoneComponent.class)) {
                        player.sendMessage("§cOnly items that can have Gemstones applied to them can be put in the Grinder!");
                        return;
                    }

                    e.setCancelled(true);
                    player.getInventory().setCursorItem(ItemStack.AIR);
                    updateFromItem(item);
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

            for (int slot : PRE_SLOTS) {
                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack(
                                "§dGemstone Slot", Material.GRAY_STAINED_GLASS_PANE, 1,
                                "§7Place an item above to apply",
                                "§7Gemstones to it!"
                        );
                    }
                });
            }

            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        for (int slot : PRE_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return FILLER_ITEM;
                }
            });
        }

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack stack = player.getInventory().getCursorItem();

                if (stack == ItemStack.AIR) {
                    e.setCancelled(true);
                    player.getInventory().setCursorItem(PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
                    updateFromItem(null);
                }
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
            }
        });

        List<GemstoneComponent.GemstoneSlot> gemstoneSlots = item.getComponent(GemstoneComponent.class).getSlots();
        int[] slotsToPlaceGems = SLOTS.get(gemstoneSlots.size());
        int index = 0;
        for (GemstoneComponent.GemstoneSlot gemstoneSlot : gemstoneSlots) {
            int slot = slotsToPlaceGems[index];
            ItemAttributeGemData.GemData gemData = item.getAttributeHandler().getGemData();
            Gemstone.Slots gemSlot = gemstoneSlot.slot();

            if (gemData.isSlotUnlocked(index)) {
                if (gemData.getGem(index).filledWith != null) {
                    SkyBlockItem appliedGem = new SkyBlockItem(gemData.getGem(index).filledWith);

                    int finalIndex = index;
                    set(new GUIClickableItem(slot) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            int costToRemove = appliedGem.getComponent(GemstoneImplComponent.class).getGemRarity().costToRemove;
                            if (player.getCoins() >= costToRemove) {
                                player.removeCoins(costToRemove);
                                player.addAndUpdateItem(appliedGem);
                                gemData.removeGem(finalIndex);

                                updateFromItem(item);
                            } else {
                                player.sendMessage("§cYou don't have enough coins to remove this!");
                            }
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;

                            ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                                    player, appliedGem.getItemStack()
                            );
                            ArrayList<String> lore = new ArrayList<>(appliedGem.getLore());

                            lore.add("");
                            lore.add("§7Cost to Remove");
                            lore.add("§6" + StringUtility.commaify(appliedGem.getComponent(GemstoneImplComponent.class).getGemRarity().costToRemove) + " Coins");
                            lore.add("");
                            lore.add("§eClick to remove!");

                            return ItemStackCreator.updateLore(itemStack, lore);
                        }
                    });
                } else {
                    set(new GUIItem(slot) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer player) {
                            String title = gemSlot.getColor() + gemSlot.getSymbol() + " " + gemSlot.getName() + " Gemstone Slot";
                            List<String> lore = new ArrayList<>();

                            if (gemSlot.getValidGemstones().size() > 1) { // Universal Slot
                                lore.add("§7Click §aany Gemstone §7of any quality in");
                                lore.add("§7your inventory to apply it to this item!");
                                lore.add("");
                                lore.add("§7Applicable Gemstones");
                                for (Gemstone gemstone : gemSlot.getValidGemstones()) {
                                    lore.add(gemstone.getColor() + StringUtility.toNormalCase(gemstone.name()) + " Gemstone");
                                }
                            } else { // Specific Gem Slot
                                Gemstone gemstone = gemSlot.getValidGemstones().getFirst();
                                lore.add("§7Click a " + gemstone.getColor() + StringUtility.toNormalCase(gemstone.name()) + " Gemstone §7of any");
                                lore.add("§7quality in your inventory to apply it");
                                lore.add("§7to this item!");
                            }

                            return ItemStackCreator.getStack(title, gemSlot.paneColor, 1, lore);
                        }
                    });
                }
            } else {
                int finalI = index;
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;

                        int coins = Math.max(gemstoneSlot.unlockPrice(), 0);

                        List<GemstoneComponent.ItemRequirement> requirements = gemstoneSlot.itemRequirements();
                        Map<ItemType, Integer> itemRequirements = new HashMap<>();

                        for (GemstoneComponent.ItemRequirement requirement : requirements) {
                            itemRequirements.put(requirement.itemId(), requirement.amount());
                        }

                        if (player.getCoins() < coins) {
                            player.sendMessage("§cYou don't have the required items!");
                            return;
                        }

                        if (!player.removeItemsFromPlayer(itemRequirements)) {
                            player.sendMessage("§cYou don't have the required items!");
                            return;
                        }

                        player.removeCoins(coins);
                        gemData.unlockSlot(finalI);

                        updateFromItem(item);
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        String title = gemSlot.getColor() + gemSlot.getSymbol() + " " + gemSlot.getName() + " Gemstone Slot";

                        List<String> lore = new ArrayList<>();
                        lore.add("§7This slot is locked! Purchasing this");
                        lore.add("§7slot allows you to apply a");
                        lore.add(gemSlot.getColor() + gemSlot.getSymbol() + " " + gemSlot.getName() + " Gemstone §7to it!");
                        lore.add("");

                        if (gemSlot.getValidGemstones().size() > 1) {
                            for (Gemstone gemstone : gemSlot.getValidGemstones()) {
                                lore.add(gemstone.getColor() + StringUtility.toNormalCase(gemstone.name()) + " Gemstone");
                            }
                            lore.add("");
                        }

                        if (!gemstoneSlot.itemRequirements().isEmpty() || gemstoneSlot.unlockPrice() > 0) {
                            lore.add("§7Cost");
                            if (gemstoneSlot.unlockPrice() > 0) {
                                lore.add(ChatColor.GOLD + StringUtility.commaify(gemstoneSlot.unlockPrice()) + " Coins");
                            }
                            if (!gemstoneSlot.itemRequirements().isEmpty()) {
                                for (GemstoneComponent.ItemRequirement requirement : gemstoneSlot.itemRequirements()) {
                                    Gemstone.Slots slots = Gemstone.Slots.getFromGemstone(Gemstone.getFromItemType(requirement.itemId()));
                                    SkyBlockItem skyBlockItem = new SkyBlockItem(requirement.itemId());
                                    lore.add(skyBlockItem.getComponent(GemstoneImplComponent.class).getGemRarity().getRarity().getColor() +
                                            slots.getSymbol() + " " + skyBlockItem.getDisplayName() + " §8x" + requirement.amount());
                                }
                            }
                        }

                        lore.add("");
                        lore.add("§eClick to unlock!");

                        return ItemStackCreator.getStack(title, Material.GRAY_STAINED_GLASS_PANE, 1, lore);
                    }
                });
            }

            index++;
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        if (item == null) return;

        if (!clickedItem.hasComponent(GemstoneImplComponent.class)) {
            player.sendMessage("§cYou cannot apply that to this item!");
            e.setCancelled(true);
            return;
        }
        ItemAttributeGemData.GemData gemData = item.getAttributeHandler().getGemData();

        List<GemstoneComponent.GemstoneSlot> itemSlots = item.getComponent(GemstoneComponent.class).getSlots();
        int index = 0;
        for (GemstoneComponent.GemstoneSlot slot : itemSlots) {
            ItemAttributeGemData.GemData.GemSlots gemSlot = gemData.getGem(index);
            index++;
            if (gemSlot.filledWith != null) continue;

            List<ItemType> allowedGems = new ArrayList<>();
            for (Gemstone gemstone : slot.slot().getValidGemstones()) {
                allowedGems.addAll(gemstone.item);
            }

            if (gemSlot.isUnlocked() && allowedGems.contains(clickedItem.getAttributeHandler().getPotentialType())) {
                gemSlot.setFilledWith(clickedItem.getAttributeHandler().getPotentialType());
                player.getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
                updateFromItem(item);
                break;
            }
        }
        e.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        if (item != null) player.addAndUpdateItem(item);
    }
}
