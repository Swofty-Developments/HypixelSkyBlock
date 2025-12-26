package net.swofty.type.galatea.tab;

import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GalateaServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§3§lServer Info"), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry("§b§lArea: §7Galatea", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Server: §8" + HypixelConst.getServerName(), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
