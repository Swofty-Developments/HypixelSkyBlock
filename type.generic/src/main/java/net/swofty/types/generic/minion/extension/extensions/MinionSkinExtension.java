package net.swofty.types.generic.minion.extension.extensions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetCursorItemAction;
import net.swofty.types.generic.gui.inventory.inventories.GUIMinion;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.MinionSkinComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.extension.MinionExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MinionSkinExtension extends MinionExtension {

    public MinionSkinExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        super(itemTypeLinker, data);
    }

    @Override
    public @NotNull GUIItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot, SkyBlockAbstractInventory inventory) {
        if (getItemTypePassedIn() == null) {
            return GUIItem.builder(slot)
                    .item(ItemStackCreator.getStack("§aMinion Skin Slot", Material.LIME_STAINED_GLASS_PANE, 1,
                            "§7You can insert a Minion Skin",
                            "§7here to change the appearance of",
                            "§7your minion.").build())
                    .onClick((ctx, itemClicked) -> {
                        SkyBlockItem skinItem = new SkyBlockItem(ctx.cursorItem());

                        if (!skinItem.hasComponent(MinionSkinComponent.class)) {
                            ctx.player().sendMessage("§cThis item is not a valid Minion Skin.");
                            return false;
                        }

                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(inventory);
                        setItemTypePassedIn(skinItem.getAttributeHandler().getPotentialType());
                        minion.getExtensionData().setData(slot, MinionSkinExtension.this);
                        minion.getMinionEntity().updateMinionDisplay(minion);
                        ctx.player().openInventory(new GUIMinion(minion));
                        return true;
                    })
                    .onPostClick((player) -> {
                        player.openInventory(new GUIMinion(minion));
                    })
                    .build();
        }

        return GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder item = new NonPlayerItemUpdater(new SkyBlockItem(getItemTypePassedIn())).getUpdatedItem();
                    item.set(ItemComponent.CUSTOM_NAME, Component.text("§aMinion Skin Slot")
                            .decoration(TextDecoration.ITALIC, false));
                    item = ItemStackCreator.updateLore(item, Stream.of(
                            "§7You can insert a Minion Skin",
                            "§7here to change the appearance of",
                            "§7your minion.",
                            " ",
                            "§7Current Skin: " + getItemTypePassedIn().rarity.getColor() + getItemTypePassedIn().getDisplayName(),
                            " ",
                            "§eClick to remove."
                    ).toList());

                    return item.build();
                })
                .onClick((ctx, itemClicked) -> {
                    if (!ctx.cursorItem().isAir()) {
                        ctx.player().sendMessage("§cYour cursor must be empty to pick this item up!");
                        return false;
                    }

                    ctx.player().addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    minion.getExtensionData().setData(slot, MinionSkinExtension.this);
                    minion.getMinionEntity().updateMinionDisplay(minion);
                    ctx.player().openInventory(new GUIMinion(minion));
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