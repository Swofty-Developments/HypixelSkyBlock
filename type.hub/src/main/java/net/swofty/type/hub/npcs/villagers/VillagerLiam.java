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

public class VillagerLiam extends HypixelNPC {

    public VillagerLiam() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"&fLiam", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(10.5,70,-41.5);
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
                    setDialogue(player, "quest-hello");
                }
            }
        }
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Did you know you have a SkyBlock Level?",
                                "In fact, everyone does! You can see them in the tab list by holding [TAB]!",
                                "You can level up by playing every aspect of the game!",
                                "If you're curious, you can view your level, and more information in your SkyBlock Menu!"

                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
