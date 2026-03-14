package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.level.SkywarsLevelCategory;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;

/**
 * Main SkyWars Menu GUI accessible from the hotbar emerald item.
 */
public class GUISkyWarsMenu extends HypixelInventoryGUI {

    public GUISkyWarsMenu() {
        super("SkyWars Menu", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        long playerXP = handler != null ? handler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue() : 0;
        int playerLevel = SkywarsLevelRegistry.calculateLevel(playerXP);
        long souls = handler != null ? handler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue() : 0;

        // Kits & Perks (slot 10)
        set(new GUIClickableItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aKits & Perks",
                        Material.ENDER_EYE,
                        1,
                        "§7Change the way you play by picking",
                        "§7kits and perks!",
                        "",
                        "§7Win kits and perks in the §bSoul Well §7or",
                        "§7buy them directly using §6coins§7.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitsPerks().open(player);
            }
        });

        // My Cosmetics (slot 11) - placeholder for now
        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMy Cosmetics",
                        Material.ARMOR_STAND,
                        1,
                        "§7Browse and equip all the available",
                        "§7in-game SkyWars cosmetics.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cCosmetics browser coming soon!");
            }
        });

        // SkyWars Level Progression (slot 12)
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                // Get progress info for display
                long xpProgress = SkywarsLevelRegistry.getXPIntoCurrentLevel(playerXP);
                long xpNeeded = SkywarsLevelRegistry.getXPForNextLevel(playerXP);
                double progress = SkywarsLevelRegistry.getProgressToNextLevel(playerXP);
                int filled = (int) (progress * 10);
                int nextLevel = Math.min(playerLevel + 1, SkywarsLevelRegistry.getMaxLevel());

                // Get current and next prestige info
                SkywarsLevelCategory.SkywarsLevel currentPrestige = SkywarsLevelRegistry.getCurrentPrestige(playerXP);
                String currentEmblem = currentPrestige != null ? currentPrestige.getEmblem() : "§7[1\u272F]";

                SkywarsLevelCategory.SkywarsLevel nextLevelData = SkywarsLevelRegistry.getLevel(nextLevel);
                String nextEmblem = nextLevelData != null ? "§f[" + nextLevel + "\u272F]" : "";

                String progressBar = "§8[§b" + "\u25A0".repeat(filled) + "§7" + "\u25A0".repeat(10 - filled) + "§8]";

                return ItemStackCreator.getStack(
                        "§dSkyWars Level Progression",
                        Material.NETHER_STAR,
                        1,
                        "§7View information about your SkyWars",
                        "§7Level progression, select your",
                        "§7Emblem, and view level rewards.",
                        "",
                        "§7Progress: §b" + SkywarsLevelCategory.formatXPRequirement(xpProgress) +
                                "§7/§a" + SkywarsLevelCategory.formatXPRequirement(xpNeeded),
                        currentEmblem + " " + progressBar + " " + nextEmblem,
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISkyWarsLevelProgression().open(player);
            }
        });

        // Soul Well (slot 14)
        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§bSoul Well",
                        Material.END_PORTAL_FRAME,
                        1,
                        "§7Test your luck by spending §bSouls §7to",
                        "§7earn random Kits and Perks!",
                        "",
                        "§bSouls §7are earned by killing players",
                        "§7in games of SkyWars!",
                        "",
                        "§7Your Souls: §b" + souls,
                        "",
                        "§eClick to open!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.openView(new GUISoulWell());
            }
        });

        // Angel's Descent (slot 15) - locked until level 15
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (playerLevel >= 15) {
                    return ItemStackCreator.getStack(
                            "§bAngel's Descent",
                            Material.FEATHER,
                            1,
                            "§7Spend §9Opals §7to unlock exclusive",
                            "§7perks, upgrades, kits, and cosmetics!",
                            "",
                            "§7Large amounts of §bSouls §7can be",
                            "§7fused into §9Opals §7at the §5Fallen Forge§7.",
                            "",
                            "§eClick to enter!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§bAngel's Descent",
                            Material.RED_STAINED_GLASS_PANE,
                            1,
                            "§7Spend §9Opals §7to unlock exclusive",
                            "§7perks, upgrades, kits, and cosmetics!",
                            "",
                            "§7Large amounts of §bSouls §7can be",
                            "§7fused into §9Opals §7at the §5Fallen Forge§7.",
                            "",
                            "§c§l!! Requires SkyWars Level 15!"
                    );
                }
            }
        });

        // Angel's Brewery (slot 16) - locked until level 25
        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (playerLevel >= 25) {
                    return ItemStackCreator.getStack(
                            "§cAngel's Brewery",
                            Material.BREWING_STAND,
                            1,
                            "§7Brew Potions using §6Coins §7and §9Opals",
                            "§7which grant buffs for the next §a50",
                            "§7games you play.",
                            "",
                            "§eClick to brew!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§cAngel's Brewery",
                            Material.RED_STAINED_GLASS_PANE,
                            1,
                            "§7Brew Potions using §6Coins §7and §9Opals",
                            "§7which grant buffs for the next §a50",
                            "§7games you play.",
                            "",
                            "§c§l!! Requires SkyWars Level 25!"
                    );
                }
            }
        });

        // Close button (slot 31)
        set(GUIClickableItem.getCloseItem(31));

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
