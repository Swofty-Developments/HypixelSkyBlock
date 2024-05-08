package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.type.hub.gui.GUIShopAlchemist;
import net.swofty.types.generic.entity.animalnpc.NPCAnimalParameters;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;

public class NPCAlchemist extends SkyBlockAnimalNPC {
    public NPCAlchemist() {
        super(new NPCAnimalParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Alchemist", "§e§lCLICK"};
            }

            @Override
            public int hologramYOffset() {
                return 75;
            }

            @Override
            public EntityType entityType() {
                return EntityType.WITCH;
            }

            @Override
            public Pos position() {
                return new Pos(41.5, 70, -63.5);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickAnimalNPCEvent e) {
        new GUIShopAlchemist().open(e.player());
    }
}
