package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCBobbyJoe extends NPCDialogue {

    public NPCBobbyJoe() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Bobby Joe", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "oVeFAAWVWIGXPEQF7zj4D585v76kXYspPTwnLRPNhBgmyVP9ktwc/xQiHhNkfyNRRaspUHVkxmdz2zbY5PQR12z9D9gp42vRRyyvgNaEAPmK85wdvgi6Mmc/3sBuLmpNUI2+yUYWm0+MZYwDd8oaClzl2sGgMKkztGWNo+KoeLIZEPBjHk3vN4pEnJy20hU4kDEbDmQNVqYvPJ91PaEq7RIp3drC0QDENYGtyZMYr6jMig0iCnRfJrBhY/7Lb9SZor4aUke3YOm8co7/jc9HOnGR5ud9tiLz/v12KQWp+uahyDXwRsr9/HpQeeJrm1rBq26vLaOMRRs7yM4FsL7hoVpkyNOjQzhUMl2RQSEQLNHKet9DLVbh4LNSGivGbGSP480Klc6rn4XgA4nrIfLJ2nYP5li+b30uVr4k/19YHyHP3UDxqL3jOIahkFw4jO2ixEEfIGhR/nypSt/93kRpOv7AAjq7XA3joQcsVGRGSnD8SH/sPCGOSpa7QgEzpCS5x6S06c9gPEWMaNYCyFwigab9/LJ6sFqiJkG5FIb4wUaNBjpi8OCS4aQ3Td6RTscLyH53YvbO3plOT5aJK4pGUNGYxfOcbvg1o2hfVqCIWP38irwLmG46vZjspFEGi8I+oZpHDh7n384YvFbnXYl91T1s0kfnwxe9yBphNXpmlOI=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMTU2NzcwOTE4MiwKICAicHJvZmlsZUlkIiA6ICIxNmJhNWU4MDJhMmU0ZDJhYjEwZmZiYWJiYmQ1MDdlZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzbGlua3l1c2VyMzMxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZjA1MTc0YjNmYjVjMDdmN2ViY2U1NDM4Y2MxOTg0MDU2Y2MzZmI0MjgxZjVhZmIwNmMwN2ZlNDljM2MxN2M0IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(62.5, 71, -115.5, 40, 0);
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
        setDialogue(e.player(), "hello");
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "WELCOME! Plenty of room here for all of your needs!",
                                "Have you been to the §2Wilderness §f? I hear there is a creepy bar there.",
                                "Darn it, where did I leave my belongings? Those §dFairies §fmust be up to no good."
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }

}
