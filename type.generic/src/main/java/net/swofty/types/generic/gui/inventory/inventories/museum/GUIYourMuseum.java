package net.swofty.types.generic.gui.inventory.inventories.museum;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIYourMuseum extends SkyBlockAbstractInventory {
    private static final String STATE_SERVICE_ONLINE = "service_online";
    private static final int[] SLOTS = new int[]{20, 22, 24};

    public GUIYourMuseum() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Your Museum")));
    }

    @SneakyThrows
    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Check service availability
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
            player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
            player.closeInventory();
            return;
        }
        doAction(new AddStateAction(STATE_SERVICE_ONLINE));

        // Base setup
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        setupGeneralItems(player);
        setupCategoryItems(player);
    }

    private void setupGeneralItems(SkyBlockPlayer player) {
        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .requireState(STATE_SERVICE_ONLINE)
                .build());

        // Museum rewards
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.getStack("§6Museum Rewards", Material.GOLD_BLOCK, 1,
                        "§7Each time you donate an item to your",
                        "§7Museum, the §bCurator §7will reward you.",
                        " ",
                        "§dSpecial Items §ddo not count towards",
                        "§7your Museum rewards progress.",
                        " ",
                        "§7Currently, most rewards are §ccoming",
                        "§csoon§7, but you can view them anyway.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIMuseumRewards());
                    return true;
                })
                .requireState(STATE_SERVICE_ONLINE)
                .build());

        // NPC Tags
        attachItem(GUIItem.builder(45)
                .item(ItemStackCreator.getStack("§aEdit NPC Tags", Material.NAME_TAG, 1,
                        "§7Edit the tags that appear above",
                        "§7your NPC. Show off your SkyBlock",
                        "§7progress with tags showing your",
                        "§7highest collection, best Skill, and",
                        "§7more!",
                        " ",
                        "§cCOMING SOON").build())
                .requireState(STATE_SERVICE_ONLINE)
                .build());

        // Museum Info
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    DatapointMuseum.MuseumData data = player.getMuseumData();
                    List<String> lore = new ArrayList<>(List.of(
                            "§7The §9Museum §7is a compendium of all of",
                            "§7your items in SkyBlock. Donate items",
                            "§7to your Museum to unlock rewards.",
                            " ",
                            "§7Other players can visit your Museum",
                            "§7at any time! Display your best items",
                            "§7proudly for all to see.",
                            " "
                    ));

                    int maxAmountOfItems = MuseumableItemCategory.getMuseumableItemCategorySize();
                    int unlockedItems = data.getAllItems().size();
                    double percentageUnlocked = (double) unlockedItems / (double) maxAmountOfItems * 100;

                    lore.add("§7Items Donated: §e" + percentageUnlocked + "§6%");
                    lore.add(getAsDisplay(unlockedItems, maxAmountOfItems));

                    addTopItemsToLore(lore, data);

                    return ItemStackCreator.getStackHead("§9Museum",
                            "597e4e27a04afa5f06108265a9bfb797630391c7f3d880d244f610bb1ff393d8",
                            1, lore).build();
                })
                .requireState(STATE_SERVICE_ONLINE)
                .build());
    }

    private void setupCategoryItems(SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        for (MuseumableItemCategory category : MuseumableItemCategory.values()) {
            int slot = SLOTS[category.ordinal()];

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        List<String> lore = new ArrayList<>(List.of(
                                "§7View all of the " + category.getColor() + category.getCategory() + " §7that you",
                                "§7have donated to the §9Museum§7!",
                                " "
                        ));

                        int maxAmountOfItems = MuseumableItemCategory.getMuseumableItemCategorySize(category);
                        int unlockedItems = data.getAllItems(category).size();
                        double percentage = (double) unlockedItems / (double) maxAmountOfItems * 100;

                        lore.add("§7Items Donated: §e" + percentage + "§6%");
                        lore.add(getAsDisplay(unlockedItems, maxAmountOfItems));
                        lore.add(" ");
                        lore.add("§eClick to view!");

                        return ItemStackCreator.getStack("§a" + category.getCategory(),
                                category.getMaterial(), 1, lore).build();
                    })
                    .onClick((ctx, item) -> {
                        if (category == MuseumableItemCategory.ARMOR_SETS) {
                            ctx.player().openInventory(new InventoryMuseumArmorCategory());
                        } else {
                            ctx.player().openInventory(new InventoryMuseumCategory(category));
                        }
                        return true;
                    })
                    .requireState(STATE_SERVICE_ONLINE)
                    .build());
        }
    }

    private void addTopItemsToLore(List<String> lore, DatapointMuseum.MuseumData data) {
        Map<UUID, Double> calculatedPrices = data.getCalculatedPrices();
        if (!calculatedPrices.isEmpty()) {
            lore.add(" ");
            lore.add("§7Top Items");

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
    }

    public static String getAsDisplay(int unlocked, int total) {
        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((unlocked / (double) total) * maxBarLength);

        String completedLoadingBar = "§b§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
        int formattingCodeLength = 4;
        String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                completedLoadingBar.length() - formattingCodeLength,
                maxBarLength
        ));

        return (completedLoadingBar + uncompletedLoadingBar + "§r §b" + unlocked + "§9/§b" + total);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}