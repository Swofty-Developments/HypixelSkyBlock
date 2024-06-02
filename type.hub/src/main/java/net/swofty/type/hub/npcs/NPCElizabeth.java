package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCElizabeth extends NPCDialogue {

    public NPCElizabeth() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§dElizabeth", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "iGozQnHju5S3z9IwM3tSF2P9rSuxljhRmyDhBdxt58GiXVVJwtwjhJLC6yhFu8qzJt2a4hMaL2WbCFtoauB1IB3nI8XJqUhcXb+f6skYRaPaFQxLxydDZq9yEy77JshVl3Jj00W72lxpeC1+wjXfwYRR/VFx0Nuch+Y6uycJJ45atWHSpzkgwYQfRDowrwBUXRYfrQLnprs2JcNVhkVPdgT5u1qwsmqVfcaLgNMfhNN56Sib8n2Ul3QS/uegBkqUb26iHaG+cC88jFafoBWK7Nlz6NvANufTK34buF5u4KI795PG0f7SSdAkV3SyAmv3r4fDbVcsxX5XCAlrRoQfOf/rMESSEKElRtBA6pFoVml3MpdSQDDlk/zMKyWAy3msqhZDymQbjZH4eIxbaomKmv3F7En6e4Aeij0WJnmaOQelf25hTR5Zr9b729QuLlZzzMrdsHsjeC2lCPjmiUh8W5yK+iCGtx8x2aQqKfpnXkobxZxlpqaYtQCfmrvmPC3eks7KqD+0xHYkF/834XYzVgpfIbXmdvxQuNmbybCnEWsQeejors8GRs8kHIczuVYyzBmGy19qyyIVPm7pM6ne+XJTBET3DNSqP60OiVryQLKPpOjtsiEgn8nh/Qti0FLylvKl4eoKx6R5xqFTYCcMIKtGe+yQ6IbM7RSy96Oigf4=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDQyODcyMDkwOSwKICAicHJvZmlsZUlkIiA6ICIzOWEzOTMzZWE4MjU0OGU3ODQwNzQ1YzBjNGY3MjU2ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkZW1pbmVjcmFmdGVybG9sIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzkwMTdiNmFjZjIyZGViMTY4NjI5M2Q3Y2NjZmZlNDA5MDFhYTVhYTRhNmZhOTBmOGUxMjE3MTE0ZTFkMzczMDIiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(0, 71, -101, -90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        if (isInDialogue(e.player())) return;
        SkyBlockLevelRequirement lvl = e.player().getSkyBlockExperience().getLevel();
        if (lvl.asInt() >= 3) {
            new GUIBitsShop().open(e.player());
            return;
        }
        setDialogue(e.player(), "hello");
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§fHello! Welcome to §bSkyBlock§f!",
                                "§fI have powerful items to offer, but only to experienced adventurers!",
                                "§fUntil then, I suggest leveling up to SkyBlock Level 3!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
