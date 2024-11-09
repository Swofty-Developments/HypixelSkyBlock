package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.SkyBlockItemComponent;

import java.util.ArrayList;
import java.util.List;

public class MinionSkinComponent extends SkyBlockItemComponent {
    @Getter
    private final ItemStack helmet;
    @Getter
    private final ItemStack chestplate;
    @Getter
    private final ItemStack leggings;
    @Getter
    private final ItemStack boots;

    public MinionSkinComponent(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        addInheritedComponent(new ExtraRarityComponent("MINION SKIN"));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    public List<String> getSkinLore(String name) {
        return new ArrayList<>(List.of(
                "§7This Minion skin changes your",
                "§7minion's appearance to a",
                "§e" + name + "§7.",
                " ",
                "§7You can place this item in any",
                "§7minion of your choice!"
        ));
    }
}
