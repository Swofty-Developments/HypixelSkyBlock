package net.swofty.type.hub.npcs.election;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class NPCCandidate5 extends HypixelNPC {
    private static final int INDEX = 4;

    private static SkyBlockMayor getCandidate() {
        ElectionData data = ElectionManager.getElectionData();
        if (!data.isElectionOpen()) return null;
        List<ElectionData.CandidateData> candidates = data.getCandidates();
        if (candidates.size() <= INDEX) return null;
        return candidates.get(INDEX).getMayorEnum().setColorFromIndex(INDEX);
    }

    public NPCCandidate5() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor mayor = getCandidate();
                if (mayor == null) return new String[]{"Mayor ???", "Candidate", "§e§lCLICK"};
                return new String[]{mayor.getColor() + mayor.getDisplayName(), mayor.getColor() + "Candidate", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor mayor = getCandidate();
                return mayor != null ? mayor.getSignature() : "";
            }

            @Override
            public boolean visible(HypixelPlayer player) {
                return getCandidate() != null;
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor mayor = getCandidate();
                return mayor != null ? mayor.getTexture() : "";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(12.5, 50.0625, 34.5, 90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public @NonNull String chatName() {
                SkyBlockMayor mayor = getCandidate();
                return mayor != null ? mayor.getColor() + "Mayor " + mayor.getDisplayName() : "Mayor ???";
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockMayor mayor = getCandidate();
        if (mayor == null) return;
        sendNPCMessage(event.player(), "Running for mayor!");
    }
}
