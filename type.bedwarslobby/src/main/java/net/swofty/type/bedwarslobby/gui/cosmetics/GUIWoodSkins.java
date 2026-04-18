package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: dynamic, PaginatedView
public class GUIWoodSkins extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Wood Skins ", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        // COMMON, unlocked and selected by default.
        layout.slot(10, ItemStackCreator.getStack(
            "§aOak Plank",
            Material.OAK_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Oak Plank Wood Skin to be",
            "§7used when placing wood blocks.",
            "§eClick to select!"
        ));

        // MVP+ required, EPIC
        layout.slot(11, ItemStackCreator.getStack(
            "§aAcacia Plank",
            Material.ACACIA_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Acacia Plank Wood Skin to",
            "§7be used when placing wood blocks.",
            "§eClick to select!"
        ));

        // MVP, EPIC
        layout.slot(12, ItemStackCreator.getStack(
            "§aJungle Plank",
            Material.JUNGLE_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Jungle Plank Wood Skin to",
            "§7be used when placing wood blocks.",
            "§eClick to select!"
        ));

        // VIP+, RARE
        layout.slot(13, ItemStackCreator.getStack(
            "§aBirch Plank",
            Material.BIRCH_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Birch Plank Wood Skin to",
            "§7be used when placing wood blocks.",
            "§aSELECTED!"
        ));

        // VIP RARE
        layout.slot(14, ItemStackCreator.getStack(
            "§aSpruce Plank",
            Material.SPRUCE_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Spruce Plank Wood Skin to",
            "§7be used when placing wood blocks.",
            "§eClick to select!"
        ));

        // custom unlock, LEGENDARY like all other logs
        layout.slot(15, ItemStackCreator.getStack(
            "§cDark Oak Log",
            Material.DARK_OAK_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Dark Oak Log Wood Skin to",
            "§7be used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§cAcacia Log",
            Material.ACACIA_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Acacia Log Wood Skin to",
            "§7be used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));
        layout.slot(19, ItemStackCreator.getStack(
            "§cJungle Log",
            Material.JUNGLE_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Jungle Log Wood Skin to",
            "§7be used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));
        layout.slot(20, ItemStackCreator.getStack(
            "§cBirch Log",
            Material.BIRCH_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Birch Log Wood Skin to be",
            "§7used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));
        layout.slot(21, ItemStackCreator.getStack(
            "§cSpruce Log",
            Material.SPRUCE_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Spruce Log Wood Skin to",
            "§7be used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));
        layout.slot(22, ItemStackCreator.getStack(
            "§cOak Log",
            Material.OAK_LOG,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Oak Log Wood Skin to be",
            "§7used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked in Tournament Hall!"
        ));

        // legendary mvp++
        layout.slot(23, ItemStackCreator.getStack(
            "§cDark Oak Plank",
            Material.DARK_OAK_PLANKS,
            1,
            "§8Wood Skin",
            "",
            "§7Select the Dark Oak Plank Wood Skin",
            "§7to be used when placing wood blocks.",
            "",
            "§7Rarity: §6LEGENDARY",
            "",
            "§bUnlocked with MVP§9++§b!"
        ));
        Components.back(layout, 48, ctx);

        layout.slot(49, ItemStackCreator.getStack(
            "§7Total Tokens: §21,924,622",
            Material.EMERALD,
            1,
            "§6https://store.hypixel.net"
        ));
        layout.slot(50, ItemStackCreator.getStack(
            "§6Sorted by: §aHighest rarity first",
            Material.HOPPER,
            1,
            "§7Sorts by rarity: Highest rarity first",
            "",
            "§7Next sort: §aA to Z",
            "§eLeft click to use!",
            "",
            "§7Owned items first: §aYes",
            "§eRight click to toggle!"
        ));
    }
}
