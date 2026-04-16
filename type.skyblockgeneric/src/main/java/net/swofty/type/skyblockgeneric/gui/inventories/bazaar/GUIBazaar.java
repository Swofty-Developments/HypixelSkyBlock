package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.bazaar.BazaarCategories;
import net.swofty.type.skyblockgeneric.bazaar.BazaarItemSet;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GUIBazaar extends HypixelInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = {
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };
    private static final long CACHE_TTL_MS = 30_000L;

    private static final Map<BazaarCategories, CacheEntry> CACHE = new ConcurrentHashMap<>();

    private final BazaarCategories category;

    public GUIBazaar(BazaarCategories category) {
        super(I18n.t("gui_bazaar.main.title", Component.text(StringUtility.toNormalCase(category.name()))),
                InventoryType.CHEST_6_ROW);
        this.category = category;

        fill(ItemStackCreator.createNamedItemStack(category.getGlassItem()));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarOrders().open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack("gui_bazaar.main.manage_orders_button",
                        Material.BOOK, 1,
                        "gui_bazaar.main.manage_orders_button.lore");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        // Tabs at top
        renderCategoryTabs();

        long now = System.currentTimeMillis();
        CacheEntry entry = CACHE.get(category);

        if (entry != null && now - entry.timestamp <= CACHE_TTL_MS) {
            // fresh cache: render immediately
            renderSlots(entry.slots);
        } else {
            // expired or missing: clear cache & show placeholders
            CACHE.remove(category);
            renderPlaceholders();
            // async rebuild & render
            CompletableFuture.runAsync(() -> rebuildCacheAndRender((SkyBlockPlayer) e.player()));
        }
    }

    private void renderCategoryTabs() {
        int idx = 0;
        for (BazaarCategories cat : BazaarCategories.values()) {
            int slot = idx * 9;
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIBazaar(cat).open(p);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    Locale l = p.getLocale();
                    var b = ItemStackCreator.getStack(
                            cat.getColor() + StringUtility.toNormalCase(cat.name()),
                            cat.getDisplayItem(), 1,
                            I18n.string("gui_bazaar.main.category_subtitle", l), " ",
                            category == cat
                                    ? I18n.string("gui_bazaar.main.category_viewing", l)
                                    : I18n.string("gui_bazaar.main.category_click", l)
                    );
                    if (category == cat) b = ItemStackCreator.enchant(b);
                    return b;
                }
            });
            idx++;
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    private void renderPlaceholders() {
        for (int slot : SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return TranslatableItemStackCreator.getStack("gui_bazaar.main.loading", Material.GRAY_STAINED_GLASS_PANE, 1);
                }
            });
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    private void rebuildCacheAndRender(SkyBlockPlayer player) {
        List<BazaarItemSet> sets = category.getItems().stream().toList();

        // Create a thread-safe map to collect results
        Map<BazaarItemSet, Map<ItemType, PriceData>> setDataMap = new ConcurrentHashMap<>();

        // Initialize the map
        for (BazaarItemSet set : sets) {
            setDataMap.put(set, new ConcurrentHashMap<>());
        }

        // Collect all futures for price fetching
        List<CompletableFuture<Void>> allFutures = new ArrayList<>();

        for (BazaarItemSet set : sets) {
            for (ItemType type : set.items) {
                CompletableFuture<Void> future = player.getBazaarConnector().getItemStatistics(type)
                        .thenAccept(stats -> {
                            // Store the price data thread-safely
                            setDataMap.get(set).put(type, new PriceData(
                                    type,
                                    stats.averageAsk(),  // Average sell price
                                    stats.averageBid()    // Average buy price
                            ));
                        })
                        .exceptionally(throwable -> {
                            System.err.println("Failed to fetch bazaar data for " + type.name() + ": " + throwable.getMessage());
                            // Store empty data on failure
                            setDataMap.get(set).put(type, new PriceData(type, 0, 0));
                            return null;
                        });
                allFutures.add(future);
            }
        }

        // When all price data is collected, build the cache entry
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    Locale l = player.getLocale();
                    List<CacheEntry.CachedSlot> slotData = new ArrayList<>();

                    for (int i = 0; i < sets.size() && i < SLOTS.length; i++) {
                        int slot = SLOTS[i];
                        BazaarItemSet set = sets.get(i);

                        List<String> lore = new ArrayList<>();
                        lore.add(I18n.string("gui_bazaar.main.item_set_products", l, Component.text(String.valueOf(set.items.size()))));
                        lore.add(" ");

                        // Add price data for each item in the set
                        Map<ItemType, PriceData> priceDataMap = setDataMap.get(set);
                        for (ItemType type : set.items) {
                            PriceData priceData = priceDataMap.get(type);
                            if (priceData != null) {
                                lore.add(type.rarity.getColor()
                                        + "▶ §7" + type.getDisplayName()
                                        + " §c" + StringUtility.shortenNumber(priceData.sellPrice())
                                        + " §8| §a" + StringUtility.shortenNumber(priceData.buyPrice()));
                            }
                        }

                        lore.add(" ");
                        lore.add(I18n.string("gui_bazaar.main.item_set_click", l));
                        slotData.add(new CacheEntry.CachedSlot(slot, set, lore));
                    }

                    // Cache it
                    CACHE.put(category, new CacheEntry(System.currentTimeMillis(), slotData));

                    // Schedule UI update on main thread
                    MathUtility.delay(() -> {
                        renderSlots(slotData);
                    }, 1);
                })
                .exceptionally(throwable -> {
                    System.err.println("Failed to rebuild bazaar cache: " + throwable.getMessage());

                    // Fallback: render with "Error loading" placeholders
                    MathUtility.delay(() -> {
                        for (int slot : SLOTS) {
                            set(new GUIItem(slot) {
                                @Override
                                public ItemStack.Builder getItem(HypixelPlayer p) {
                                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                                    return TranslatableItemStackCreator.getStack("gui_bazaar.main.error_loading", Material.BARRIER, 1,
                                            "gui_bazaar.main.error_loading.lore");
                                }
                            });
                        }
                        updateItemStacks(getInventory(), getPlayer());
                    }, 1);
                    return null;
                });
    }

    private void renderSlots(List<CacheEntry.CachedSlot> slots) {
        // Clear existing slots first
        for (int slot : SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStack.builder(Material.AIR);
                }
            });
        }

        // Set the new slots
        for (CacheEntry.CachedSlot cs : slots) {
            set(new GUIClickableItem(cs.slot()) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIBazaarItemSet(category, cs.itemSet()).open(p);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            category.getColor() + cs.itemSet().displayName,
                            cs.itemSet().displayMaterial.material,
                            1,
                            cs.lore()
                    );
                }
            });
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    /**
     * Thread-safe price data holder
     */
    private record PriceData(ItemType itemType, double sellPrice, double buyPrice) {
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    @Override
    public void refreshItems(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        player.getBazaarConnector().isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage(I18n.t("gui_bazaar.main.offline_message"));
                player.closeInventory();
            } else {
                player.getBazaarConnector().processAllPendingTransactions();
            }
        });
    }

    private record CacheEntry(long timestamp, List<CachedSlot> slots) {
        record CachedSlot(int slot, BazaarItemSet itemSet, List<String> lore) {
        }
    }
}
