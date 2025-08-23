package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIExperimentOver extends HypixelInventoryGUI {

    private final String gameType;
    private final String tier;
    private final boolean success;
    private final String errorMessage;
    private final int bestChain;
    private final int xpAward;
    private final int bonusClicksEarned;

    public GUIExperimentOver(String gameType, String tier, boolean success, String errorMessage, 
                           int bestChain, int xpAward, int bonusClicksEarned) {
        super("Experiment Over", InventoryType.CHEST_5_ROW);
        this.gameType = gameType;
        this.tier = tier;
        this.success = success;
        this.errorMessage = errorMessage;
        this.bestChain = bestChain;
        this.xpAward = xpAward;
        this.bonusClicksEarned = bonusClicksEarned;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, " ");

        // Chronomatron item (dark purple cylindrical with black cube) - slot [1,2] = 11
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                    "§5" + gameType,
                    Material.PURPLE_CONCRETE,
                    1,
                    "§7Claim",
                    "",
                    success ? "§aExperiment Complete!" : "§c" + errorMessage,
                    "",
                    "§7Stakes: " + getTierColor(tier) + tier,
                    "",
                    "§7Longest Chain: §e" + bestChain + " notes",
                    "",
                    "§7Rewards:",
                    "§b+" + String.format("%,d", xpAward) + " Enchanting Exp",
                    bonusClicksEarned > 0 ? "§b+" + bonusClicksEarned + " Superpairs clicks" : "§7No bonus clicks earned",
                    "",
                    "§eClick to claim rewards!"
                );
            }
        });

        // Redstone block (red cube) - slot [1,6] = 15
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                    "§bEnchanting Experience",
                    Material.REDSTONE_BLOCK,
                    Math.max(1, xpAward / 1000),
                    "§7Reward: §b+" + String.format("%,d", xpAward) + " Enchanting Exp"
                );
            }
        });

        // Claim button (bottom center)
        set(new GUIClickableItem(40) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                // Award the XP and bonus clicks (already done in manager, this is just UI feedback)
                player.sendMessage("§aRewards claimed!");
                player.sendMessage("§b+" + String.format("%,d", xpAward) + " Enchanting Experience");
                if (bonusClicksEarned > 0) {
                    player.sendMessage("§b+" + bonusClicksEarned + " Superpairs bonus clicks");
                }
                
                // Return to experiments table
                new GUIExperiments().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                    "§aClick to claim rewards!",
                    Material.LIME_STAINED_GLASS_PANE,
                    1,
                    "§7Click here to claim your rewards",
                    "§7and return to the Experimentation Table"
                );
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
        // Prevent moving items
    }

    private String getTierColor(String tier) {
        String t = tier.toLowerCase();
        if (t.contains("high")) return "§a";
        if (t.contains("grand")) return "§e";
        if (t.contains("supreme")) return "§6";
        if (t.contains("transcendent")) return "§c";
        if (t.contains("metaphysical")) return "§d";
        return "§f";
    }
}
