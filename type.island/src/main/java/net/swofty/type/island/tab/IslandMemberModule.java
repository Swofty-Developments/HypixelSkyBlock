package net.swofty.type.island.tab;

import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IslandMemberModule extends TablistModule {

    @Override
    public List<TablistEntry> getEntries(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        ArrayList<SkyBlockPlayer> toShow;
        if (player.isCoop())
            toShow = new ArrayList<>(player.getSkyBlockIsland().getCoop().getOnlineMembers());
        else
            toShow = new ArrayList<>(Collections.singletonList(player));

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered(I18n.string("tablist.module.island", Map.of("count", String.valueOf(toShow.size())))), TablistSkinRegistry.CYAN)
        ));

        // Sort players by their rank ordinal in reverse
        toShow.sort((o1, o2) -> o2.getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().ordinal()
                - o1.getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().ordinal());
        Collections.reverse(toShow);

        for (int x = 0; x < 19; x++) {
            if (x >= toShow.size()) {
                entries.add(new TablistEntry(" ", TablistSkinRegistry.GRAY));
                continue;
            }

            SkyBlockPlayer tablistPlayer = toShow.get(x);

            entries.add(new TablistEntry(tablistPlayer.getFullDisplayName(), TablistSkinRegistry.GRAY));
        }

        fillRestWithGray(entries);

        return entries;
    }
}
