package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCJimBob extends HypixelNPC {

    public NPCJimBob() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Jim Bob", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "ZHETGYrzWXIOz/gbS7xRw0cKUORtUq+RI3ZsNoOJO1/wdlENkZFycgM9tJuDPTmB5bWMsQz7nZn8Gm/3UvvLoieLKrPXXLrEpccznRhXVZpFsa0ceBOpKNwmQ+jJ4zURhR5Qez5F7+gLQNpUsMJnFADHa6LDPIrA/8ZgNPKyomF7xmZVKSq+h+Nxv/myczRtV/PDKC3etTg4AEVm1NyTm5hvmK9DUYsiVA7HKPgPpdqp0/Tl8gw3rJWfa8laVFP2VGMDCG+UNbgpBPrFUNTBuuyZA4UKh9AJFs08qrEEmfykV3xjGOMlo7ZoRuslytnwPPMlff0yZw79nTd6xdKRyNC4E5jZsHxXnDU532D/P2rjRdTow9yECbcj7VuUkezJc4wCBqjfC91Zj6dZUHlyJtkSTwGX73eac35BXHCFaYGUmLREZTJ/JjJ/MINyepgkbo7utML0vS2bWO4vyjg4SicVBBbrmv0k3GhxlFRzkPuS4tbs3VCqy84qfLdIGixQpqttL3MiTjE8dgAsFCO7S6gqNnik0zEU4cShH72+3gHIP+GRIasL6E2SOHwIsNcAWl9ampF4Ecsl+YvEREJ4Oo1aSnzi4g6GLQN5ZQM7wn/5kmzD+kiMqqer9TCIeXHVinCI3tAQzf4cvb3ILiBuROzbEdrYhUqFv9jAXEqaclo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY4OTAyMTY0ODE1MiwKICAicHJvZmlsZUlkIiA6ICJmYzg3ZTI3YTYwZjY0NjdhOGMwMDgyMmI2ZWY5ZTMyNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJhbmRyZWlvX28iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUyN2ZiMzA2OTBkMGJlZTJiYzcxYzVmOWY3MWNkZjBlOThkZTlhYjliYjQ3YmM4Mzg3YTE3ODc1MjliNjkyNiIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(65.5, 71, -61.5, -90, 0);
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
                                "Have you seen Arthur's §eWheat Minion§f?",
                                "I can hear that cow mooing all the way over here.",
                                "I should have thought about that before buying this house!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
