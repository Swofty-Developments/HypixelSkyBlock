package net.swofty.type.island.tab;

import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.TablistSkinRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IslandMemberModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        ArrayList<SkyBlockPlayer> toShow;
        if (player.isCoop())
            toShow = new ArrayList<>(player.getSkyBlockIsland().getCoop().getOnlineMembers());
        else
            toShow = new ArrayList<>(Collections.singletonList(player));

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§b§lIsland §f(" + toShow.size() + ")"), TablistSkinRegistry.CYAN)
        ));

        // Sort players by their rank ordinal in reverse
        toShow.sort((o1, o2) -> o2.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().ordinal()
                - o1.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().ordinal());
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
