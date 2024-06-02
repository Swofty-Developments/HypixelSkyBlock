package net.swofty.types.generic.minion.extension.extensions;

import lombok.NonNull;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.GUIMinion;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MinionShippingItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MinionShippingExtension extends MinionExtension {
    private double heldCoins = 0;
    private double itemsSold = 0;

    public MinionShippingExtension(@Nullable ItemType itemType, @Nullable Object data) {
        super(itemType, data);

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
    public @NonNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot) {
        if (getItemTypePassedIn() == null) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    SkyBlockItem shippingItem = new SkyBlockItem(e.getCursorItem());

                    if (shippingItem.getGenericInstance() == null) {
                        player.sendMessage("§cThis item is not a valid Minion Shipping item.");
                        e.setCancelled(true);
                        return;
                    }

                    if (shippingItem.getGenericInstance() instanceof MinionShippingItem) {
                        e.setClickedItem(ItemStack.AIR);
                        setItemTypePassedIn(shippingItem.getAttributeHandler().getItemTypeAsType());
                        minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                    } else {
                        player.sendMessage("§cThis item is not a valid Minion Shipping Item.");
                        e.setCancelled(true);
                    }
                }

                @Override
                public void runPost(InventoryClickEvent e, SkyBlockPlayer player) {
                    new GUIMinion(minion).open(player);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aAutomated Shipping", Material.BLUE_STAINED_GLASS_PANE, 1,
                            "§7Add a §aBudget Hopper §7or",
                            "§9Enchanted Hopper §7here to make",
                            "§7your minion automatically sell",
                            "§7generated items after its",
                            "§7inventory is full.");
                }
            };
        } else {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!e.getCursorItem().isAir()) {
                        player.sendMessage("§cYour cursor must be empty to pick this item up!");
                        e.setCancelled(true);
                        return;
                    }

                    if (e.getClickType().equals(ClickType.RIGHT_CLICK)) {
                        e.setCancelled(true);

                        if (heldCoins == 0)
                            return;

                        player.setCoins(player.getCoins() + heldCoins);
                        player.sendMessage("§aYou have received §6" + StringUtility.commaify(heldCoins) + " coins§a from your Minion!");
                        heldCoins = 0;
                        return;
                    }

                    player.addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    itemsSold = 0;
                    e.setClickedItem(ItemStack.AIR);
                    minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                }

                @Override
                public void runPost(InventoryClickEvent e, SkyBlockPlayer player) {
                    new GUIMinion(minion).open(player);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    SkyBlockItem shippingItem = new SkyBlockItem(getItemTypePassedIn());
                    ArrayList<String> lore = new ArrayList<>(shippingItem.getLore());

                    lore.add(" ");
                    lore.add("§7Items Sold: §b" + StringUtility.commaify(itemsSold));
                    lore.add("§7Held Coins: §b" + StringUtility.commaify(heldCoins));
                    lore.add(" ");
                    lore.add("§bRight-click to get held coins.");
                    lore.add("§eClick to remove.");

                    return ItemStackCreator.getStack(shippingItem.getDisplayName(), shippingItem.getMaterial(), 1, lore);
                }
            };
        }
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
