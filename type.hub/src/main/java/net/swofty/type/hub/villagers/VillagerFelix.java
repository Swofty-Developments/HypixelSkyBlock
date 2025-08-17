package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class VillagerFelix extends NPCVillagerDialogue {
    public VillagerFelix() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fFelix", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-25.5, 68, -103.5, -135f, 0f);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.BUTCHER;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;

        MissionData data = player.getMissionData();
        if (data.isCurrentlyActive("speak_to_villagers")) {
            if (data.getMission("speak_to_villagers").getKey().getCustomData()
                    .values()
                    .stream()
                    .anyMatch(value -> value.toString().contains(getID()))) {
                if (System.currentTimeMillis() -
                        (long) data.getMission("speak_to_villagers").getKey().getCustomData().get("last_updated") < 30) {
                    setDialogue(player, "hello");
                    return;
                }
            }
        }

        setDialogue(player, "hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "&e[NPC] Felix&f: You can access your §aEnder Chest §fin your §aSkyBlock Menu§f.",
                                "&e[NPC] Felix&f: Store items in this chest and access them at any time!"
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
