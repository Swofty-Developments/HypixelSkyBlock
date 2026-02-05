package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateMilestone;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIChocolateFactoryMilestones implements StatefulView<GUIChocolateFactoryMilestones.State> {
    private static final int[] MILESTONE_SLOTS = {
            27, 18, 9, 0, 1, 2, 11, 20, 29, 30, 31, 22,
            13, 4, 5, 6, 15, 24, 33, 34, 35, 26, 17, 8
    };

    public record State() {}

    @Override
    public State initialState() {
        return new State();
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "Chocolate Factory Milestones", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        // Set milestone items
        for (ChocolateMilestone milestone : ChocolateMilestone.values()) {
            int slot = MILESTONE_SLOTS[milestone.getNumber() - 1];
            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);
                long allTimeChocolate = data.getChocolateAllTime();

                if (milestone.isUnlocked(allTimeChocolate)) {
                    return createUnlockedMilestoneItem(milestone);
                } else {
                    return createLockedMilestoneItem(milestone, allTimeChocolate);
                }
            });
        }

        // Go Back button (slot 48)
        layout.slot(48, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7To Chocolate Factory");
            return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, lore);
        }, (click, c) -> GUIChocolateFactory.open((SkyBlockPlayer) c.player()));

        // Close button (slot 49)
        Components.close(layout, 49);
    }

    private ItemStack.Builder createUnlockedMilestoneItem(ChocolateMilestone milestone) {
        List<String> lore = new ArrayList<>();
        String formattedReq = formatRequirement(milestone.getRequiredChocolate());

        lore.add("§7Reach §6" + formattedReq + " Chocolate §7all-time to");
        lore.add("§7unlock this special §aChocolate Rabbit§7!");
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
                "§" + milestone.getColorCode() + getOrdinal(milestone.getNumber()) + " Chocolate Milestone",
                milestone.getTextureId(),
                1,
                lore
        );
    }

    private ItemStack.Builder createLockedMilestoneItem(ChocolateMilestone milestone, long allTimeChocolate) {
        List<String> lore = new ArrayList<>();
        String formattedReq = formatRequirement(milestone.getRequiredChocolate());
        String formattedCurrent = formatRequirement(allTimeChocolate);
        double progress = milestone.getProgress(allTimeChocolate);

        lore.add("§7Reach §6" + formattedReq + " Chocolate §7all-time to");
        lore.add("§7unlock this special §aChocolate Rabbit§7!");
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
        lore.add("§cRequires " + formattedReq + " all-time Chocolate!");

        return ItemStackCreator.getStack(
                "§" + milestone.getColorCode() + getOrdinal(milestone.getNumber()) + " Chocolate Milestone",
                milestone.getGlassPaneMaterial(),
                1,
                lore
        );
    }

    private String createProgressBar(double progress) {
        int filled = (int) (progress / 4);
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

    public static void open(SkyBlockPlayer player) {
        ViewNavigator.get(player).push(new GUIChocolateFactoryMilestones());
    }
}
