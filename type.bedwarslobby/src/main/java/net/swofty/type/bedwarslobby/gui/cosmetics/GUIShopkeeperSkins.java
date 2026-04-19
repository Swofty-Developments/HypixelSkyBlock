package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.collectibles.CollectibleSelectionCheck;
import net.swofty.type.generic.collectibles.CollectibleUnlockMethod;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleCatalog;
import net.swofty.type.generic.collectibles.bedwars.BedWarsCollectibleStateService;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.gui.v2.collectibles.CollectibleSelectionView;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public class GUIShopkeeperSkins extends CollectibleSelectionView {

    private static final int[] PAGINATED_SLOTS_WITH_RANDOM = {
        10,
        13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    public GUIShopkeeperSkins() {
        super("Shopkeeper Skins ");
    }

    @Override
    protected List<CollectibleDefinition> loadItems(HypixelPlayer player) {
        BedWarsCollectibleCatalog.initialize();
        BedWarsCollectibleStateService.reconcileSelected(player, CollectibleCategory.SHOPKEEPER_SKINS);
        return BedWarsCollectibleCatalog.getCategoryItems(CollectibleCategory.SHOPKEEPER_SKINS);
    }

    @Override
    protected CollectibleSelectionCheck selectionCheck(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.checkSelectable(player, definition);
    }

    @Override
    protected boolean isSelected(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.isSelected(player, CollectibleCategory.SHOPKEEPER_SKINS, definition.id());
    }

    @Override
    protected SelectionOutcome selectItem(HypixelPlayer player, ViewContext ctx, CollectibleDefinition definition) {
        CollectibleSelectionCheck check = BedWarsCollectibleStateService.checkSelectable(player, definition);
        if (!check.selectable() && definition.unlockRequirement().method() == CollectibleUnlockMethod.CURRENCY) {
            BedWarsCollectibleStateService.PurchaseCheck purchaseCheck =
                BedWarsCollectibleStateService.checkPurchase(player, definition);

            boolean openConfirmation = purchaseCheck.purchasable()
                || "You do not have enough BedWars Tokens.".equals(purchaseCheck.reason());

            if (openConfirmation) {
                ctx.push(new GUIBedWarsCollectiblePurchaseConfirm(),
                    new GUIBedWarsCollectiblePurchaseConfirm.State(definition.id(), purchaseCheck.cost()));
                return SelectionOutcome.success("");
            }

            String reason = purchaseCheck.reason() != null ? purchaseCheck.reason() : "This collectible cannot be purchased.";
            return SelectionOutcome.failure("§c" + reason);
        }

        BedWarsCollectibleStateService.SelectionResult result = BedWarsCollectibleStateService.select(player, definition);
        return result.success()
            ? SelectionOutcome.success(result.message())
            : SelectionOutcome.failure(result.message());
    }

    @Override
    protected long tokenBalance(HypixelPlayer player) {
        BedWarsDataHandler dataHandler = BedWarsDataHandler.getUser(player);
        if (dataHandler == null) {
            return 0L;
        }
        return dataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
    }

    @Override
    protected boolean isCategoryFavoriteable(CollectibleCategory category) {
        return BedWarsCollectibleCatalog.categorySupportsFavorites(category);
    }

    @Override
    protected boolean isFavorite(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.isFavorite(player, definition.id());
    }

    @Override
    protected boolean toggleFavorite(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.toggleFavorite(player, definition.id());
    }

    @Override
    protected boolean isPreviewSupported(HypixelPlayer player, CollectibleDefinition definition) {
        return true;
    }

    @Override
    protected void preview(HypixelPlayer player, CollectibleDefinition definition) {
        ShopkeeperPreviewController.startPreview(player, definition);
    }

    @Override
    protected boolean isPinnedDefault(HypixelPlayer player, CollectibleDefinition definition) {
        String pinned = BedWarsCollectibleCatalog.pinnedDefaultId(CollectibleCategory.SHOPKEEPER_SKINS);
        return pinned != null && pinned.equals(definition.id());
    }

    @Override
    protected int[] getPaginatedSlots() {
        if (BedWarsCollectibleCatalog.categorySupportsRandom(CollectibleCategory.SHOPKEEPER_SKINS)) {
            return PAGINATED_SLOTS_WITH_RANDOM;
        }
        return super.getPaginatedSlots();
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        super.layoutCustom(layout, state, ctx);

        if (!BedWarsCollectibleCatalog.categorySupportsRandom(CollectibleCategory.SHOPKEEPER_SKINS)) {
            return;
        }

        if (state.page() != 0) {
            return;
        }

        boolean randomSelected = BedWarsCollectibleStateService.isRandomModeSelected(
            ctx.player(), CollectibleCategory.SHOPKEEPER_SKINS, BedWarsCollectibleStateService.RANDOM_SELECTION_ID);
        boolean randomFavoriteSelected = BedWarsCollectibleStateService.isRandomModeSelected(
            ctx.player(), CollectibleCategory.SHOPKEEPER_SKINS, BedWarsCollectibleStateService.RANDOM_FAVORITE_SELECTION_ID);

        layout.slot(11,
            randomOptionStack("§aRandom Shopkeeper Skin", Material.CHEST, randomSelected,
                "§7Pick a random unlocked shopkeeper",
                "§7skin every time it is used."),
            (click, context) -> {
                BedWarsCollectibleStateService.SelectionResult result = BedWarsCollectibleStateService.selectSpecial(
                    context.player(),
                    CollectibleCategory.SHOPKEEPER_SKINS,
                    BedWarsCollectibleStateService.RANDOM_SELECTION_ID
                );
                context.player().sendMessage(result.message());
                context.session(State.class).refresh();
            }
        );

        layout.slot(12,
            randomOptionStack("§aRandom Favorite Shopkeeper Skin", Material.ENDER_CHEST, randomFavoriteSelected,
                "§7Use a Random §6 Favorite §7Shopkeeper Skin!"),
            (click, context) -> {
                BedWarsCollectibleStateService.SelectionResult result = BedWarsCollectibleStateService.selectSpecial(
                    context.player(),
                    CollectibleCategory.SHOPKEEPER_SKINS,
                    BedWarsCollectibleStateService.RANDOM_FAVORITE_SELECTION_ID
                );
                context.player().sendMessage(result.message());
                context.session(State.class).refresh();
            }
        );
    }

    @Override
    public void onClose(State state, ViewContext ctx, ViewSession.CloseReason reason) {

    }

    private static ItemStack.Builder randomOptionStack(
        String name,
        Material material,
        boolean selected,
        String... description
    ) {
        String[] lore = new String[description.length + 2];
        System.arraycopy(description, 0, lore, 0, description.length);
        lore[description.length] = "";
        lore[description.length + 1] = selected ? "§aSELECTED!" : "§eClick to select!";

        return ItemStackCreator.getStack(name, material, 1, lore);
    }
}
