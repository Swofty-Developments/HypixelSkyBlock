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

public class VillagerJack extends HypixelNPC {
    public VillagerJack() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"&fJack", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-0.5, 70, -54.5, 0f, 0f);
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
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Increasing your Foraging Skill Level will permanently boost your §cStrength",
                                "Increasing your Enchanting and Alchemy Skill Levels will permanently boost your §aIntelligence."
                        }).build(),
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Your §aSkyBlock Profile §fin your §aSkyBlock Menu §fshows details about your current stats!",
                                "There are 7 stats in total, including §cHealth§f,§c Strength§f, and §aDefense§f.",
                                "Equipped armor, weapons, and accessories in your inventory all improve your stats."
                        }).build()
        ).toArray(DialogueSet[]::new);
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
}
