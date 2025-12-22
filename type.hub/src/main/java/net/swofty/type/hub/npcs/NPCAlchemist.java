package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUIShopAlchemist;

public class NPCAlchemist extends HypixelNPC {
    public NPCAlchemist() {
        super(new AnimalConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Alchemist", "§e§lCLICK"};
            }

            @Override
            public float hologramYOffset() {
                return 0.1f;
            }

            @Override
            public EntityType entityType() {
                return EntityType.WITCH;
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(41.5, 70, -63.5);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        new GUIShopAlchemist().open(e.player());
    }
}
