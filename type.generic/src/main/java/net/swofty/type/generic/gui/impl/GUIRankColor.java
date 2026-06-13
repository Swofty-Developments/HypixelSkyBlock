package net.swofty.type.generic.gui.impl;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.user.categories.RankColor;

public class GUIRankColor extends StatelessView {
    private static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Rank Color", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 40);
        RankColor[] colors = RankColor.values();
        for (int i = 0; i < colors.length; i++) addColor(layout, SLOTS[i], colors[i]);

        if (ctx.player().getRank() == Rank.MVP_PLUS_PLUS) {
            layout.slot(44, (_, c) -> {
                boolean aqua = c.player().isMvpPlusPlusAqua();
                return ItemStackCreator.getStack("§aToggle Prefix Color", Material.NETHER_STAR, 1,
                    "§7Selected: " + (aqua ? "§bAqua" : "§6Gold"), "",
                    "§7Click to change the color to " + (aqua ? "§6Gold" : "§bAqua"));
            }, (_, c) -> {
                var data = c.player().getDataHandler().get(net.swofty.type.generic.data.HypixelDataHandler.Data.MVP_PLUS_PLUS_AQUA,
                    net.swofty.type.generic.data.datapoints.DatapointBoolean.class);
                data.setValue(!data.getValue());
                c.session().refresh();
            });
        }
    }

    private void addColor(ViewLayout<DefaultState> layout, int slot, RankColor color) {
        layout.slot(slot, (_, ctx) -> render(ctx.player(), color), (_, ctx) -> {
            HypixelPlayer player = ctx.player();
            if (!player.getRank().isEqualOrHigherThan(Rank.MVP_PLUS)) {
                player.sendMessage("§cYou must be MVP+ or higher to use rank colors!");
                return;
            }
            if (!color.isUnlocked(player)) {
                player.sendMessage(color == RankColor.DARK_BLUE
                    ? "§cYou must gift 100 ranks to unlock this rank color!"
                    : "§cYou must be Hypixel Level " + color.getRequiredLevel() + " to unlock this rank color!");
                return;
            }
            player.setRankColor(color);
            ctx.session().refresh();
        });
    }

    private ItemStack.Builder render(HypixelPlayer player, RankColor color) {
        boolean hasRank = player.getRank().isEqualOrHigherThan(Rank.MVP_PLUS);
        boolean unlocked = hasRank && color.isUnlocked(player);
        boolean selected = player.getRankColor() == color;
        String code = legacyCode(color.getColor());
        String ending = selected ? "§aCurrently selected!" : unlocked ? "§eClick to select!"
                                                             : color == RankColor.DARK_BLUE ? "§6Unlock by claiming 100 Ranks Gifted Reward!"
                                                               : "§3Unlocked at Hypixel Level " + color.getRequiredLevel();
        return ItemStackCreator.getStack((unlocked ? "§a" : "§c") + color.getDisplayName() + " Rank Color",
            unlocked ? color.getMaterial() : Material.GRAY_DYE, 1,
            "§7Changes the color of the plus in §bMVP§c+",
            "§7to " + color.getDisplayName().toLowerCase() + ", turning it into §bMVP" + code + "+",
            "", "§7Shown in tab list also when chatting", "§7and joining lobbies.", "", ending);
    }

    private String legacyCode(NamedTextColor color) {
        return "§" + Integer.toHexString(color.value() == 0x000000 ? 0 : switch (color.toString()) {
            case "dark_blue" -> 1;
            case "dark_green" -> 2;
            case "dark_aqua" -> 3;
            case "dark_red" -> 4;
            case "dark_purple" -> 5;
            case "gold" -> 6;
            case "gray" -> 7;
            case "dark_gray" -> 8;
            case "blue" -> 9;
            case "green" -> 10;
            case "aqua" -> 11;
            case "red" -> 12;
            case "light_purple" -> 13;
            case "yellow" -> 14;
            default -> 15;
        });
    }
}
