package net.swofty.type.replayviewer;

import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;

import java.util.List;

public class ReplayTablistManager extends TablistManager {
	@Override
	public List<TablistModule> getModules() {
		return List.of(new ReplayTablistModule());
	}
}
