package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCClerkSeraphine extends SkyBlockNPC {

    public NPCClerkSeraphine() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Clerk Seraphine", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "U1ui0b5hI9jPnWkxdWSFKd9wpWT7AKvhzvuWy+ba/DpXjv444LqPPa3Y7DY6b4SPA9O/L1oPKUMDSrBe70pCwhWsBASdTPdJKfkCH1+TG24LlfoPMCwD/EYER2zR1A8hsnKAy+NNUfdoTkHScs4QEKekhX23/w7L3lm2cReWaJniP7HGutmAMONZJwfagheFvnzTnaQtrIWuQyNrXc8AQV8kSCDz1OC7nKxvY9o/NS/JSz5jP/ErIDWd3LKiEso16dOSbW0dtjW+4VpUaf/x6Hy+5ODeYBRV5WP3Ytl+g1CgYE9VyxalFoqAJIRltAd5EB4QAPrKDX/KoBYCBwRD6njC4U23IiBJanaf7CojPv2gfI4SNNwEPbIeU0efZ73S6FjteMWvTUis+al724mJMeqLzB6ktHdQD4ptLbbPzHMNeM7ZsNGt3u/wtkLpp0CIBtpps+GF7cQkJX1dFz9duSz7I+abo0zDq9lkJvBmDQWolAnhpto4spwAnHufnT9dR/HcD2pfKXhqexpX7PPeufLzTdEN/OcRMBv+Ix1bh1uYjD4DkiE2KiXRgB8D+wn8CHXxlvjSGofvvSpqgEZL2kJsB3mwB2mgZ65l5e98+CoLanvrXMqkQZs5pFVpBRlGAwKzb0AqRM5wwCXGln50o+ESXvcrrdXUMWScU5atIjY=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MTg3MTUzMjU0NiwKICAicHJvZmlsZUlkIiA6ICI2ZmQyNGJlNDk4ZjA0MDJlOTZhYWQ2MWUzY2VmYjZmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmdlbGFsbHhfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUyZTE1YzJkN2NmZGQzMDA4ZGQ3OTBiMmY4OTE3NWM3M2Q1ZTc3NjI3YWY2NTNmYzE4ZTlmY2M4Yjk2YWRmODQiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position() {
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
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it done quickly!");
    }

}
