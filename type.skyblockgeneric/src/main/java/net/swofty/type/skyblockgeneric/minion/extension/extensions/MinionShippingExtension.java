package net.swofty.type.skyblockgeneric.minion.extension.extensions;

import lombok.NonNull;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIMinion;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionShippingComponent;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.minion.extension.MinionExtension;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
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
    public @NonNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot) {
        if (getItemTypePassedIn() == null) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    SkyBlockItem shippingItem = new SkyBlockItem(p.getInventory().getCursorItem());

                    if (!shippingItem.hasComponent(MinionShippingComponent.class)) {
                        player.sendMessage("§cThis item is not a valid Minion Shipping item.");
                        e.setCancelled(true);
                        return;
                    }

                    p.getInventory().setCursorItem(ItemStack.AIR);
                    e.setCancelled(true);
                    setItemTypePassedIn(shippingItem.getAttributeHandler().getPotentialType());
                    minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                    new GUIMinion(minion).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
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
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (!p.getInventory().getCursorItem().isAir()) {
                        player.sendMessage("§cYour cursor must be empty to pick this item up!");
                        e.setCancelled(true);
                        return;
                    }

                    if (e.getClick() instanceof Click.Right) {
                        e.setCancelled(true);

                        if (heldCoins == 0)
                            return;

                        player.addCoins(heldCoins);
                        player.sendMessage("§aYou have received §6" + StringUtility.commaify(heldCoins) + " coins§a from your Minion!");
                        heldCoins = 0;
                        return;
                    }

                    player.addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    itemsSold = 0;
                    p.getInventory().setCursorItem(ItemStack.AIR);
                    e.setCancelled(true);
                    minion.getExtensionData().setData(slot, MinionShippingExtension.this);
                    new GUIMinion(minion).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
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
        return getItemTypePassedIn() + ":" + itemsSold + ":" + heldCoins;
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
