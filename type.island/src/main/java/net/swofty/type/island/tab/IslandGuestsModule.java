package net.swofty.type.island.tab;

import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.TablistSkinRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class IslandGuestsModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§d§lGuests"), TablistSkinRegistry.PURPLE)
        ));

        fillRestWithGray(entries);

        return entries;
    }
}
