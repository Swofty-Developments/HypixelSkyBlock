package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleCatalog;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleStateService;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.List;
import java.util.Optional;

public class GUIMyCosmetics extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("My Cosmetics", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        BedWarsCollectibleCatalog.initialize();

        layout.slot(10, ItemStackCreator.getStack(
            "§aProjectile Trails ",
            Material.ARROW,
            1,
            "§7Change your projectile particle trail",
            "§7effect.",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(12, ItemStackCreator.getStackHead(
            "§aVictory Dances ",
            "73480592266dd7f53681efeee3188af531eea53da4af583a67617deeb4f473",
            1,
            "§7Celebrate by gloating and showing",
            "§7off to other players whenever you",
            "§7win!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aFinal Kill Effects ",
            Material.IRON_SWORD,
            1,
            "§7A selection of various effects to",
            "§7choose from that will trigger",
            "§7whenever you final kill an enemy!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§aSprays ",
            Material.FILLED_MAP,
            1,
            "§7Select a spray to show off all over",
            "§7the place! Spray slots can be found",
            "§7on every spawn island and some",
            "§7center islands.",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(19, ItemStackCreator.getStackHead(
            "§aIsland Toppers ",
            "d55b1aa95fdb777179a4bb9c92f116d787eddc97b9b8c1666256eedf2d6b35",
            1,
            "§7Select an Island Topper to decorate",
            "§7your island with! In Doubles and",
            "§7Teams Modes a random player's",
            "§7choice from each team is chosen.",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(21, ItemStackCreator.getStackHead(
            "§aDeath Cries ",
            "b371e4e1cf6a1a36fdae27137fd9b8748e6169299925f9af2be301e54298c73",
            1,
            "§7Let others know just how salty your",
            "§7tears are every time you die with",
            "§7these death cries!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(23, (_, c) -> {
            CosmeticSummary summary = summarize(c.player(), CollectibleCategory.SHOPKEEPER_SKINS);
            return ItemStackCreator.getStackHead(
                "§aShopkeeper Skins ",
                "822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b",
                1,
                "§7Select from various Shopkeeper",
                "§7skins, which will replace how the",
                "§7Shopkeepers look in-game! In",
                "§7Doubles and Teams Modes a random",
                "§7player's choice from each team is",
                "§7chosen.",
                "",
                "§7Unlocked: §a" + summary.unlocked() + "/" + summary.total() + " §8(" + summary.percent() + "%)",
                "§7Currently Selected:",
                summary.selectedDisplay(),
                "",
                "§eClick to view!"
            );
        }, (_, context) -> context.push(new GUIShopkeeperSkins()));
        layout.slot(25, ItemStackCreator.getStack(
            "§aKill Messages ",
            Material.OAK_SIGN,
            1,
            "§7Select a Kill Message package to",
            "§7replace chat messages when you kill",
            "§7players, Teams and break Beds!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(28, ItemStackCreator.getStack(
            "§aGlyphs ",
            Material.DIAMOND,
            1,
            "§7Select a Glyph image which will",
            "§7appear when picking up diamonds and",
            "§7emeralds!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(30, ItemStackCreator.getStack(
            "§aBed Destroys ",
            Material.RED_BED,
            1,
            "§7Select from various Bed Destroy",
            "§7effects, which will occur when you",
            "§7break a bed!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        layout.slot(32, (_, c) -> {
            CosmeticSummary summary = summarize(c.player(), CollectibleCategory.WOOD_SKINS);
            return ItemStackCreator.getStack(
                "§aWood Skins ",
                Material.DARK_OAK_PLANKS,
                1,
                "§7Change the Skin of Wood in-game.",
                "",
                "§7Unlocked: §a" + summary.unlocked() + "/" + summary.total() + " §8(" + summary.percent() + "%)",
                "§7Currently Selected:",
                summary.selectedDisplay(),
                "",
                "§eClick to view!"
            );
        }, (_, context) -> context.push(new GUIWoodSkins()));
        layout.slot(34, ItemStackCreator.getStack(
            "§aFigurines ",
            Material.ARMOR_STAND,
            1,
            "§7Choose which of your figurines is",
            "§7showcased at your base in games!",
            "",
            "§7Unlocked: §a0/0 §8(NaN%)",
            "§7Currently Selected:",
            "§aNone",
            "",
            "§eClick to view!"
        ));
        Components.backOrClose(layout, 48, ctx);
        layout.slot(49, (_, c) -> {
            BedWarsDataHandler dataHandler = BedWarsDataHandler.getUser(c.player());
            long tokens = dataHandler == null
                ? 0L
                : dataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
            return ItemStackCreator.getStack(
                "§7Total Tokens: §2" + StringUtility.commaify(tokens),
                Material.EMERALD,
                1,
                "§6https://store.hypixel.net"
            );
        });
        layout.slot(50, ItemStackCreator.getStack(
            "§aSearch",
            Material.COMPASS,
            1,
            "§7Use this feature to easily find a",
            "§7specific cosmetic item."
        ));
    }

    private static CosmeticSummary summarize(net.swofty.type.generic.user.HypixelPlayer player, CollectibleCategory category) {
        List<CollectibleDefinition> definitions = BedWarsCollectibleCatalog.getCategoryItems(category);
        int total = definitions.size();
        int unlocked = (int) definitions.stream()
            .filter(definition -> BedWarsCollectibleStateService.checkSelectable(player, definition).selectable())
            .count();

        int percent = total == 0 ? 0 : (int) Math.round((unlocked * 100.0) / total);
        String selectedDisplay = resolveSelectedDisplay(player, category);
        return new CosmeticSummary(unlocked, total, percent, selectedDisplay);
    }

    private static String resolveSelectedDisplay(net.swofty.type.generic.user.HypixelPlayer player, CollectibleCategory category) {
        String selectedId = BedWarsCollectibleStateService.getSelectedId(player, category);
        if (BedWarsCollectibleStateService.RANDOM_SELECTION_ID.equals(selectedId)) {
            return "§aRandom";
        }
        if (BedWarsCollectibleStateService.RANDOM_FAVORITE_SELECTION_ID.equals(selectedId)) {
            return "§aRandom Favorite";
        }

        Optional<CollectibleDefinition> selected = BedWarsCollectibleStateService.resolveSelected(player, category);
        return selected.map(collectibleDefinition -> "§a" + collectibleDefinition.name()).orElse("§cNone");
    }

    private record CosmeticSummary(int unlocked, int total, int percent, String selectedDisplay) {
    }
}
