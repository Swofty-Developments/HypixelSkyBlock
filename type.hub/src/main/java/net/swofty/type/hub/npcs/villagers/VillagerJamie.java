package net.swofty.type.hub.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUIJamie;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class VillagerJamie extends HypixelNPC {
    public VillagerJamie() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"&fJamie", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-36, 68, -38);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.NONE;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;

        MissionData data = player.getMissionData();
        if (data.isCurrentlyActive("speak_to_villagers")) {
            if (data.getMission("speak_to_villagers").getKey().getCustomData()
                    .values()
                    .stream()
                    .anyMatch(value -> value.toString().contains(getClass().getSimpleName()))) {
                if (System.currentTimeMillis() -
                        (long) data.getMission("speak_to_villagers").getKey().getCustomData().get("last_updated") < 30) {
                    setDialogue(player, "quest-hello").thenRun(() -> {
                        new GUIJamie().open(player);
                    });
                    return;
                }
            }
        }
        setDialogue(player, "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "You might have noticed that you have a Mana bar!",
                                "Some items have mysterious properties, called Abilities.",
                                "Abilities use your Mana as a resource. Here, take this Rogue Sword. I don't need it!"
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "You might have noticed that you have a Mana bar!",
                                "Some items have mysterious properties, called Abilities.",
                                "Abilities use your Mana as a resource."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
