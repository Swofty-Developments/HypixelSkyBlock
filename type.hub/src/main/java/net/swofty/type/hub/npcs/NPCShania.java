package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.entity.animalnpc.NPCAnimalParameters;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;

public class NPCShania extends SkyBlockAnimalNPC {
    public NPCShania() {
        super(new NPCAnimalParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Shania", "§e§lCLICK"};
            }

            @Override
            public int hologramYOffset() {
                return 75;
            }

            @Override
            public EntityType entityType() {
                return EntityType.COW;
            }

            @Override
            public Pos position() {
                return new Pos(48, 72, -159);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickAnimalNPCEvent e) {
        e.player().sendMessage("§e[NPC] Shania§f: Moooooo!");
    }
}