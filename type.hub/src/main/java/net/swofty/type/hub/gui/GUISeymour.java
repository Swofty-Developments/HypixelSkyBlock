package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.color.Color;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUISeymour extends SkyBlockAbstractInventory {
    private static final String STATE_CAN_AFFORD_CHEAP = "can_afford_cheap";
    private static final String STATE_CAN_AFFORD_FANCY = "can_afford_fancy";
    private static final String STATE_CAN_AFFORD_ELEGANT = "can_afford_elegant";

    public GUISeymour() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Seymour's Fancy Suits")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Set states based on player's coins
        double coins = player.getCoins();
        if (coins >= 3000000) doAction(new AddStateAction(STATE_CAN_AFFORD_CHEAP));
        if (coins >= 20000000) doAction(new AddStateAction(STATE_CAN_AFFORD_FANCY));
        if (coins >= 74999999) doAction(new AddStateAction(STATE_CAN_AFFORD_ELEGANT));

        // Cheap Tuxedo
        setupTuxedoItem(11, "§5Cheap Tuxedo", 3000000, new Color(56, 56, 56),
                new String[]{
                        "§8Complete suit",
                        "§7Crit Damage: §c+100%",
                        "§7Intelligence: §a+100",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c75♥§7.",
                        "§7Deal §c+50% §7damage!",
                        "§8Very stylish."
                },
                STATE_CAN_AFFORD_CHEAP,
                new ItemType[]{
                        ItemType.CHEAP_TUXEDO_CHESTPLATE,
                        ItemType.CHEAP_TUXEDO_BOOTS,
                        ItemType.CHEAP_TUXEDO_LEGGINGS
                });

        // Fancy Tuxedo
        setupTuxedoItem(13, "§6Fancy Tuxedo", 20000000, new Color(51, 42, 42),
                new String[]{
                        "§8Complete suit",
                        "§7Crit Damage: §c+150%",
                        "§7Intelligence: §a+300",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c150♥§7.",
                        "§7Deal §c+100% §7damage!",
                        "§8Very stylish."
                },
                STATE_CAN_AFFORD_FANCY,
                new ItemType[]{
                        ItemType.FANCY_TUXEDO_CHESTPLATE,
                        ItemType.FANCY_TUXEDO_BOOTS,
                        ItemType.FANCY_TUXEDO_LEGGINGS
                });

        // Elegant Tuxedo
        setupTuxedoItem(15, "§6Elegant Tuxedo", 74999999, new Color(25, 25, 25),
                new String[]{
                        "§8Complete suit",
                        "§7Crit Damage: §c+200%",
                        "§7Intelligence: §a+500",
                        "",
                        "§6Full Set Bonus: Dashing §7(0/3)",
                        "§7Max Health set to §c1250♥§7.",
                        "§7Deal §c+150% §7damage!",
                        "§8Very stylish."
                },
                STATE_CAN_AFFORD_ELEGANT,
                new ItemType[]{
                        ItemType.ELEGANT_TUXEDO_CHESTPLATE,
                        ItemType.ELEGANT_TUXEDO_BOOTS,
                        ItemType.ELEGANT_TUXEDO_LEGGINGS
                });
    }

    private void setupTuxedoItem(int slot, String name, double cost, Color color, String[] description,
                                 String affordState, ItemType[] items) {
        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder builder = ItemStackCreator.getStack(name, Material.LEATHER_CHESTPLATE, 1);
                    builder.set(ItemComponent.DYED_COLOR, new DyedItemColor(color));

                    java.util.List<String> lore = new java.util.ArrayList<>();
                    lore.add("");
                    java.util.Collections.addAll(lore, description);
                    lore.add("");
                    lore.add("§7Cost: §6" + cost + " Coins");
                    lore.add("");
                    lore.add(hasState(affordState) ? "§eClick to purchase" : "§cCan't afford this!");

                    return ItemStackCreator.updateLore(builder, lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (!hasState(affordState)) return true;

                    SkyBlockPlayer player = ctx.player();
                    double coins = player.getCoins();

                    for (ItemType item : items) {
                        player.addAndUpdateItem(item);
                    }

                    player.playSuccessSound();
                    player.setCoins(coins - cost);
                    player.closeInventory();
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}