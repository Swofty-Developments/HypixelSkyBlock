package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCClerkSeraphine extends HypixelNPC {

    public NPCClerkSeraphine() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Clerk Seraphine", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "U1ui0b5hI9jPnWkxdWSFKd9wpWT7AKvhzvuWy+ba/DpXjv444LqPPa3Y7DY6b4SPA9O/L1oPKUMDSrBe70pCwhWsBASdTPdJKfkCH1+TG24LlfoPMCwD/EYER2zR1A8hsnKAy+NNUfdoTkHScs4QEKekhX23/w7L3lm2cReWaJniP7HGutmAMONZJwfagheFvnzTnaQtrIWuQyNrXc8AQV8kSCDz1OC7nKxvY9o/NS/JSz5jP/ErIDWd3LKiEso16dOSbW0dtjW+4VpUaf/x6Hy+5ODeYBRV5WP3Ytl+g1CgYE9VyxalFoqAJIRltAd5EB4QAPrKDX/KoBYCBwRD6njC4U23IiBJanaf7CojPv2gfI4SNNwEPbIeU0efZ73S6FjteMWvTUis+al724mJMeqLzB6ktHdQD4ptLbbPzHMNeM7ZsNGt3u/wtkLpp0CIBtpps+GF7cQkJX1dFz9duSz7I+abo0zDq9lkJvBmDQWolAnhpto4spwAnHufnT9dR/HcD2pfKXhqexpX7PPeufLzTdEN/OcRMBv+Ix1bh1uYjD4DkiE2KiXRgB8D+wn8CHXxlvjSGofvvSpqgEZL2kJsB3mwB2mgZ65l5e98+CoLanvrXMqkQZs5pFVpBRlGAwKzb0AqRM5wwCXGln50o+ESXvcrrdXUMWScU5atIjY=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MTg3MTUzMjU0NiwKICAicHJvZmlsZUlkIiA6ICI2ZmQyNGJlNDk4ZjA0MDJlOTZhYWQ2MWUzY2VmYjZmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmdlbGFsbHhfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUyZTE1YzJkN2NmZGQzMDA4ZGQ3OTBiMmY4OTE3NWM3M2Q1ZTc3NjI3YWY2NTNmYzE4ZTlmY2M4Yjk2YWRmODQiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(4.5, 72, -104.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Welcome to the §bCommunity Center§f!",
                                "Contribute to community projects, upgrade your account, and more by talking to §dElizabeth§f!",
                                "You can also vote in the §bmayor elections §fby heading through the warp upstairs!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
