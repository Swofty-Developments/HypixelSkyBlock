package net.swofty.type.skyblockgeneric.gui.inventories.museum;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIYourMuseum extends HypixelInventoryGUI {
    private static final Map<MuseumableItemCategory, Integer> CATEGORY_SLOTS = Map.of(
            MuseumableItemCategory.WEAPONS, 20,
            MuseumableItemCategory.ARMOR_SETS, 22,
            MuseumableItemCategory.RARITIES, 24
    );

    public GUIYourMuseum() {
        super(I18n.t("gui_museum.main.title"), InventoryType.CHEST_6_ROW);
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
            e.player().sendMessage(I18n.t("gui_museum.main.item_tracker_offline"));
            e.player().closeInventory();
            return;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointMuseum.MuseumData data = player.getMuseumData();
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(40) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIMuseumRewards().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack("gui_museum.main.rewards_button", Material.GOLD_BLOCK, 1,
                        "gui_museum.main.rewards_button.lore");
            }
        });
        set(new GUIItem(45) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack("gui_museum.main.edit_npc_tags", Material.NAME_TAG, 1,
                        "gui_museum.main.edit_npc_tags.lore");
            }
        });
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>(I18n.lore("gui_museum.main.museum_info.lore", l));

                int maxAmountOfItems = MuseumableItemCategory.getMuseumableItemCategorySize();
                int unlockedItems = data.getAllItems().size();

                double percentageUnlocked = (double) unlockedItems / (double) maxAmountOfItems * 100;
                double percentageUnlockedToTwoDecimalPlaces = Math.round(percentageUnlocked * 100) / 100.0;

                lore.add(I18n.string("gui_museum.main.items_donated", l, Component.text(String.valueOf(percentageUnlockedToTwoDecimalPlaces))));
                lore.add(getAsDisplay(unlockedItems, maxAmountOfItems));

                Map<UUID, Double> calculatedPrices = data.getCalculatedPrices();
                if (!calculatedPrices.isEmpty()) {
                    lore.add(" ");
                    lore.add(I18n.string("gui_museum.main.top_items", l));

                    AtomicInteger index = new AtomicInteger(1);
                    calculatedPrices.entrySet().stream()
                            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                            .limit(5)
                            .forEach(entry -> {
                                SkyBlockItem item = data.getFromUUID(entry.getKey());
                                lore.add("§8" + index + ". " + item.getDisplayName());
                                lore.add("§8    " + StringUtility.commaify(entry.getValue()) + " Coins");

                                index.getAndIncrement();
                            });
                }

                return ItemStackCreator.getStackHead(I18n.string("gui_museum.main.museum_info", l),
                        "597e4e27a04afa5f06108265a9bfb797630391c7f3d880d244f610bb1ff393d8",
                        1, lore);
            }
        });

        for (MuseumableItemCategory category : MuseumableItemCategory.values()) {
            Integer slot = CATEGORY_SLOTS.get(category);
            if (slot == null) {
                continue;
            }

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (category == MuseumableItemCategory.ARMOR_SETS)
                        new GUIMuseumArmorCategory().open(player);
                    else new GUIMuseumCategory(category).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    Locale l = p.getLocale();
                    List<String> lore = new ArrayList<>(I18n.lore("gui_museum.main.category_button.lore_prefix", l,
                        Component.text(category.getColor()), Component.text(category.getCategory())));

                    int maxAmountOfItems = MuseumableItemCategory.getMuseumableItemCategorySize(category);
                    int unlockedItems = data.getItemsByCategory(category).size();
                    double percentage = (double) unlockedItems / (double) maxAmountOfItems * 100;
                    double percentageToTwoDecimalPlaces = Math.round(percentage * 100) / 100.0;

                    lore.add(I18n.string("gui_museum.main.category_items_donated", l, Component.text(String.valueOf(percentageToTwoDecimalPlaces))));
                    lore.add(getAsDisplay(unlockedItems, maxAmountOfItems));
                    lore.add(" ");
                    lore.add(I18n.string("gui_museum.main.category_click", l));

                    return ItemStackCreator.getStack("§a" + category.getCategory(),
                            category.getMaterial(), 1,
                            lore);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    public static String getAsDisplay(int unlocked, int total) {
        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((unlocked / (double) total) * maxBarLength);

        String completedLoadingBar = "§b§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
        int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
        String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                completedLoadingBar.length() - formattingCodeLength, // Adjust for added formatting codes
                maxBarLength
        ));

        return (completedLoadingBar + uncompletedLoadingBar + "§r §b" + unlocked + "§9/§b" + total);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
