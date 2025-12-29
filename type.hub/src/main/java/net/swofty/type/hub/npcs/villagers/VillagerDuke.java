package net.swofty.type.hub.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class VillagerDuke extends HypixelNPC {
    public VillagerDuke() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"&fDuke", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-11.5, 70, -95.5, -40f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.BUTCHER;
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
                    setDialogue(player, "quest-hello");
                    return;
                }
            }
        }
        setDialogue(player, "initial-hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "I found a few Fairly Souls during my travels, they are usually pretty hard to find!",
                                "I would not venture South of the §bVillage§f, it seems like this place was abandoned."
                        }).build(),
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Are you new here? As you can see there is alot to explore!",
                                "My advice is to start by visiting the §bFarm §for the §bCoal Mine§f, both North of here.",
                                "If you do need some wood, the best place to get some is West of the §bVillage§f!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
