package net.swofty.types.generic.tab.modules;

import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.TablistSkinRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayersOnlineModule extends TablistModule {
    public int page;

    public PlayersOnlineModule(int page) {
        this.page = page;
    }


    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        List<SkyBlockPlayer> players = SkyBlockGenericLoader.getLoadedPlayers();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§a§lPlayers §f(" + players.size() + ")"), TablistSkinRegistry.GREEN)
        ));

        List<SkyBlockPlayer> toShow = new ArrayList<>();

        // Sort players by their rank ordinal in reverse
        players.sort((o1, o2) -> o2.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().ordinal()
                - o1.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().ordinal());
        Collections.reverse(players);

        // In chunks of 19, load the players into the toShow list.
        // If the page is 1, then use the first 19, if the page is 2, then use the second set of 19, etc.

        for (int i = 0; i < players.size(); i++) {
            if (i >= (page - 1) * 19 && i < page * 19) {
                toShow.add(players.get(i));
            }
        }

        for (int x = 0; x < 19; x++) {
            if (x >= toShow.size()) {
                entries.add(new TablistEntry(" ", TablistSkinRegistry.GRAY));
                continue;
            }

            SkyBlockPlayer tablistPlayer = toShow.get(x);

            entries.add(new TablistEntry(tablistPlayer.getFullDisplayName(), TablistSkinRegistry.GRAY));
        }

        return entries;
    }
}
