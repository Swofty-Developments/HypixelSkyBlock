package net.swofty.type.goldmine.tab;

import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldMineServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.server_info")), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry(I18n.string("tablist.server_info.area.gold_mine"), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.server_info.server_label", Map.of("server_name", HypixelConst.getServerName())), TablistSkinRegistry.GRAY));

        fillRestWithGray(entries);

        return entries;
    }
}
