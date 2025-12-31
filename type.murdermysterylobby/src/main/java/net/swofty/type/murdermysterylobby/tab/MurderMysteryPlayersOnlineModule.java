package net.swofty.type.murdermysterylobby.tab;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MurderMysteryPlayersOnlineModule extends TablistModule {
    public int page;

    public MurderMysteryPlayersOnlineModule(int page) {
        this.page = page;
    }

    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        List<HypixelPlayer> players = HypixelGenericLoader.getLoadedPlayers();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§c§lPlayers §f(" + players.size() + ")"), TablistSkinRegistry.ORANGE)
        ));

        List<HypixelPlayer> toShow = new ArrayList<>();

        // Sort players by their rank ordinal in reverse
        players.sort((o1, o2) -> o2.getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().ordinal()
                - o1.getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue().ordinal());
        Collections.reverse(players);

        // In chunks of 19, load the players into the toShow list
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

            HypixelPlayer tablistPlayer = toShow.get(x);
            String displayName = tablistPlayer.getRank().getPrefix() + StringUtility.getTextFromComponent(tablistPlayer.getName());

            entries.add(new TablistEntry(displayName, TablistSkinRegistry.GRAY));
        }

        return entries;
    }
}
