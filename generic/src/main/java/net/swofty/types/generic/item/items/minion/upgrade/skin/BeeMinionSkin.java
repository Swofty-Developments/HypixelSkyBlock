package net.swofty.types.generic.item.items.minion.upgrade.skin;

import net.minestom.server.color.Color;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.LeatherArmorMeta;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.MinionSkinItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BeeMinionSkin implements CustomSkyBlockItem, MinionSkinItem, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return getSkinLore("Bee");
    }

    @Override
    public ItemStack getHelmet() {
        return ItemStackCreator.getStackHead("12724a9a4cdd68ba49415560e5be40b4a1c47cb5be1d66aedb52a30e62ef2d47").build();
    }

    @Override
    public ItemStack getChestplate() {
        return ItemStack.builder(Material.LEATHER_CHESTPLATE).meta(bootMeta -> {
            LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(bootMeta.tagHandler());
            leatherMeta.color(new Color(196, 170, 39));
        }).build();
    }

    @Override
    public ItemStack getLeggings() {
        return ItemStack.builder(Material.LEATHER_LEGGINGS).meta(bootMeta -> {
            LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(bootMeta.tagHandler());
            leatherMeta.color(new Color(196, 170, 39));
        }).build();
    }

    @Override
    public ItemStack getBoots() {
        return ItemStack.builder(Material.LEATHER_BOOTS).meta(bootMeta -> {
            LeatherArmorMeta.Builder leatherMeta = new LeatherArmorMeta.Builder(bootMeta.tagHandler());
            leatherMeta.color(new Color(196, 170, 39));
        }).build();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "12724a9a4cdd68ba49415560e5be40b4a1c47cb5be1d66aedb52a30e62ef2d47";
    }
}
