package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCChristopher extends HypixelNPC {

    public NPCChristopher() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Christopher", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "vUuq+bODcSfPWjYkc3LH2mXTH6jXV8yTuL+tRl6VsJbp5Nw/etyYOHQC6qKb5FE/Mu3RUgYf4o7SRIMYdxc1XWj+edeCc80OV8wNA4rVwcYJFY9x+NggmDB5l8C0Cs+4syiS0jOirpA41GeqhhAT3b/6UiPsiYZz1z4f9Ege6MWu8AydCVFHnL1GNCkV+OjyfAtyAUEaMqW7AkxdlNjW45yk6ORGPKJ9rE66pDg8W63ggN/X5+HwGX29IwCafe5DWdz1XldP/Rh4N+/HhG/AawJTiKS2wsz6wD2qG2mat0FyUWtF7CKP6O1rjvkqgNus1ckI07PvqNCp58gSP03t1hoOuuyUReEmM6gTlwmOoV+t+32ugP1F8ERDYq3Oefvz4s5JfcgNO+xEFZaLyfcb9Ror34BU+5zZpRtBOS7EhJZBGMHBbBsEzRPt2axqW7XKee+bJhAYDP3ozTQAZy7EfCwQQRNQEcVOPzaGDtNyR0S76XQpSoUomJFpBs47u+zm3DDnhPX5egyHy4cbAfwaEUHABD56hYzjY3p/Xv4hEZuUx8V+bCx6YnJkZWqNUAKB1gzisdXk5woriWe5abwQEw5+Yo7+xi/gAKPecbSDT/WzKtfF+VqCKmM6C3nmNv3KEpAmBsze/grHrrU7e+DPaM/Txfy4jgE6zPtYmpPcMRg=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzIzNzQ3Njk0NSwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg3Yjg5NTJiZTJhMjlhNTdkN2YwZTc4OGY3OWZjZmRjMWZiZDI1ZmZlYzAxMmZiZmVmMzRkN2JlNDYzOWI0OSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-59.5, 89, -16.5, -90, 0);
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
