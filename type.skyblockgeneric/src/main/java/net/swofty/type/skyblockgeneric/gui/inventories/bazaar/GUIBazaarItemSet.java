package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.bazaar.BazaarCategories;
import net.swofty.type.skyblockgeneric.bazaar.BazaarItemSet;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.EnchantedComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.swofty.type.generic.gui.inventory.ItemStackCreator.*;

public class GUIBazaarItemSet extends HypixelInventoryGUI implements RefreshingGUI {
    private static final Map<Integer, int[]> SLOTS = Map.of(
            1, new int[]{13},
            2, new int[]{12, 14},
            3, new int[]{11, 13, 15},
            4, new int[]{10, 12, 14, 16},
            5, new int[]{11, 12, 13, 14, 15},
            6, new int[]{11, 12, 13, 14, 15, 22},
            7, new int[]{10, 11, 12, 13, 14, 15, 16},
            8, new int[]{11, 12, 13, 14, 15, 21, 22, 23},
            9, new int[]{10, 11, 12, 13, 14, 15, 16, 21, 23},
            10, new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24}
    );

    private final BazaarItemSet itemSet;

    public GUIBazaarItemSet(BazaarCategories category, BazaarItemSet itemSet) {
        super(StringUtility.toNormalCase(category.name()) + " -> " + itemSet.displayName, InventoryType.CHEST_4_ROW);

        this.itemSet = itemSet;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaar(category)));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarOrders().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return getStack("§aManage Orders", Material.BOOK, 1,
                        "§7View your pending Bazaar orders",
                        " ",
                        "§eClick to manage!");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        int i = 0;
        for (ItemType itemType : itemSet.items) {
            int slot = SLOTS.get(itemSet.items.size())[i];

            CompletableFuture<Void> future = ((SkyBlockPlayer) e.player()).getBazaarConnector().getItemStatistics(itemType)
                    .thenAccept(stats -> {
                        set(new GUIClickableItem(slot) {
                            @Override
                            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                new GUIBazaarItem(itemType).open(player);
                            }

                            @Override
                            public ItemStack.Builder getItem(HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                List<String> lore = new ArrayList<>();
                                lore.add("§8" + StringUtility.toNormalCase(itemType.rarity.name()) + " commodity");
                                lore.add(" ");

                                // Buy price (what you pay - from sell orders)
                                if (stats.bestAsk() > 0) {
                                    lore.add("§7Buy price: §6" +
                                            new DecimalFormat("#,###").format(stats.bestAsk())
                                            + " coins");
                                    lore.add("§8" + StringUtility.shortenNumber(stats.bestAsk())
                                            + " best offer");
                                } else {
                                    lore.add("§7Buy price: §cNo offers");
                                    lore.add("§8No sell orders available");
                                }

                                lore.add(" ");

                                // Sell price (what you get - from buy orders)
                                if (stats.bestBid() > 0) {
                                    lore.add("§7Sell price: §6" +
                                            new DecimalFormat("#,###").format(stats.bestBid())
                                            + " coins");
                                    lore.add("§8" + StringUtility.shortenNumber(stats.bestBid())
                                            + " best bid");
                                } else {
                                    lore.add("§7Sell price: §cNo orders");
                                    lore.add("§8No buy orders available");
                                }

                                lore.add(" ");
                                lore.add("§eClick to view details!");

                                SkyBlockItem item = new SkyBlockItem(itemType);

                                return ItemStackCreator.updateLore(getFromSkyBlockItem(item), lore);
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        // Handle errors gracefully
                        set(new GUIClickableItem(slot) {
                            @Override
                            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                new GUIBazaarItem(itemType).open(player);
                            }

                            @Override
                            public ItemStack.Builder getItem(HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                List<String> lore = new ArrayList<>();
                                lore.add("§8" + StringUtility.toNormalCase(itemType.rarity.name()) + " commodity");
                                lore.add(" ");
                                lore.add("§cError loading market data");
                                lore.add("§7Please try again later");
                                lore.add(" ");
                                lore.add("§eClick to view details!");

                                return getStack(
                                        itemType.rarity.getColor() + itemType.getDisplayName(),
                                        itemType.material, 1, lore);
                            }
                        });
                        return null;
                    });

            futures.add(future);
            i++;
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> updateItemStacks(getInventory(), getPlayer()));
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
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getPotentialType();
        e.setCancelled(true);

        if (clickedItem.isNA()) {
            return;
        }

        if (type == null) {
            return;
        }

        Map.Entry<BazaarCategories, BazaarItemSet> entry = BazaarCategories.getFromItem(type);

        if (entry == null) {
            return;
        }

        Thread.startVirtualThread(() -> {
            new GUIBazaarItemSet(entry.getKey(), entry.getValue()).open((SkyBlockPlayer) e.getPlayer());
        });
    }

    @Override
    public void refreshItems(HypixelPlayer p) {
        if (!(p instanceof SkyBlockPlayer player)) {
            return;
        }
        player.getBazaarConnector().isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage("§cThe Bazaar is currently offline!");
                player.closeInventory();
            }
        });
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    /**
     * Creates an {@link ItemStack.Builder} from an existing {@link SkyBlockItem}.
     *
     * @param item the original {@link SkyBlockItem} to create a builder from
     * @return an {@link ItemStack.Builder} with the properties of the original item
     * @implNote moved from {@link ItemStackCreator} to here until a better place is found
     */
    private ItemStack.Builder getFromSkyBlockItem(SkyBlockItem item) {
        ItemStack.Builder builder;

        if (item.hasComponent(SkullHeadComponent.class)) {
            builder = getStackHead(item.getDisplayName(), item.getComponent(SkullHeadComponent.class).getSkullTexture(item), item.getAmount());
        } else {
            builder = getStack(item.getDisplayName(), item.getMaterial(), item.getAmount());
        }

        if (item.hasComponent(EnchantedComponent.class)) return enchant(builder);
        else return builder;
    }
}

