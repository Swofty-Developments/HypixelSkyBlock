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

        entries.add(new TablistEntry("§b§lREPLAY", TablistSkinRegistry.ORANGE));

        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> {
                entries.add(new TablistEntry("§7Time: §e" + session.getFormattedTime() + "/" + session.getFormattedTotalTime(), TablistSkinRegistry.ORANGE));
                entries.add(new TablistEntry("§7Speed: §e" + session.getPlaybackSpeed() + "x", TablistSkinRegistry.ORANGE));
                entries.add(new TablistEntry(session.isPlaying() ? "§aPlaying" : "§ePaused", TablistSkinRegistry.ORANGE));
            },
            () -> entries.add(new TablistEntry("§7Loading...", TablistSkinRegistry.ORANGE))
        );

        return TablistModule.fillRestWithGray(entries);
    }
}