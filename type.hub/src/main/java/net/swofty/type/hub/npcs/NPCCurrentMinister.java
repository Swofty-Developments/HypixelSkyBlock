package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import net.swofty.type.skyblockgeneric.gui.inventories.election.GUIMinisterMenu;

public class NPCCurrentMinister extends HypixelNPC {

    public NPCCurrentMinister() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                if (minister == null) return new String[]{"Minister ????", "§e§lCLICK"};
                return new String[]{"Minister " + minister.getDisplayName(), "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                if (minister == null) return "";
                return minister.getSignature();
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                if (minister == null) return "";
                return minister.getTexture();
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(8.5, 79, 17.5, 135, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockMayor minister = ElectionManager.getCurrentMinister();
        if (minister == null) {
            event.player().sendMessage("§cHello!!");
            return;
        }
        new GUIMinisterMenu().open(event.player());
    }
}
