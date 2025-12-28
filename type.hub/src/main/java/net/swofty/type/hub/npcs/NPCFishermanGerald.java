package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCFishermanGerald extends HypixelNPC {

    public NPCFishermanGerald() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§3Fisherman Gerald", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "oJ24ajDV0/I3NFdHBh7D71v+jboQFJlaFxxu47bWeSmUXhLl6z1Vk9aksUE8qqTNs9EVUWFpSjAe7i/w57nkh4AAH+GvplyzZANEhHf9SJhBdwjCpIDVJZ453hs9xMYbyvp4KiZkia+jbLKrQfQOFOa9aWt1mmhhOneNgzx4it5Bo1qzDoPvbgFu5uL7rbyzdOl9ZW5wEobb2Ns4TbqPdT+NjZurw7rkpRpdLhAbHZoD2NEw0BX3VHTvtlY8zh14//YV7Vo5+xUUWGTrt0UjudIxDdJI8R9ZiWgCgl9N1ElzokFh/h0aHg0vL0QSG1Y5bHY5ea+E2+3tLDiEvQO7Soh/VV1/yySjmkt/JbUiEFmCv6vkjm4bgbAZAm42GxlvkAyFpZoFZijmCaw8ObJivZlwJNUjY4D0PBEm4rnVSVFjjWBGaGXyFG2/KtUL8nYZE81ABqrL3xSHFeEUIBePNsBq84eI88aNGYCjU9Ct4bfAhbdZMWM84PzBqAa5jH6NNqb/5aV2jmEp0OcoF80W+pcaR/uNPOo9Gjy5HkUYMpLJ775SqC//m/Rrh7RypdNcVUIanmUqP+hP7oz1SI3L6glv4+CGlGprr67QHP9d9PcZzVgF1YHOfOKW4muqJNjgpKYFZRiz4yWmOdURKomqUuz4tyKDxu0drx5eHfQ+3mw=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NTk1NzQ0MjU3OTEsInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNjc1ZjA3MWVmYjBjZjI3YmYxNjA4MWUzZjgyZjliNWY4YWU4OGVjYTllZTk1MjNiNjIxNmU2MTdmNmY0NWM5In19fQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(153.500, 68.500, 55.500, -60, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        if (isInDialogue(player)) return;

        boolean belowFive = player.getSkills().getCurrentLevel(SkillCategories.FISHING) < 5;
        if (belowFive) {
            setDialogue(player, "below-fishing-5");
            return;
        }

        if(!player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISHERMAN_GERALD)) {
            setDialogue(player, "first-interaction").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISHERMAN_GERALD, true);
            });
            return;
        }

        // TODO: finish this quest
    }

    @Override
    protected DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("below-fishing-5").lines(new String[]{
                                "Hmm, you're not quite ready yet.",
                                "Go talk to the §eFish Merchant §fin the §bFishing Outpost§f, get geared up, and get fishin'!",
                                "Come back once you've reached §aFishing Skill V §fand I'll tell you how you can get to a §anew island§f!",
                        }).build(),
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "Keep the noise down, kid!",
                                "If you want to learn about §aFishing§f, go talk to my wife, Fisherwoman Enid.",
                                "She's fishing a bit §bupstream§f. Once she's shown you the ropes, come back and talk to me!",
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
