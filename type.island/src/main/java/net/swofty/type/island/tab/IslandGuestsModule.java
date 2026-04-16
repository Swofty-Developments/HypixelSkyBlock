package net.swofty.type.island.tab;

import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IslandGuestsModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        Locale l = player.getLocale();
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.guests", l)), TablistSkinRegistry.PURPLE)
        ));

        fillRestWithGray(entries);

        return entries;
    }
}
