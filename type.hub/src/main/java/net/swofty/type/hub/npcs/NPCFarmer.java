package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionCollectWheat;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionTalkToFarmer;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionTalkToFarmerAgain;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCFarmer extends HypixelNPC {

    public NPCFarmer() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Farmer", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "F0DZ8COJZ2+j9OpMbK87nw6jjW9a1No40LxYZlIab1BdD68DIHBoUc4bA6XEisWhn0vtFvaV988j/IdYaGEo+O7+TZgrXpz5sgEzeP7BU9XRburiclHTJeY73PhX6MgdPuVcH2dhTkqp+kdqSmPme8xDvRiuenV9TC23ixuPpW+gduXYr1H3UoAx2H/7UVVznWw+WteBtOY3ENTv742nBmituYmyVx0cbmz6rPtoWdzxvQl2mnQqF68iSC9ADLGqhP4xFX3ERLX9uQKG27Lh27svSMPfy+ow5Ra9JV06Vac+j4THFMgVuVpp+UQgFQLmkCn0JyazNYJQkXirSSjkTkofFfMVPQJtal+6YJ8plBlgTXxA+MSuEUIiYJCK1LjQaxr6CW8U+uGY8kvo1w+C1J6iOyWdApvGrZEbCaXbR3Hu0E5AZFfjbscM6mLjh4N8jj/9tBcy1HKY8Lau2CkSI8bs00C8h0nzgb9W126m+jsFpNawoJVC4RF9mxh0576Y0kwzbgX+mFUfavrwFcsnhaRL1pyrUEMmmJ3j7vka7enezT+siGJ8KC4A2rMAuTGpSRpL4Ot6NxWWD6wv4bMRzNmHz+yEmqPGqZu4SsC0GSzS3bSVpK7o5lwzxYmQZFQZNRHmo70nWvPdQxcGsGe3ylZ88lb+78iS9itJ9Mbb/As=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU4OTg1MDk0MDAyNiwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI2NTJkZDA4MTRhYjIxMzRmZmY5MzYzZmIyNTk2MzI5Njk5YzFkM2IyYjc5OTNmYzI5NzdiZjJjZTYxNTEyOSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(44.5, 72, -162.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;
        MissionData data = player.getMissionData();

        if (data.isCurrentlyActive(MissionTalkToFarmer.class)) {
            setDialogue(player, "initial-hello").thenRun(() -> {
                data.endMission(MissionTalkToFarmer.class);
            });
            return;
        }
        if (!data.hasCompleted(MissionCollectWheat.class)) {
            player.sendMessage("My cow is getting worse! Hurry with that Wheat!");
            return;
        }
        if (!data.hasCompleted(MissionTalkToFarmerAgain.class)) {
            setDialogue(player, "spoke-again").thenRun(() -> {
                data.endMission(MissionTalkToFarmerAgain.class);
            });
        }

    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return List.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Howdy, friend!",
                                "My cow is sick, she needs some food to replenish her strength.",
                                "Could you gather some Wheat from my farm and bring it back to me so that I can feed her? Poor thing."
                        }).build(),
                DialogueSet.builder()
                        .key("spoke-again").lines(new String[]{
                                "Thank you so much!",
                                "My Farm is yours to harvest! Wheat is a valuable resource to collect, you can unlock many cool things by collecting it."
                        }).build()
        ).stream().toArray(DialogueSet[]::new);
    }

}
