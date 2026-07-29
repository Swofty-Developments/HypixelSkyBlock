package net.swofty.type.island.lifecycle;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleContext;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleStep;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;

public class JerryDefaultsStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.CREATE;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        context.island().setJerryInformation(new JerryInformation(null, new Pos(9.5, 100, 33.5, 145, 0), null));
    }
}
