package net.swofty.type.skyblockgeneric.minion.extension.extensions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIMinion;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionUpgradeComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.minion.extension.MinionExtension;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MinionUpgradeExtension extends MinionExtension {

    public MinionUpgradeExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        super(itemTypeLinker, data);
    }

    @Override
    public @NotNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot) {
        if (getItemTypePassedIn() == null) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    SkyBlockItem upgradeItem = new SkyBlockItem(p.getInventory().getCursorItem());
                    e.setCancelled(true);

                    ItemType itemTypeLinker = upgradeItem.getAttributeHandler().getPotentialType();
                    if (minion.getExtensionData().hasMinionUpgrade(itemTypeLinker)) {
                        player.sendMessage("§cThis upgrade is already applied to your minion.");
                        e.setCancelled(true);
                        return;
                    }

                    if (upgradeItem.hasComponent(MinionUpgradeComponent.class)) {
                        p.getInventory().setCursorItem(ItemStack.AIR);
                        setItemTypePassedIn(itemTypeLinker);
                        minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                        e.setCancelled(true);
                    } else {
                        player.sendMessage("§cThis item is not a valid Minion Upgrade.");
                        e.setCancelled(true);
                    }
                    new GUIMinion(minion).open(player);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§aUpgrade Slot", Material.YELLOW_STAINED_GLASS_PANE, 1,
                            "§7You can improve your minion by",
                            "§7adding a minion upgrade item",
                            "§7here.");
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

                    player.addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    p.getInventory().setCursorItem(ItemStack.AIR);
                    e.setCancelled(true);
                    minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                    new GUIMinion(minion).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    ItemStack.Builder item = new NonPlayerItemUpdater(new SkyBlockItem(getItemTypePassedIn())).getUpdatedItem();
                   item.set(DataComponents.CUSTOM_NAME, Component.text("§aUpgrade Slot").decoration(TextDecoration.ITALIC, false));
                    item = ItemStackCreator.updateLore(item, Stream.of(
                            "§7You can improve your minion by",
                            "§7adding a minion upgrade item",
                            "§7here.",
                            " ",
                            "§7Current Upgrade: " + getItemTypePassedIn().rarity.getColor() + getItemTypePassedIn().getDisplayName(),
                            " ",
                            "§eClick to remove."
                    ).toList());

                    return item;
                }
            };
        }
    }

    @Override
    public String toString() {
        if (getItemTypePassedIn() == null)
            return "null";
        return getItemTypePassedIn().name();
    }

    @Override
    public void fromString(String string) {
        if (string.equals("null")) {
            setItemTypePassedIn(null);
            return;
        }
        setItemTypePassedIn(ItemType.valueOf(string));
    }
}
