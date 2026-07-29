package net.swofty.type.hub.npcs.election;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import net.swofty.type.skyblockgeneric.gui.inventories.election.MinisterMenuView;

public class NPCCurrentMinister extends HypixelNPC {

    public NPCCurrentMinister() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                if (minister == null) return new String[]{
                    I18n.string("npcs_hub.election.minister_unknown", player.getLocale()),
                    I18n.string("npcs_hub.election.click", player.getLocale())
                };
                return new String[]{
                    "Minister " + minister.getDisplayName(),
                    I18n.string("npcs_hub.election.click", player.getLocale())
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                return minister != null ? minister.getSignature() : "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor minister = ElectionManager.getCurrentMinister();
                return minister != null ? minister.getTexture() : "";
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
            event.player().sendMessage(I18n.string("npcs_hub.election.hello", event.player().getLocale()));
            return;
        }
        event.player().openView(new MinisterMenuView());
    }
}
