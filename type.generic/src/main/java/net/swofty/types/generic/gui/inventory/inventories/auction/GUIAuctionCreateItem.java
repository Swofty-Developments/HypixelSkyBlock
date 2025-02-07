package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAuctionEscrow;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.AuctionCategoryComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIAuctionCreateItem extends SkyBlockAbstractInventory {
    private static final String STATE_HAS_ITEM = "has_item";
    private static final String STATE_BIN_MODE = "bin_mode";
    private final SkyBlockAbstractInventory previousGUI;

    public GUIAuctionCreateItem(SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.previousGUI = previousGUI;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        DatapointAuctionEscrow.AuctionEscrow escrow = player.getDataHandler().get(
                DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        // Set initial states
        if (escrow.getItem() != null) {
            doAction(new AddStateAction(STATE_HAS_ITEM));
        }
        if (escrow.isBin()) {
            doAction(new AddStateAction(STATE_BIN_MODE));
            doAction(new SetTitleAction(Component.text("Create BIN Auction")));
        } else {
            doAction(new SetTitleAction(Component.text("Create Auction")));
        }

        // Basic setup
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        setupBackButton();
        setupItemSlot(escrow);
        setupDurationButton(escrow);
        setupAuctionTypeButton(escrow);
        setupCreateButton(escrow);
        setupPriceButton(escrow);

        // Start refresh loop
        startLoop("refresh", 10, () -> refreshItems(player));
    }

    private void setupBackButton() {
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitleAsString()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());
    }

    private void setupItemSlot(DatapointAuctionEscrow.AuctionEscrow escrow) {
        attachItem(GUIItem.builder(13)
                .item(() -> {
                    if (escrow.getItem() == null) {
                        return ItemStackCreator.getStack("§eClick an item in your inventory!",
                                Material.STONE_BUTTON, 1,
                                "§7Selects it for auction").build();
                    }

                    SkyBlockItem item = escrow.getItem();
                    ItemStack itemStack = new NonPlayerItemUpdater(item).getUpdatedItem().build();
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    lore.add(StringUtility.getTextFromComponent(itemStack.get(ItemComponent.CUSTOM_NAME)));
                    itemStack.get(ItemComponent.LORE).forEach(loreEntry ->
                            lore.add(StringUtility.getTextFromComponent(loreEntry)));
                    lore.add(" ");
                    lore.add("§eClick to pickup!");

                    return ItemStackCreator.getStack("§a§lAUCTION FOR ITEM:",
                            itemStack.material(), itemStack.amount(), lore).build();
                })
                .onClick((ctx, item) -> {
                    if (escrow.getItem() == null) return true;
                    ctx.player().addAndUpdateItem(escrow.getItem());
                    escrow.setItem(null);
                    ctx.player().openInventory(new GUIAuctionCreateItem(previousGUI));
                    return true;
                })
                .build());
    }

    private void setupDurationButton(DatapointAuctionEscrow.AuctionEscrow escrow) {
        attachItem(GUIItem.builder(33)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    if (hasState(STATE_BIN_MODE)) {
                        lore.add("§7How long the item will be");
                        lore.add("§7up for sale.");
                    } else {
                        lore.add("§7How long players will be");
                        lore.add("§7able to place bids for.");
                        lore.add(" ");
                        lore.add("§7Note: Bids automatically");
                        lore.add("§7increase the duration of");
                        lore.add("§7auctions.");
                    }
                    lore.add(" ");
                    lore.add("§7Extra fee: §6+" + (escrow.getDuration() / 180000) + " coins");
                    lore.add(" ");
                    lore.add("§eClick to edit!");

                    return ItemStackCreator.getStack(
                            "Duration: §e" + StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()),
                            Material.CLOCK, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionDuration());
                    return true;
                })
                .build());
    }

    private void setupAuctionTypeButton(DatapointAuctionEscrow.AuctionEscrow escrow) {
        attachItem(GUIItem.builder(48)
                .item(() -> {
                    if (hasState(STATE_BIN_MODE)) {
                        return ItemStackCreator.getStack("§aSwitch to Auction", Material.POWERED_RAIL, 1,
                                "§7With traditional auctions, multiple",
                                "§7buyers compete for the item by",
                                "§7bidding turn by turn.",
                                " ",
                                "§eClick to switch!").build();
                    } else {
                        return ItemStackCreator.getStack("§aSwitch to BIN", Material.GOLD_INGOT, 1,
                                "§7BIN Auctions are simple.",
                                " ",
                                "§7Set a price, then one player may buy",
                                "§7the item at that price.",
                                " ",
                                "§8(BIN means Buy It Now)",
                                " ",
                                "§eClick to switch!").build();
                    }
                })
                .onClick((ctx, item) -> {
                    escrow.setBin(!escrow.isBin());
                    ctx.player().openInventory(new GUIAuctionCreateItem(previousGUI));
                    return true;
                })
                .build());
    }

    private void setupCreateButton(DatapointAuctionEscrow.AuctionEscrow escrow) {
        attachItem(GUIItem.builder(29)
                .item(() -> {
                    if (!hasState(STATE_HAS_ITEM)) {
                        return ItemStackCreator.getStack("§cCreate Auction", Material.RED_TERRACOTTA, 1,
                                "§7No item selected!",
                                " ",
                                "§7Click an item in your inventory to",
                                "§7select it for this auction.").build();
                    }

                    ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();
                    return ItemStackCreator.getStack("§aCreate " + (hasState(STATE_BIN_MODE) ? "Bin " : "") + "Auction",
                            Material.GREEN_TERRACOTTA, 1,
                            "§7This item will be added to the auction",
                            "§7house for other players to",
                            "§7purchase.",
                            " ",
                            "§7Item: " + StringUtility.getTextFromComponent(builtItem.get(ItemComponent.CUSTOM_NAME)),
                            "§7Auction Duration: §e" + StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()),
                            "§7" + (hasState(STATE_BIN_MODE) ? "Item Price" : "Starting bid") + ": §e" +
                                    StringUtility.commaify(escrow.getPrice()) + " coins",
                            " ",
                            "§7Creation fee: §6+" + ((escrow.getPrice() * 0.05) +
                                    (escrow.getDuration() / 180000)) + " coins",
                            " ",
                            "§eClick to submit!").build();
                })
                .onClick((ctx, item) -> handleCreateAuction(ctx.player()))
                .requireState(STATE_HAS_ITEM)
                .build());
    }

    private void setupPriceButton(DatapointAuctionEscrow.AuctionEscrow escrow) {
        attachItem(GUIItem.builder(31)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    Material material;

                    if (hasState(STATE_BIN_MODE)) {
                        material = Material.GOLD_INGOT;
                        lore.add("§7The price at which you want");
                        lore.add("§7to sell this item.");
                    } else {
                        material = Material.POWERED_RAIL;
                        lore.add("§7The minimum price a player");
                        lore.add("§7can offer to obtain your");
                        lore.add("§7item.");
                        lore.add(" ");
                        lore.add("§7Once a player bids for your");
                        lore.add("§7item, other players will");
                        lore.add("§7have until the auction ends");
                        lore.add("§7to make a higher bid.");
                    }
                    lore.add(" ");
                    lore.add("§7Extra fee: §6+" + (escrow.getPrice() * 0.05) + " coins §e(5%)");
                    lore.add(" ");
                    lore.add("§eClick to edit!");

                    return ItemStackCreator.getStack(
                            (hasState(STATE_BIN_MODE) ? "Item Price: " : "Starting bid: ") +
                                    "§e" + StringUtility.commaify(escrow.getPrice()) + " coins",
                            material, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    SkyBlockSignGUI signGUI = new SkyBlockSignGUI(ctx.player());
                    signGUI.open(new String[]{"Enter price"}).thenAccept(lines -> {
                        handlePriceInput(ctx.player(), lines, escrow);
                    });
                    return true;
                })
                .build());
    }

    private void handlePriceInput(SkyBlockPlayer player, String input, DatapointAuctionEscrow.AuctionEscrow escrow) {
        try {
            long price = Long.parseLong(input);
            if (price <= 50) {
                player.sendMessage("§cThat price is too low for your item!");
                return;
            }
            escrow.setPrice(price);
            player.openInventory(this);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cCould not read this number!");
        }
    }

    private boolean handleCreateAuction(SkyBlockPlayer player) {
        ProxyService auctionService = new ProxyService(ServiceType.AUCTION_HOUSE);
        DatapointAuctionEscrow.AuctionEscrow escrow = player.getDataHandler().get(
                DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        auctionService.isOnline().thenAccept((response) -> {
            if (escrow.getItem() == null || !response) return;

            long fee = (long) ((escrow.getPrice() * 0.05) + (escrow.getDuration() / 180000));
            DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);

            if (coins.getValue() < fee) {
                player.sendMessage("§cYou don't have enough coins to create this auction!");
                return;
            }

            coins.setValue(coins.getValue() - fee);
            player.closeInventory();
            player.sendMessage("§7Putting item in escrow...");

            ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();
            String itemName = StringUtility.getTextFromComponent(builtItem.get(ItemComponent.CUSTOM_NAME));

            AuctionItem item = new AuctionItem(
                    escrow.getItem().toUnderstandable(),
                    player.getUuid(),
                    escrow.getDuration() + System.currentTimeMillis(),
                    escrow.isBin(),
                    escrow.getPrice()
            );

            AuctionCategories category = AuctionCategories.TOOLS;
            if (escrow.getItem().hasComponent(AuctionCategoryComponent.class)) {
                category = escrow.getItem().getComponent(AuctionCategoryComponent.class).getCategory();
            }

            player.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).clearEscrow();
            player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().add(item.getUuid());

            player.sendMessage("§7Setting up the auction...");

            AuctionAddItemProtocolObject.AuctionAddItemMessage message = new AuctionAddItemProtocolObject.AuctionAddItemMessage(item, category);
            CompletableFuture<AuctionAddItemProtocolObject.AuctionAddItemResponse> future = auctionService.handleRequest(message);
            UUID auctionUUID = future.join().uuid();

            player.sendMessage("§eAuction started for " + itemName + "§e!");
            player.sendMessage("§8ID: " + auctionUUID);
        });
        return true;
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        ItemStack clicked = event.getClickedItem();
        SkyBlockItem item = new SkyBlockItem(clicked);

        if (item.isNA() || item.isAir()) {
            return;
        }

        DatapointAuctionEscrow.AuctionEscrow escrow = ((SkyBlockPlayer) event.getPlayer()).getDataHandler()
                .get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        if (escrow.getItem() != null) {
            event.getPlayer().sendMessage("§cYou already have an item in the auction slot!");
            return;
        }

        event.setCancelled(true);
        escrow.setItem(item);
        event.getPlayer().getInventory().setItemStack(event.getSlot(), ItemStack.AIR);
        ((SkyBlockPlayer) event.getPlayer()).openInventory(new GUIAuctionCreateItem(previousGUI));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}