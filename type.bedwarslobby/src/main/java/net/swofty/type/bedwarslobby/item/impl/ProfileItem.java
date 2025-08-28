package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Arrays;

public class ProfileItem extends BedWarsItem {

    public ProfileItem() {
        super("profile");
    }

    @Override
    public ItemStack getBlandItem() {
        return null;
    }

    @Override
    public ItemStack getItemStack(HypixelPlayer... p) {
        HypixelPlayer player = Arrays.stream(p).findFirst().orElseThrow();
        return ItemStackCreator.createNamedItemStack(Material.PLAYER_HEAD, "§aProfile")
            .set(DataComponents.PROFILE, new HeadProfile(player.getPlayerSkin())).build();
    }

}