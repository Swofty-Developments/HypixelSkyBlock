package net.swofty.types.generic.minion.extension.extensions;

import lombok.NonNull;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetCursorItemAction;
import net.swofty.types.generic.gui.inventory.inventories.GUIMinion;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.MinionShippingComponent;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MinionShippingExtension extends MinionExtension {
    private double heldCoins = 0;
    private double itemsSold = 0;

    public MinionShippingExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        super(itemTypeLinker, data);

        if (data != null) {
            String[] split = ((String) data).split(":");
            setItemTypePassedIn(ItemType.valueOf(split[0]));
            itemsSold = Double.parseDouble(split[1]);
        }
    }

    public void addCoins(double coins) {
        heldCoins += coins;
        itemsSold += 1;
    }

    @Override
    public @NonNull GUIItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot, SkyBlockAbstractInventory inventory) {
        if (getItemTypePassedIn() == null) {
            return GUIItem.builder(slot)
                    .item(ItemStackCreator.getStack("§aAutomated Shipping", Material.BLUE_STAINED_GLASS_PANE, 1,
                            "§7Add a §aBudget Hopper §7or",
                            "§9Enchanted Hopper §7here to make",
                            "§7your minion automatically sell",
                            "§7generated items after its",
                            "§7inventory is full.").build())
                    .onClick((ctx, itemClicked) -> {
                        SkyBlockItem shippingItem = new SkyBlockItem(ctx.cursorItem());

                        if (!shippingItem.hasComponent(MinionShippingComponent.class)) {
                            ctx.player().sendMessage("§cThis item is not a valid Minion Shipping item.");
                            return false;
                        }

                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(inventory);
                        setItemTypePassedIn(shippingItem.getAttributeHandler().getPotentialType());
                        minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                        ctx.player().openInventory(new GUIMinion(minion));
                        return true;
                    })
                    .build();
        }

        return GUIItem.builder(slot)
                .item(() -> {
                    SkyBlockItem shippingItem = new SkyBlockItem(getItemTypePassedIn());
                    ArrayList<String> lore = new ArrayList<>(shippingItem.getLore());

                    lore.add(" ");
                    lore.add("§7Items Sold: §b" + StringUtility.commaify(itemsSold));
                    lore.add("§7Held Coins: §b" + StringUtility.commaify(heldCoins));
                    lore.add(" ");
                    lore.add("§bRight-click to get held coins.");
                    lore.add("§eClick to remove.");

                    return ItemStackCreator.getStack(shippingItem.getDisplayName(),
                            shippingItem.getMaterial(), 1, lore).build();
                })
                .onClick((ctx, itemClicked) -> {
                    if (!ctx.cursorItem().isAir()) {
                        ctx.player().sendMessage("§cYour cursor must be empty to pick this item up!");
                        return false;
                    }

                    if (ctx.clickType().equals(ClickType.RIGHT_CLICK)) {
                        if (heldCoins == 0) return true;

                        ctx.player().setCoins(ctx.player().getCoins() + heldCoins);
                        ctx.player().sendMessage("§aYou have received §6" + StringUtility.commaify(heldCoins) + " coins§a from your Minion!");
                        heldCoins = 0;
                        return true;
                    }

                    ctx.player().addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    itemsSold = 0;
                    minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                    return true;
                })
                .onPostClick((player) -> {
                    player.openInventory(new GUIMinion(minion));
                })
                .build();
    }

    @Override
    public String toString() {
        if (getItemTypePassedIn() == null)
            return "null";
        return getItemTypePassedIn().toString() + ":" + itemsSold + ":" + heldCoins;
    }

    @Override
    public void fromString(String string) {
        if (string.equals("null"))
            return;
        String[] split = string.split(":");
        setItemTypePassedIn(ItemType.valueOf(split[0]));
        itemsSold = Double.parseDouble(split[1]);
        heldCoins = Double.parseDouble(split[2]);
    }
}