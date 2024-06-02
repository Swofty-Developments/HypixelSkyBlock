package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionChunkUnload implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.ENTITY , requireDataLoaded = false , isAsync = true)
    public void run(InstanceChunkUnloadEvent event) {
        Instance instance = event.getInstance();
        int chunkX = event.getChunkX();
        int chunkZ = event.getChunkZ();

        if (SkyBlockConst.isIslandServer()) return;

        instance.loadChunk(chunkX, chunkZ).join();
    }
}
