package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
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
import net.swofty.type.skywarslobby.kit.SkywarsKit;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI showing detailed information about a specific kit, including prestige levels and stats.
 */
public class GUIKitBreakdown extends HypixelInventoryGUI {
    private static final int[] PRESTIGE_THRESHOLDS = {0, 1000, 2500, 5000, 10000, 15000, 20000, 30000};
    private static final String[] PRESTIGE_NAMES = {"None", "I", "II", "III", "IV", "V", "VI", "VII"};

    private final SkywarsKit kit;
    private final String mode;

    public GUIKitBreakdown(SkywarsKit kit, String mode) {
        super(kit.getName() + " Kit", InventoryType.CHEST_6_ROW);
        this.kit = kit;
        this.mode = mode;
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
        boolean owned = unlocks.hasKit(kit.getId());
        boolean isFavorite = unlocks.isFavorite(kit.getId());
        int kitXP = unlocks.getKitXP(kit.getId());
        int prestigeLevel = unlocks.getKitPrestigeLevel(kit.getId());

        // Kit info display (slot 4)
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Rarity: " + kit.getRarity().getFormattedName());
                lore.add("");
                lore.add("§7Starting Items (" + mode + "):");
                lore.addAll(kit.getItemsLore(mode));

                if (kit.getSpecialAbility() != null && !kit.getSpecialAbility().isEmpty()) {
                    lore.add("");
                    lore.add("§6Special: §e" + kit.getSpecialAbility());
                }

                lore.add("");
                if (owned) {
                    lore.add("§a§lOWNED");
                    lore.add("§7Kit XP: §e" + kitXP);
                    lore.add("§7Prestige: §d" + PRESTIGE_NAMES[prestigeLevel]);
                } else {
                    lore.add("§c§lNOT OWNED");
                    lore.add("§7Cost: " + kit.getFormattedCost());
                }

                String name = (owned ? "§a" : "§c") + kit.getName() + " Kit";
                if (kit.hasCustomTexture()) {
                    return ItemStackCreator.getStackHead(name, kit.getIconTexture(), 1, lore);
                } else {
                    return ItemStackCreator.getStack(name, kit.getIconMaterial(), 1, lore);
                }
            }
        });

        // Prestige levels (slots 10-16)
        for (int i = 0; i < 7; i++) {
            final int level = i + 1;
            final int slot = 10 + i;
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    boolean unlocked = prestigeLevel >= level;
                    boolean isNextLevel = prestigeLevel == level - 1;
                    int xpRequired = PRESTIGE_THRESHOLDS[level];
                    int xpForPrevious = level > 1 ? PRESTIGE_THRESHOLDS[level - 1] : 0;

                    List<String> lore = new ArrayList<>();
                    lore.add("§7Earn §dSkyWars XP §7with this kit to prestige it.");
                    lore.add("");

                    // Progress bar
                    int progress = Math.max(0, kitXP - xpForPrevious);
                    int needed = xpRequired - xpForPrevious;
                    double percent = owned ? Math.min(100.0, (progress * 100.0) / needed) : 0;
                    int filled = (int) (percent / 10);

                    lore.add("§7Progress: §e" + (int) percent + "§6%");
                    lore.add("§7§m                              §e " + String.format("%,d", owned ? progress : 0) + "§6/§e" + String.format("%,d", needed) + " XP");
                    lore.add("");

                    // Rewards based on level
                    lore.add("§7Rewards:");
                    switch (level) {
                        case 1 -> {
                            lore.add(" §8+§650,000 §7SkyWars Coins");
                            lore.add(" §8+§fSilver §7Particle Trail");
                        }
                        case 2 -> {
                            lore.add(" §8+§6100,000 §7SkyWars Coins");
                            lore.add(" §8+§2Green §7Particle Trail");
                        }
                        case 3 -> {
                            lore.add(" §8+§6250,000 §7SkyWars Coins");
                            lore.add(" §8+§9Blue §7Particle Trail");
                        }
                        case 4 -> {
                            lore.add(" §8+§91 §7Opal");
                            lore.add(" §8+§5Purple §7Particle Trail");
                        }
                        case 5 -> {
                            lore.add(" §8+§91 §7Opal");
                            lore.add(" §8+§6Gold §7Particle Trail");
                        }
                        case 6 -> {
                            lore.add(" §8+§91 §7Opal");
                            lore.add(" §8+§dPink §7Particle Trail");
                        }
                        case 7 -> {
                            lore.add(" §8+§91 §7Opal");
                            lore.add(" §8+§cR§6a§ei§an§bb§9o§5w §7Particle Trail");
                            lore.add(" §8+§3[§e9§4✯] §5Prestige §7Scheme");
                        }
                    }
                    lore.add("");
                    lore.add("§8Earn Coins, Opals, Movement Trails");
                    lore.add("§8and an exclusive Prestige Scheme at");
                    lore.add("§8max level.");

                    // Determine material and name color
                    Material mat;
                    String nameColor;
                    if (unlocked) {
                        mat = Material.LIME_STAINED_GLASS_PANE;
                        nameColor = "§a";
                    } else if (isNextLevel && owned) {
                        mat = Material.YELLOW_STAINED_GLASS_PANE;
                        nameColor = "§e";
                    } else {
                        mat = Material.RED_STAINED_GLASS_PANE;
                        nameColor = "§c";
                    }

                    return ItemStackCreator.getStack(
                            nameColor + "Prestige " + PRESTIGE_NAMES[level],
                            mat,
                            level,
                            lore
                    );
                }
            });
        }

        // Kit Stats button (slot 30)
        set(new GUIClickableItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aKit Stats",
                        Material.FILLED_MAP,
                        1,
                        "§7Access your statistics and challenge",
                        "§7completions for this Kit!",
                        "",
                        "§eClick to open!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitStats(kit, mode).open(player);
            }
        });

        // Kit Customizer (slot 32) - only if owned
        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (owned) {
                    return ItemStackCreator.getStack(
                            "§cKit Customizer",
                            Material.BLAZE_POWDER,
                            1,
                            "§7Customize the layout of this kit.",
                            "",
                            "§c§lCOMING SOON!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§cKit Customizer",
                            Material.BLAZE_POWDER,
                            1,
                            "§7Customize the layout of this kit.",
                            "",
                            "§cYou don't own this kit!"
                    );
                }
            }
        });

        // Go Back (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String modeName = mode.equals("NORMAL") ? "Normal" : "Insane";
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To " + modeName + " Kits"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitSelector(mode).open(player);
            }
        });

        // Coins display (slot 49)
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§7Total Coins: §6" + String.format("%,d", coins),
                        Material.EMERALD,
                        1,
                        "§6https://store.hypixel.net"
                );
            }
        });

        // Favorite toggle (slot 50)
        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (isFavorite) {
                    return ItemStackCreator.getStack(
                            "§aFavorite Kit Toggle",
                            Material.LIME_DYE,
                            1,
                            "§7Kits that have been favorited show",
                            "§7up at the top of the Kit Selection",
                            "§7menu.",
                            "",
                            "§a§lFAVORITED",
                            "",
                            "§eClick to unfavorite!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§cFavorite Kit Toggle",
                            Material.GRAY_DYE,
                            1,
                            "§7Kits that have been favorited show",
                            "§7up at the top of the Kit Selection",
                            "§7menu.",
                            "",
                            "§c§lNOT FAVORITED",
                            "",
                            "§eClick to favorite!"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (!owned) {
                    player.sendMessage("§cYou must own this kit to favorite it!");
                    return;
                }

                unlocks.toggleFavorite(kit.getId());
                boolean nowFavorite = unlocks.isFavorite(kit.getId());
                if (nowFavorite) {
                    player.sendMessage("§e★ §aFavorited §e" + kit.getName() + " §akit!");
                } else {
                    player.sendMessage("§7☆ §7Unfavorited §e" + kit.getName() + " §7kit.");
                }
                // Refresh GUI
                new GUIKitBreakdown(kit, mode).open(player);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
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
