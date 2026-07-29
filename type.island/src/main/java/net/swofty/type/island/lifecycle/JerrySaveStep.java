package net.swofty.type.island.lifecycle;

import net.swofty.type.generic.entity.hologram.ServerHolograms;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleContext;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import net.swofty.type.skyblockgeneric.user.island.IslandLifecycleStep;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;

public class JerrySaveStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.SAVE;
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        JerryInformation jerryInformation = context.island().getJerryInformation();

        if (jerryInformation == null) return;

        if (jerryInformation.getJerry() != null) {
            jerryInformation.getJerry().remove();
        }
        if (jerryInformation.getHologram() != null) {
            ServerHolograms.removeExternalHologram(jerryInformation.getHologram());
        }

        if (jerryInformation.getJerryPosition() == null) return;

        context.island().getDatabase().insertOrUpdate("jerry_position_x", jerryInformation.getJerryPosition().x());
        context.island().getDatabase().insertOrUpdate("jerry_position_y", jerryInformation.getJerryPosition().y());
        context.island().getDatabase().insertOrUpdate("jerry_position_z", jerryInformation.getJerryPosition().z());
        context.island().getDatabase().insertOrUpdate("jerry_position_yaw", (double) jerryInformation.getJerryPosition().yaw());
        context.island().getDatabase().insertOrUpdate("jerry_position_pitch", (double) jerryInformation.getJerryPosition().pitch());
    }
}
