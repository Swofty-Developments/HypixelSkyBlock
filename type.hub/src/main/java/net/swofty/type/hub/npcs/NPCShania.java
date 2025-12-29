package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCShania extends HypixelNPC {
    public NPCShania() {
        super(new AnimalConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Shania", "§e§lCLICK"};
            }

            @Override
            public float hologramYOffset() {
                return -0.4f;
            }

            @Override
            public EntityType entityType() {
                return EntityType.COW;
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(48, 72, -159);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendMessage("Moooooo!");
    }
}