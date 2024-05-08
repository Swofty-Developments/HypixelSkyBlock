package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCClerkSeraphine extends NPCDialogue {

    public NPCClerkSeraphine() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Clerk Seraphine", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "U1ui0b5hI9jPnWkxdWSFKd9wpWT7AKvhzvuWy+ba/DpXjv444LqPPa3Y7DY6b4SPA9O/L1oPKUMDSrBe70pCwhWsBASdTPdJKfkCH1+TG24LlfoPMCwD/EYER2zR1A8hsnKAy+NNUfdoTkHScs4QEKekhX23/w7L3lm2cReWaJniP7HGutmAMONZJwfagheFvnzTnaQtrIWuQyNrXc8AQV8kSCDz1OC7nKxvY9o/NS/JSz5jP/ErIDWd3LKiEso16dOSbW0dtjW+4VpUaf/x6Hy+5ODeYBRV5WP3Ytl+g1CgYE9VyxalFoqAJIRltAd5EB4QAPrKDX/KoBYCBwRD6njC4U23IiBJanaf7CojPv2gfI4SNNwEPbIeU0efZ73S6FjteMWvTUis+al724mJMeqLzB6ktHdQD4ptLbbPzHMNeM7ZsNGt3u/wtkLpp0CIBtpps+GF7cQkJX1dFz9duSz7I+abo0zDq9lkJvBmDQWolAnhpto4spwAnHufnT9dR/HcD2pfKXhqexpX7PPeufLzTdEN/OcRMBv+Ix1bh1uYjD4DkiE2KiXRgB8D+wn8CHXxlvjSGofvvSpqgEZL2kJsB3mwB2mgZ65l5e98+CoLanvrXMqkQZs5pFVpBRlGAwKzb0AqRM5wwCXGln50o+ESXvcrrdXUMWScU5atIjY=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MTg3MTUzMjU0NiwKICAicHJvZmlsZUlkIiA6ICI2ZmQyNGJlNDk4ZjA0MDJlOTZhYWQ2MWUzY2VmYjZmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmdlbGFsbHhfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUyZTE1YzJkN2NmZGQzMDA4ZGQ3OTBiMmY4OTE3NWM3M2Q1ZTc3NjI3YWY2NTNmYzE4ZTlmY2M4Yjk2YWRmODQiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(11.5, 71, -101, 90, 0);
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
                                "Welcome to the §bCommunity Center§f!",
                                "Contribute to community projects, upgrade your account, and more by talking to §dElizabeth§f!",
                                "You can also vote in the §bmayor elections §fby heading through the warp upstairs!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
