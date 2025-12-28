package net.swofty.type.skyblockgeneric.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

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
