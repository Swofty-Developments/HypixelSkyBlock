package net.swofty.type.skyblockgeneric.gui.inventories;

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
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateShopMilestone;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIChocolateShopMilestones extends HypixelInventoryGUI {
    // Slot mapping for milestones (milestone number -> slot)
    // Layout based on the original design (spiral pattern)
    private static final int[] MILESTONE_SLOTS = {
            27, // 1
            18, // 2
            9,  // 3
            0,  // 4
            1,  // 5
            2,  // 6
            11, // 7
            20, // 8
            29, // 9
            30, // 10
            31, // 11
            22, // 12
            13, // 13
            4,  // 14
            5,  // 15
            6,  // 16
            15, // 17
            24, // 18
            33, // 19
            34, // 20
            35, // 21
            26, // 22
            17, // 23
            8   // 24
    };

    public GUIChocolateShopMilestones() {
        super("Chocolate Shop Milestones", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        // Fill with black glass panes
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
        long totalSpent = data.getTotalChocolateSpent();

        // Set milestone items
        for (ChocolateShopMilestone milestone : ChocolateShopMilestone.values()) {
            int slot = MILESTONE_SLOTS[milestone.getNumber() - 1];
            set(createMilestoneItem(slot, milestone, totalSpent));
        }

        // Go Back button (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer p) {
                new GUIChocolateShop().open((SkyBlockPlayer) p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7To Chocolate Shop");
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, lore);
            }
        });

        // Close button (slot 49)
        set(GUIClickableItem.getCloseItem(49));
    }

    private GUIItem createMilestoneItem(int slot, ChocolateShopMilestone milestone, long totalSpent) {
        boolean unlocked = milestone.isUnlocked(totalSpent);

        if (unlocked) {
            // Show player head when unlocked
            return new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return createUnlockedMilestoneItem(milestone);
                }
            };
        } else {
            // Show colored glass pane when locked
            return new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                    return createLockedMilestoneItem(milestone, data.getTotalChocolateSpent());
                }
            };
        }
    }

    private ItemStack.Builder createUnlockedMilestoneItem(ChocolateShopMilestone milestone) {
        List<String> lore = new ArrayList<>();
        String formattedReq = formatRequirement(milestone.getRequiredSpent());

        lore.add("§7Spend §6" + formattedReq + " Chocolate §7in the shop");
        lore.add("§7to unlock this special §aChocolate Rabbit§7!");
        lore.add("");
        lore.add("§7Milestone " + milestone.getRomanNumeral() + " Reward");
        lore.add("§" + milestone.getColorCode() + milestone.getRabbitName());
        lore.add("");

        if (milestone.getChocolateBonus() > 0) {
            lore.add("§7Grants §6+" + milestone.getChocolateBonus() + " Chocolate §7and §6" +
                    String.format("%.3fx", milestone.getMultiplierBonus()));
            lore.add("§6Chocolate §7per second to your");
            lore.add("§7§6Chocolate Factory§7.");
        } else {
            lore.add("§7Grants §6+" + String.format("%.2fx", milestone.getMultiplierBonus()) + " Chocolate §7per second");
            lore.add("§7to your §6Chocolate Factory§7.");
        }
        lore.add("");
        lore.add("§a§lUNLOCKED");

        return ItemStackCreator.getStackHead(
                "§" + milestone.getColorCode() + getOrdinal(milestone.getNumber()) + " Shop Milestone",
                milestone.getTextureId(),
                1,
                lore
        );
    }

    private ItemStack.Builder createLockedMilestoneItem(ChocolateShopMilestone milestone, long totalSpent) {
        List<String> lore = new ArrayList<>();
        String formattedReq = formatRequirement(milestone.getRequiredSpent());
        String formattedCurrent = formatRequirement(totalSpent);
        double progress = milestone.getProgress(totalSpent);

        lore.add("§7Spend §6" + formattedReq + " Chocolate §7in the shop");
        lore.add("§7to unlock this special §aChocolate Rabbit§7!");
        lore.add("");
        lore.add("§7Progress to Milestone " + milestone.getRomanNumeral() + ": §b" + String.format("%.0f", progress) + "%");
        lore.add(createProgressBar(progress) + " §b" + formattedCurrent + "§3/§b" + formattedReq);
        lore.add("");
        lore.add("§7Milestone " + milestone.getRomanNumeral() + " Reward");
        lore.add("§" + milestone.getColorCode() + milestone.getRabbitName());
        lore.add("");

        if (milestone.getChocolateBonus() > 0) {
            lore.add("§7Grants §6+" + milestone.getChocolateBonus() + " Chocolate §7and §6" +
                    String.format("%.3fx", milestone.getMultiplierBonus()));
            lore.add("§6Chocolate §7per second to your");
            lore.add("§7§6Chocolate Factory§7.");
        } else {
            lore.add("§7Grants §6+" + String.format("%.2fx", milestone.getMultiplierBonus()) + " Chocolate §7per second");
            lore.add("§7to your §6Chocolate Factory§7.");
        }
        lore.add("");
        lore.add("§cRequires spending " + formattedReq + " Chocolate!");

        return ItemStackCreator.getStack(
                "§" + milestone.getColorCode() + getOrdinal(milestone.getNumber()) + " Shop Milestone",
                milestone.getGlassPaneMaterial(),
                1,
                lore
        );
    }

    private String createProgressBar(double progress) {
        int filled = (int) (progress / 4); // 25 characters total, each represents 4%
        int empty = 25 - filled;

        StringBuilder bar = new StringBuilder("§3§l§m");
        for (int i = 0; i < filled; i++) {
            bar.append(" ");
        }
        bar.append("§f§l§m");
        for (int i = 0; i < empty; i++) {
            bar.append(" ");
        }
        bar.append("§r");

        return bar.toString();
    }

    private String formatRequirement(long amount) {
        if (amount >= 1_000_000_000_000L) {
            double val = amount / 1_000_000_000_000.0;
            return val == (long) val ? String.format("%.0fT", val) : String.format("%.1fT", val);
        } else if (amount >= 1_000_000_000L) {
            double val = amount / 1_000_000_000.0;
            return val == (long) val ? String.format("%.0fB", val) : String.format("%.1fB", val);
        } else if (amount >= 1_000_000L) {
            double val = amount / 1_000_000.0;
            return val == (long) val ? String.format("%.0fM", val) : String.format("%.1fM", val);
        } else if (amount >= 1_000L) {
            double val = amount / 1_000.0;
            return val == (long) val ? String.format("%.0fk", val) : String.format("%.1fk", val);
        }
        return String.valueOf(amount);
    }

    private String getOrdinal(int number) {
        String[] suffixes = {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        return number + suffixes[number % 10];
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
