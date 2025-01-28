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
import net.swofty.types.generic.item.components.MinionUpgradeComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.extension.MinionExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MinionUpgradeExtension extends MinionExtension {

    public MinionUpgradeExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        super(itemTypeLinker, data);
    }

    @Override
    public @NotNull GUIItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot, SkyBlockAbstractInventory inventory) {
        if (getItemTypePassedIn() == null) {
            return GUIItem.builder(slot)
                    .item(ItemStackCreator.getStack("§aUpgrade Slot", Material.YELLOW_STAINED_GLASS_PANE, 1,
                            "§7You can improve your minion by",
                            "§7adding a minion upgrade item",
                            "§7here.").build())
                    .onClick((ctx, itemClicked) -> {
                        SkyBlockItem upgradeItem = new SkyBlockItem(ctx.cursorItem());

                        ItemType itemTypeLinker = upgradeItem.getAttributeHandler().getPotentialType();
                        if (minion.getExtensionData().hasMinionUpgrade(itemTypeLinker)) {
                            ctx.player().sendMessage("§cThis upgrade is already applied to your minion.");
                            return false;
                        }

                        if (!upgradeItem.hasComponent(MinionUpgradeComponent.class)) {
                            ctx.player().sendMessage("§cThis item is not a valid Minion Upgrade.");
                            return false;
                        }

                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(inventory);
                        setItemTypePassedIn(itemTypeLinker);
                        minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                        ctx.player().openInventory(new GUIMinion(minion));
                        return true;
                    })
                    .build();
        }

        return GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder item = new NonPlayerItemUpdater(new SkyBlockItem(getItemTypePassedIn())).getUpdatedItem();
                    item.set(ItemComponent.CUSTOM_NAME, Component.text("§aUpgrade Slot")
                            .decoration(TextDecoration.ITALIC, false));
                    item = ItemStackCreator.updateLore(item, Stream.of(
                            "§7You can improve your minion by",
                            "§7adding a minion upgrade item",
                            "§7here.",
                            " ",
                            "§7Current Upgrade: " + getItemTypePassedIn().rarity.getColor() + getItemTypePassedIn().getDisplayName(),
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
                    minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                    ctx.player().openInventory(new GUIMinion(minion));
                    return true;
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