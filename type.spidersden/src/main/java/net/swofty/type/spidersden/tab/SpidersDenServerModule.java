package net.swofty.type.spidersden.tab;

import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SpidersDenServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        Locale l = player.getLocale();
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.server_info", l)), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry(I18n.string("tablist.server_info.area.spiders_den", l), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.server_info.server_label", l, Map.of("server_name", HypixelConst.getServerName())), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
