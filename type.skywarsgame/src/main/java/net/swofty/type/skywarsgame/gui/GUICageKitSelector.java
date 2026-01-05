package net.swofty.type.skywarsgame.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsUnlocks;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarslobby.gui.GUIKitBreakdown;
import net.swofty.type.skywarslobby.gui.GUIKitPurchaseConfirm;
import net.swofty.type.skywarslobby.gui.GUISelectNormalPerks;
import net.swofty.type.skywarslobby.gui.GUIToggleInsanePerks;
import net.swofty.type.skywarslobby.kit.SkywarsKit;
import net.swofty.type.skywarslobby.kit.SkywarsKitRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUICageKitSelector extends HypixelInventoryGUI {
    private static final int[] KIT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };
    private static final int KITS_PER_PAGE = KIT_SLOTS.length;
    private static final String RANDOM_KIT_TEXTURE = "fa72d7d2195a51fbe546e7348a6b75de1197bce21ea1dec6227df1f2ed8b73";

    private final String mode;
    private final SkywarsGame game;
    private final int page;
    private final boolean lowestFirst;
    private final boolean ownedFirst;

    public GUICageKitSelector(String mode, SkywarsGame game) {
        this(mode, game, 1, true, false);
    }

    public GUICageKitSelector(String mode, SkywarsGame game, int page, boolean lowestFirst, boolean ownedFirst) {
        super("Kit Selector", InventoryType.CHEST_6_ROW);
        this.mode = mode;
        this.game = game;
        this.page = page;
        this.lowestFirst = lowestFirst;
        this.ownedFirst = ownedFirst;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        if (handler == null) return;

        DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                SkywarsDataHandler.Data.UNLOCKS,
                DatapointSkywarsUnlocks.class
        ).getValue();

        long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

        List<SkywarsKit> allKits = SkywarsKitRegistry.getKitsSortedByRarity(
                mode, lowestFirst, ownedFirst, unlocks.getUnlockedKits()
        );
        int totalPages = (int) Math.ceil((double) allKits.size() / KITS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        int startIndex = (page - 1) * KITS_PER_PAGE;
        int endIndex = Math.min(startIndex + KITS_PER_PAGE, allKits.size());

        String modeName = mode.equals("NORMAL") ? "Normal" : "Insane";
        List<String> selectedPerks = unlocks.getSelectedPerksForMode(mode);
        boolean hasEmptySlot = selectedPerks == null || selectedPerks.size() < 6;

        set(new GUIClickableItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Selection of unique perks for " + modeName);
                lore.add("§7games.");
                lore.add("");
                if (hasEmptySlot) {
                    int emptySlots = 6 - (selectedPerks == null ? 0 : selectedPerks.size());
                    lore.add("§cYou have " + emptySlots + " empty Perk Slot" + (emptySlots > 1 ? "s" : "") + " in this");
                    lore.add("§cmode!");
                    lore.add("");
                }
                lore.add("§eClick to browse!");

                return ItemStackCreator.getStack(
                        "§aSelect " + modeName + " Perks",
                        Material.CAULDRON,
                        1,
                        lore
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (mode.equals("NORMAL")) {
                    new GUISelectNormalPerks().open(player);
                } else {
                    new GUIToggleInsanePerks().open(player);
                }
            }
        });

        for (int i = 0; i < KIT_SLOTS.length; i++) {
            int kitIndex = startIndex + i;
            int slot = KIT_SLOTS[i];

            if (kitIndex < endIndex) {
                SkywarsKit kit = allKits.get(kitIndex);
                set(createKitItem(kit, slot, player, unlocks, coins));
            }
        }

        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String currentSort = lowestFirst ? "Lowest rarity first" : "Highest rarity first";
                String nextSort = lowestFirst ? "Highest rarity first" : "Lowest rarity first";
                String ownedFirstStatus = ownedFirst ? "§aYes" : "§cNo";

                return ItemStackCreator.getStack(
                        "§6Sorted by: §a" + currentSort,
                        Material.HOPPER,
                        1,
                        "§7Sorts by rarity: " + currentSort,
                        "",
                        "§7Next sort: §a" + nextSort,
                        "§eLeft click to use!",
                        "",
                        "§7Owned items first: " + ownedFirstStatus,
                        "§eRight click to toggle!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                boolean isRightClick = e.getClick() instanceof Click.Right;
                if (isRightClick) {
                    new GUICageKitSelector(mode, game, 1, lowestFirst, !ownedFirst).open(player);
                } else {
                    new GUICageKitSelector(mode, game, 1, !lowestFirst, ownedFirst).open(player);
                }
            }
        });

        set(new GUIClickableItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSkyWars Challenges §7(Right Click)",
                        Material.BLAZE_POWDER,
                        1,
                        "§7View your active SkyWars Challenges",
                        "§7for this game. Win games with",
                        "§7different Challenges activated to",
                        "§7show off your skills and earn",
                        "§7bragging rights!",
                        "",
                        "§eClick to open!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cSkyWars Challenges coming soon!");
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§eRandom Kit",
                        RANDOM_KIT_TEXTURE,
                        1,
                        "§7Chooses a random kit that you own!",
                        "",
                        "§eClick to select!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
                if (handler == null) return;

                DatapointSkywarsUnlocks.SkywarsUnlocks currentUnlocks = handler.get(
                        SkywarsDataHandler.Data.UNLOCKS,
                        DatapointSkywarsUnlocks.class
                ).getValue();

                List<SkywarsKit> ownedKits = SkywarsKitRegistry.getKitsForMode(mode).stream()
                        .filter(kit -> currentUnlocks.hasKit(kit.getId()))
                        .toList();

                if (ownedKits.isEmpty()) {
                    player.sendMessage("§cYou don't own any kits for this mode!");
                    return;
                }

                SkywarsKit randomKit = ownedKits.get(new Random().nextInt(ownedKits.size()));
                currentUnlocks.selectKitForMode(mode, randomKit.getId());
                player.sendMessage("§aRandomly selected the §e" + randomKit.getName() + " §akit!");

                new GUICageKitSelector(mode, game, page, lowestFirst, ownedFirst).open(player);
            }
        });

        if (page > 1) {
            int prevPage = page - 1;
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aPrevious Page",
                            Material.ARROW,
                            1,
                            "§ePage " + prevPage
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUICageKitSelector(mode, game, prevPage, lowestFirst, ownedFirst).open(player);
                }
            });
        }

        int finalTotalPages = totalPages;
        if (page < totalPages) {
            int nextPage = page + 1;
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aNext Page",
                            Material.ARROW,
                            1,
                            "§ePage " + nextPage
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUICageKitSelector(mode, game, nextPage, lowestFirst, ownedFirst).open(player);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    private GUIClickableItem createKitItem(SkywarsKit kit, int slot, HypixelPlayer player,
                                           DatapointSkywarsUnlocks.SkywarsUnlocks unlocks, long coins) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean owned = unlocks.hasKit(kit.getId());
                boolean selected = unlocks.getSelectedKitForMode(mode).equals(kit.getId());

                List<String> lore = new ArrayList<>();
                lore.addAll(kit.getItemsLore(mode));

                if (selected) {
                    lore.add("§aSELECTED");
                } else if (owned) {
                    lore.add("§eClick to select!");
                } else {
                    lore.add("§cNot unlocked yet!");
                }

                String nameColor = owned ? "§a" : "§c";
                String name = nameColor + kit.getName();

                if (!owned) {
                    return ItemStackCreator.getStack(name, Material.GUNPOWDER, 1, lore);
                } else if (kit.hasCustomTexture()) {
                    return ItemStackCreator.getStackHead(name, kit.getIconTexture(), 1, lore);
                } else {
                    return ItemStackCreator.getStack(name, kit.getIconMaterial(), 1, lore);
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
                if (handler == null) return;

                DatapointSkywarsUnlocks.SkywarsUnlocks currentUnlocks = handler.get(
                        SkywarsDataHandler.Data.UNLOCKS,
                        DatapointSkywarsUnlocks.class
                ).getValue();

                boolean owned = currentUnlocks.hasKit(kit.getId());
                boolean isRightClick = e.getClick() instanceof Click.Right;

                if (isRightClick) {
                    new GUIKitBreakdown(kit, mode).open(player);
                    return;
                }

                if (owned) {
                    currentUnlocks.selectKitForMode(mode, kit.getId());
                    player.sendMessage("§aYou selected the §e" + kit.getName() + " §akit for " + mode + " mode!");
                    new GUICageKitSelector(mode, game, page, lowestFirst, ownedFirst).open(player);
                } else if (kit.costsCoin()) {
                    long currentCoins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    if (currentCoins >= kit.getCost()) {
                        new GUIKitPurchaseConfirm(kit, mode, page).open(player);
                    } else {
                        player.sendMessage("§cYou don't have enough coins to purchase this kit!");
                    }
                } else {
                    player.sendMessage("§cThis kit cannot be purchased with coins.");
                }
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
