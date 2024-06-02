package net.swofty.types.generic.item.items;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SandboxItem implements CustomSkyBlockItem {

    @Override
    public ItemStatistics getStatistics(@Nullable SkyBlockItem instance) {
        if (instance == null) return ItemStatistics.empty();
        return instance.getAttributeHandler().getSandboxData().getStatistics();
    }

    @Override
    public List<String> getAbsoluteLore(SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = item.getAttributeHandler().getSandboxData().getLore();
        List<String> loreToDisplay = new ArrayList<>();

        if (item.getAttributeHandler().getSandboxData().isShowLoreLinesToggle()) {
            // Add the line number to the start of every line
            for (int i = 0; i < lore.size(); i++) {
                loreToDisplay.add("§c" + (i + 1) + ". §7" + lore.get(i));
            }
        } else {
            loreToDisplay.addAll(lore);
        }

        // Replace all & with §
        loreToDisplay.replaceAll(s -> s.replace("&", "§"));

        return loreToDisplay;
    }

    @Override
    public String getAbsoluteName(SkyBlockPlayer player, SkyBlockItem item) {
        return item.getAttributeHandler().getSandboxData().getDisplayName().replace("&", "§");
    }
}
