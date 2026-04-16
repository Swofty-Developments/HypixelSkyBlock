package net.swofty.type.generic.tab;

import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public class EmptyTabModule extends TablistModule {

    public EmptyTabModule() {}

    public List<TablistEntry> getEntries(HypixelPlayer player) {
        return List.of();
    }
}
