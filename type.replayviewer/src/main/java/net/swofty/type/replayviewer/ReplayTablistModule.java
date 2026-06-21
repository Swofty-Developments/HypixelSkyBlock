package net.swofty.type.replayviewer;

import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

class ReplayTablistModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        List<TablistEntry> entries = new ArrayList<>();

        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> {
                // TODO: display all player names with only their color
            },
            () -> entries.add(new TablistEntry("§7Loading...", TablistSkinRegistry.ORANGE))
        );

        return entries;
    }
}