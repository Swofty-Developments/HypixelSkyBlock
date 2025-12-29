package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMarco extends HypixelNPC {

    public NPCMarco() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Marco", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "iD4YTAup8ydps4Hvc1aynvCWIdXyHVms+Zv24BWUa0F7DecE/2ACQNZz7wdzYSflOrITWRiyUOx5Ezc6wIvbVmpcs30NC3dNqiL0FBnSDwvh8X9Zhx1OL+lnWchdVFvehJC/TXiwk75zZx2WJRP5GxmVFAEdqPHHw9CqWESejVrdXptNafkjr6fajZpa7LS54I5YUA81vvLyOwsAm35u8UI2wCv3A1riQX7WaE5pYL0PHW3B7B8woiWJ4vnzLpjA90aIzOX+Rks/bdH1B83RdW2L4RY3K1rgnPOeq/jVmxvCHqVy9+EUNKHKKqPBY9dB27MeewHsm3+/BXRJ1FutP7Fhxdiz/cVqZ5ExatHhLBIQWeutHSCykbNVvUshzddTxV0Wn+RYAUBfwS3OWmL9bU6EEItYVWww65lH3Au792M5u9X8/ByEDwXADZUpZYy7MgF/sh2pXDpUv6sBY+B8t1QY5mPX+c7Mbxdt6S5wFQtbziXp8I5wHrRqJGkfsq/mKkP+nYfBysTO6XFE3JFc4sf2IzYbpaIAPHhFTOxxEEmISJZDJ6qaLWQgotWeKO/v3Mt7WqTqvYV9FVMAY1AR/XTRZmb32HTBHR40R0j5qbN9sxDz8VASjRjzjmgujE1uskHceto1Resyb096CKHO98uyaOg+651xBF3AjdgAnPo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU4ODYyMzkwOTIyNCwKICAicHJvZmlsZUlkIiA6ICIwNGI3MDhhMzM1NjY0ZjJmODVlYzVlZWYyN2QxNGRhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaW9sZXRza3l6eiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82M2I0MGRhZGQ2YTAyNjBkMGU3NmE1NDIwMTVlMGFiNWRjZDkzNjc5NjM0M2QzOWM0NDE0ZDI4MDg3NzEzNTliIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-9.5, 71, -14, -170, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }

}
