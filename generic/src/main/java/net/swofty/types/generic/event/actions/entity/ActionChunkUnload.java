package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.event.Event;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionChunkUnload extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return InstanceChunkUnloadEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        InstanceChunkUnloadEvent event = (InstanceChunkUnloadEvent) tempEvent;
        Instance instance = event.getInstance();
        int chunkX = event.getChunkX();
        int chunkZ = event.getChunkZ();

        if (SkyBlockConst.isIslandServer()) return;

        instance.loadChunk(chunkX, chunkZ).join();
    }
}
