package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;

import java.util.List;

public interface BestiaryEntry {
    String getName();
    String getDescription();
    GUIMaterial getGuiMaterial();
    List<BestiaryMob> getMobs();
}

