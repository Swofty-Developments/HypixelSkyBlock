package net.swofty.types.generic.item.impl;

import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface MinionSkinItem extends ExtraRarityDisplay, TrackedUniqueItem {
    @Override
    default String getExtraRarityDisplay() {
        return " COSMETIC";
    }

    default ArrayList<String> getSkinLore(String name) {
        return new ArrayList<>(List.of(
                "§7This Minion skin changes your",
                "§7minion's appearance to a",
                "§e" + name + "§7.",
                " ",
                "§7You can place this item in any",
                "§7minion of your choice!"
        ));
    }

    ItemStack getHelmet();
    ItemStack getChestplate();
    ItemStack getLeggings();
    ItemStack getBoots();
}
