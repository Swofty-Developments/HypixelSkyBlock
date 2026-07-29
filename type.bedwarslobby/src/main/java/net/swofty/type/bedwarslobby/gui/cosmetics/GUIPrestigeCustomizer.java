package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarslobby.gui.cosmetics.prestige.GUIPrestigeBrackets;
import net.swofty.type.bedwarslobby.gui.cosmetics.prestige.GUIPrestigeSchemes;
import net.swofty.type.bedwarslobby.gui.cosmetics.prestige.GUIPrestigeStars;
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
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.Optional;

public class GUIPrestigeCustomizer extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Prestige Customizer", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        CategorySummary schemes = summarize(ctx.player(), CollectibleCategory.PRESTIGE_SCHEMES);
        CategorySummary stars = summarize(ctx.player(), CollectibleCategory.PRESTIGE_STARS);
        CategorySummary brackets = summarize(ctx.player(), CollectibleCategory.PRESTIGE_BRACKETS);

        Components.backOrClose(layout, 48, ctx);

        layout.slot(19, ItemStackCreator.getStack(
            "§aPrestige Schemes ",
            Material.ORANGE_DYE,
            1,
            "§7Customize what colors are in your",
            "§7prestige.",
            "",
            schemes.unlockedLine(),
            "§7Currently Selected:",
            "§a" + schemes.selectedName(),
            "",
            "§eClick to view!"
        ), (_, context) -> context.push(new GUIPrestigeSchemes()));
        layout.slot(21, ItemStackCreator.getStack(
            "§aPrestige Stars ",
            Material.NETHER_STAR,
            1,
            "§7Customize what star icon is within",
            "§7your prestige.",
            "",
            stars.unlockedLine(),
            "§7Currently Selected:",
            "§a" + stars.selectedName(),
            "",
            "§eClick to view!"
        ), (_, context) -> context.push(new GUIPrestigeStars()));
        layout.slot(23, ItemStackCreator.getStack(
            "§aPrestige Brackets ",
            Material.OAK_FENCE,
            1,
            "§7Customize what brackets surround",
            "§7your prestige.",
            "",
            brackets.unlockedLine(),
            "§7Currently Selected:",
            "§a" + brackets.selectedName(),
            "",
            "§eClick to view!"
        ), (_, context) -> context.push(new GUIPrestigeBrackets()));
        layout.slot(25, ItemStackCreator.getStack(
            "§cPrestige Formatting",
            Material.RED_STAINED_GLASS,
            1,
            "§8You'll need to talk to the Hotel Owner",
            "§8first..."
        ));
        layout.slot(49, ItemStackCreator.getStack(
            "§7Total Tokens: §2" + StringUtility.commaify(tokenBalance(ctx.player())),
            Material.EMERALD,
            1,
            "§6https://store.hypixel.net"
        ));
        layout.slot(50, ItemStackCreator.getStack(
            "§aSearch",
            Material.COMPASS,
            1,
            "§7Use this feature to easily find a",
            "§7specific cosmetic item."
        ));
    }

    private CategorySummary summarize(HypixelPlayer player, CollectibleCategory category) {
        BedWarsCollectibleCatalog.initialize();
        BedWarsCollectibleStateService.reconcileSelected(player, category);
        List<CollectibleDefinition> items = BedWarsCollectibleCatalog.getCategoryItems(category);
        long unlocked = items.stream()
            .filter(item -> BedWarsCollectibleStateService.checkSelectable(player, item).selectable())
            .count();
        String selectedId = BedWarsCollectibleStateService.getSelectedId(player, category);
        String selectedName = Optional.ofNullable(selectedId)
            .flatMap(BedWarsCollectibleCatalog::findItemById)
            .map(CollectibleDefinition::name)
            .orElse("None");
        return new CategorySummary((int) unlocked, items.size(), selectedName);
    }

    private long tokenBalance(HypixelPlayer player) {
        BedWarsDataHandler dataHandler = BedWarsDataHandler.getUser(player);
        if (dataHandler == null) {
            return 0L;
        }
        return dataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
    }

    private record CategorySummary(int unlocked, int total, String selectedName) {
        private String unlockedLine() {
            int percent = total == 0 ? 0 : (int) Math.floor(unlocked * 100.0 / total);
            return "§7Unlocked: §a" + unlocked + "/" + total + " §8(" + percent + "%)";
        }
    }
}
