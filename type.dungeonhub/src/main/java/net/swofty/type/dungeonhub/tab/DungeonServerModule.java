package net.swofty.type.dungeonhub.tab;

import net.swofty.type.generic.SkyBlockConst;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class DungeonServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§3§lServer Info"), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry("§b§lArea: §7Dungeon Hub", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Server: §8" + SkyBlockConst.getServerName(), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
