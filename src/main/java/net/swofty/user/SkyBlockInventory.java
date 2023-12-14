package net.swofty.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.item.SkyBlockItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkyBlockInventory {

    @Getter
    @Setter
    Map<Integer, SkyBlockItem> items = new HashMap<>();
    @Getter
    @Setter
    SkyBlockItem helmet = new SkyBlockItem(Material.AIR);
    @Getter
    @Setter
    SkyBlockItem chestplate = new SkyBlockItem(Material.AIR);
    @Getter
    @Setter
    SkyBlockItem leggings = new SkyBlockItem(Material.AIR);
    @Getter
    @Setter
    SkyBlockItem boots = new SkyBlockItem(Material.AIR);

    public SkyBlockInventory() {}
}
