package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary;

import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.mob.BestiaryMob;

import java.util.List;

public interface BestiaryEntry {
    String getName();
    String getDescription();
    Material getMaterial();
    String getTexture();
    List<BestiaryMob> getMobs();
}

