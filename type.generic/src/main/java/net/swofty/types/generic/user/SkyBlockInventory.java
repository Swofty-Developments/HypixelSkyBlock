package net.swofty.types.generic.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItem;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SkyBlockInventory {

    @Setter
    private Map<Integer, UnderstandableSkyBlockItem> items = new HashMap<>();
    @Setter
    private UnderstandableSkyBlockItem helmet = new SkyBlockItem(Material.AIR).toUnderstandable();
    @Setter
    private UnderstandableSkyBlockItem chestplate = new SkyBlockItem(Material.AIR).toUnderstandable();
    @Setter
    private UnderstandableSkyBlockItem leggings = new SkyBlockItem(Material.AIR).toUnderstandable();
    @Setter
    private UnderstandableSkyBlockItem boots = new SkyBlockItem(Material.AIR).toUnderstandable();

    public SkyBlockInventory() {
    }
}
