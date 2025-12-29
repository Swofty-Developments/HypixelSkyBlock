package net.swofty.type.skyblockgeneric.gui.inventories.rusty;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.gui.inventories.shop.GUIConfirmBuy;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.mission.missions.goldmine.lazyminer.MissionFindLazyMinerPickaxe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GUIRustyMiscellaneous extends GUIRustySubMenu<GUIRustyMiscellaneous.RustyItem> {
    public GUIRustyMiscellaneous() {
        super(
                () -> "Rusty ➜ Miscellaneous",
                () -> List.of(RustyItem.values())
        );
    }


    public enum RustyItem implements ShopEntry {
        IRON_PICKAXE(getRustyPickaxe(), 200, (player) -> player.getMissionData().hasCompleted(MissionFindLazyMinerPickaxe.class)),
        ;

        private final SkyBlockItem item;
        private final int price;
        private final Function<SkyBlockPlayer, Boolean> unlocked;

        RustyItem(
                SkyBlockItem item,
                int price,
                Function<SkyBlockPlayer, Boolean> unlocked
        ) {
            this.item = item;
            this.price = price;
            this.unlocked = unlocked;
        }

        @Override public SkyBlockItem item() { return item; }
        @Override public int price() { return price; }
        @Override public Function<SkyBlockPlayer, Boolean> hasUnlocked() { return unlocked; }
    }

    private static SkyBlockItem getRustyPickaxe() {
        SkyBlockItem pickaxe = new SkyBlockItem(ItemType.IRON_PICKAXE);
        pickaxe.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(
                        EnchantmentType.EFFICIENCY,
                        1
                )
        );
        pickaxe.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(
                        EnchantmentType.SMELTING_TOUCH,
                        1
                )
        );
        return pickaxe;
    }
}