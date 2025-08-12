package net.swofty.type.thefarmingislands.tab;

import net.swofty.type.skyblockgeneric.SkyBlockConst;
import net.swofty.type.skyblockgeneric.tab.TablistModule;
import net.swofty.type.skyblockgeneric.tab.TablistSkinRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class TheFarmingIslandsServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§3§lServer Info"), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry("§b§lArea: §7The Farming Islands", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Server: §8" + SkyBlockConst.getServerName(), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
