package net.swofty.type.island.tab;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMinionData;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.minion.MinionHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IslandServerModule extends TablistModule {

    @Override
    public List<TablistModule.TablistEntry> getEntries(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        Locale l = player.getLocale();
        List<IslandMinionData.IslandMinion> minions = player.getSkyBlockIsland().getMinionData().getMinions();
        DatapointMinionData.ProfileMinionData data = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.MINION_DATA,
                DatapointMinionData.class
        ).getValue();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.server_info", l)), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry(I18n.string("tablist.server_info.area.private_island", l), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.server_info.server_label", l, Map.of("server_name", HypixelConst.getServerName())), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(I18n.string("tablist.island.minions_label", l, Map.of("current", String.valueOf(minions.size()), "max", String.valueOf(data.getSlots()))), TablistSkinRegistry.GRAY));

        entries.add(getGrayEntry());
        entries.add(new TablistEntry(I18n.string("tablist.module.minions", l, Map.of("count", String.valueOf(minions.size()))), TablistSkinRegistry.GRAY));

        minions.forEach(minion -> {
            String content = " " + minion.getMinion().getDisplay().replace(" Minion", "");

            content = content + " " + StringUtility.getAsRomanNumeral(minion.getTier());

            MinionHandler.InternalMinionTags.State minionState = minion.getInternalMinionTags().getState();

            switch (minionState) {
                case BAD_FULL -> content = content + " " + I18n.string("tablist.island.minion_state.full", l);
                case BAD_LOCATION -> content = content + " " + I18n.string("tablist.island.minion_state.blocked", l);
                default -> content = content + " " + I18n.string("tablist.island.minion_state.active", l);
            }

            entries.add(new TablistEntry(content, TablistSkinRegistry.GRAY));
        });

        fillRestWithGray(entries);

        return entries;
    }
}
