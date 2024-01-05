package net.swofty.item.set.impl;

import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.swofty.SkyBlock;
import net.swofty.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public interface SetRepeatable {
    Task getTask(Scheduler scheduler);
}
