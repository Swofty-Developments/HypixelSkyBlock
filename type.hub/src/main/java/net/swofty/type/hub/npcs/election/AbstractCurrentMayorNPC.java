package net.swofty.type.hub.npcs.election;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import net.swofty.type.skyblockgeneric.gui.inventories.election.MayorMenuView;

public abstract class AbstractCurrentMayorNPC extends HypixelNPC {

    protected AbstractCurrentMayorNPC(Pos mayorPosition) {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                if (mayor == null) return new String[]{
                    I18n.string("npcs_hub.election.mayor_unknown", player.getLocale()),
                    I18n.string("npcs_hub.election.click", player.getLocale())
                };
                return new String[]{
                    "Mayor " + mayor.getDisplayName(),
                    I18n.string("npcs_hub.election.click", player.getLocale())
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                return mayor != null ? mayor.getSignature() : "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                return mayor != null ? mayor.getTexture() : "";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return mayorPosition;
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
            event.player().sendMessage(I18n.string("npcs_hub.election.hello", event.player().getLocale()));
            return;
        }
        event.player().openView(new MayorMenuView());
    }
}
