package net.swofty.type.hub.npcs.election;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import net.swofty.type.skyblockgeneric.gui.inventories.election.MayorMenuView;

public class NPCCurrentMayorDuplicate extends HypixelNPC {

    public NPCCurrentMayorDuplicate() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                if (mayor == null) return new String[]{"Mayor ???", "§e§lCLICK"};
                return new String[]{"Mayor " + mayor.getDisplayName(), "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                if (mayor == null) return "";
                return mayor.getSignature();
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                if (mayor == null) return "";
                return mayor.getTexture();
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-3.5, 49, 34.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) {
            event.player().sendMessage("§cHello!!");
            return;
        }
        event.player().openView(new MayorMenuView());
    }
}
