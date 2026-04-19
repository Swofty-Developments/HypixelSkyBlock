package net.swofty.type.backwaterbayou.tab;

import net.kyori.adventure.text.Component;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BackwaterBayouServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        Locale l = player.getLocale();
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.server_info", l)), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry(I18n.string("tablist.server_info.area.backwater_bayou", l), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.server_info.server_label", l, Component.text(HypixelConst.getServerName())), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
