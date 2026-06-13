package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.guild.GuildData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.text.NumberFormat;

public class GUIGuildAchievements implements View<GUIGuildAchievements.GuildAchievementsState> {

    private static final int[][] PRESTIGE_TIERS = {{20}, {40}, {60}, {80}, {100}};
    private static final int[] PRESTIGE_SLOTS = {0, 1, 2, 3, 4};

    private static final int[][] EXP_KING_TIERS = {{50000}, {100000}, {150000}, {200000}, {250000}, {275000}, {300000}};
    private static final int[] EXP_KING_SLOTS = {9, 10, 11, 12, 13, 14, 15};

    private static final int[][] FAMILY_TIERS = {{5}, {15}, {30}, {40}, {50}, {60}, {70}};
    private static final int[] FAMILY_SLOTS = {27, 28, 29, 30, 31, 32, 33};

    @Override
    public ViewConfiguration<GuildAchievementsState> configuration() {
        return new ViewConfiguration<>("Guild Achievements", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<GuildAchievementsState> layout, GuildAchievementsState state, ViewContext ctx) {
        GuildData guild = state.guild();
        int level = guild.getLevel();
        NumberFormat nf = NumberFormat.getInstance();

        for (int i = 0; i < PRESTIGE_TIERS.length; i++) {
            int required = PRESTIGE_TIERS[i][0];
            boolean achieved = level >= required;
            String name = (achieved ? "§a" : "§7") + "Prestige " + toRoman(i + 1);
            Material mat = achieved ? Material.EXPERIENCE_BOTTLE : Material.GRAY_DYE;
            String progress = "§7Progress: §e" + level + "§7/" + required + (achieved ? " §aACHIEVED" : "");
            layout.slot(PRESTIGE_SLOTS[i], ItemStackCreator.getStack(name, mat, 1,
                "§7Reach Guild level " + required, "", progress));
        }

        long gexp = guild.getTotalGexp();
        for (int i = 0; i < EXP_KING_TIERS.length; i++) {
            int required = EXP_KING_TIERS[i][0];
            boolean achieved = gexp >= required;
            String name = (achieved ? "§a" : "§7") + "Experience Kings " + toRoman(i + 1);
            Material mat = achieved ? Material.CLOCK : Material.GRAY_DYE;
            String progress = "§7Progress: §e" + nf.format(gexp) + "§7/" + nf.format(required) + (achieved ? " §aACHIEVED" : "");
            layout.slot(EXP_KING_SLOTS[i], ItemStackCreator.getStack(name, mat, 1,
                "§7Get " + nf.format(required) + " Guild Exp in one day", "", progress));
        }

        int memberCount = guild.getMembers().size();
        for (int i = 0; i < FAMILY_TIERS.length; i++) {
            int required = FAMILY_TIERS[i][0];
            boolean achieved = memberCount >= required;
            String name = (achieved ? "§a" : "§7") + "Family " + toRoman(i + 1);
            Material mat = achieved ? Material.DIAMOND : Material.GRAY_DYE;
            String progress = "§7Progress: §e" + memberCount + "§7/" + required + (achieved ? " §aACHIEVED" : "");
            layout.slot(FAMILY_SLOTS[i], ItemStackCreator.getStack(name, mat, 1,
                "§7Have " + required + " guild members online", "§7at the same time!", "", progress));
        }

        layout.slot(49, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Guild"
        ), (click, viewCtx) -> viewCtx.navigator().pop());
    }

    private static String toRoman(int num) {
        return switch (num) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            default -> String.valueOf(num);
        };
    }

    public record GuildAchievementsState(GuildData guild) {
    }
}
