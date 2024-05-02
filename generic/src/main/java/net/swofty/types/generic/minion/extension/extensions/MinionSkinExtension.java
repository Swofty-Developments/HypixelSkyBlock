package net.swofty.types.generic.minion.extension.extensions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.GUIMinion;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MinionSkinItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MinionSkinExtension extends MinionExtension {

    public MinionSkinExtension(@Nullable ItemType itemType, @Nullable Object data) {
        super(itemType, data);
    }

    @Override
    public @NotNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot) {
        if (getItemTypePassedIn() == null) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    SkyBlockItem skinItem = new SkyBlockItem(e.getCursorItem());

                    if (skinItem.getGenericInstance() == null) {
                        player.sendMessage("§cThis item is not a valid Minion Skin.");
                        e.setCancelled(true);
                        return;
                    }

                    if (skinItem.getGenericInstance() instanceof MinionSkinItem) {
                        e.setClickedItem(ItemStack.AIR);
                        setItemTypePassedIn(skinItem.getAttributeHandler().getItemTypeAsType());
                        minion.getExtensionData().setData(slot, MinionSkinExtension.this);
                        minion.getMinionEntity().updateMinionDisplay(minion);
                    } else {
                        player.sendMessage("§cThis item is not a valid Minion Skin.");
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
                    return ItemStackCreator.getStack("§aMinion Skin Slot", Material.LIME_STAINED_GLASS_PANE, 1,
                            "§7You can insert a Minion Skin",
                            "§7here to change the appearance of",
                            "§7your minion.");
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

                    player.addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    e.setClickedItem(ItemStack.AIR);
                    minion.getExtensionData().setData(slot, MinionSkinExtension.this);
                    minion.getMinionEntity().updateMinionDisplay(minion);
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
                    ItemStack.Builder item = new NonPlayerItemUpdater(new SkyBlockItem(getItemTypePassedIn())).getUpdatedItem();
                    item = item.displayName(Component.text("§aMinion Skin Slot").decoration(TextDecoration.ITALIC, false))
                            .lore(Stream.of(
                                    "§7You can insert a Minion Skin",
                                    "§7here to change the appearance of",
                                    "§7your minion.",
                                    " ",
                                    "§7Current Skin: " + getItemTypePassedIn().rarity.getColor() + getItemTypePassedIn().getDisplayName(null),
                                    " ",
                                    "§eClick to remove."
                    ).map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false)).toList());

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
