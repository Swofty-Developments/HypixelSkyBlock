package net.swofty.type.spidersden.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCArchaelogist extends HypixelNPC {
    public NPCArchaelogist() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        ChatColor.AQUA + "Archaeologist",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "T7z9Er9geL6Y9+z7o0u7BUc5kTlbrWw+w6jNDYecdJeTX+u7XfB7C0bgYD4e2MQawQxvI8otTTA8GWrvlgjtfP/xiQD70baSd1u4/bVRKDcquG6Knn7ALmLz34WDlIjZT7N3yVPVuAnauFeYKySCe77pphoaBJCySOfDjr78zeYvTsoWNmq+AJ2BlpsMfErhzKgiuZbRFNEIrjVyUcm/OxWFiqi8nODA85St4Ka4dG9q7cUAjZbI9mfz0cUWPyGe9k1S6Zv2MXiWh/9jNGXXPzjAIPK+uXynm5ihhxJPUJfvB0HE/UX2y3clHwqLlSiG93ju/TE8EBwCXxbPkzlFny2K1aAYlTfhxoTu4J5S6vDleFBhIBTxslneEyALgsYarpS37e0cta6cNx4dwvENaZfR/vekqLfAR6G5FV4P+6q9FbfSsI5rhfpnbgsluY9hkt1J5GCzPVyH1+Lfxse8310yhQBQ0HCDvX7SG5cioXxzFs1Sr/25/bC3/BpfG1Z6PjrdKuTymz5DPbu/pkjXkbti+1yGYmBDbSFz+zurgKxerkzWSumtApuIGGX7nc43XyKBoklfq/5sEZ4IuIijlIlD1pDWtxrpxkSdhFRFibY71PAV/18bez+ABFpr1lbydPCvo8O8yiq0NTAAc8hw66kL3z6cpagvq7SiQOWdPf0=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjgwOTU0ODAzMCwKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNeVV1aWRJcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ODVlODNhMDk5NTFhMmMyZGI3ODM2MGJjOTU0NmJiMGQ0NTQ5NzViNWU1YTM0NGNhMWRhZmY0MDhhNjdlOWYxIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-360.5, 111, -290, -41, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "Hey, are you an adventurer?",
                                "There are " + ChatColor.AQUA + "Relics " + ChatColor.WHITE + "I am searching for all around this island!",
                                "But I am not strong enough to find them.",
                                "Take my compass to assist you in your journey!"
                        }).build(),
                DialogueSet.builder()
                        .key("before-finding-all-relics").lines(new String[]{
                                "Some spider eggs look a bit different, check behind those.",
                                "Are you sure you looked through every cobweb?",
                                "Down there, multiple rooms are blocked by cobwebs.",
                                "The spiders are hiding some of the relics behind cobwebs.",
                                "Look around spider eggs too, and cobwebs!",
                                "Dig through gravel and you should be able to find a relic.",
                                "I'm fairly sure there is a Relic nearby the dragon's tail.",
                                "Don't forget to look down those caves, there are plenty down there lying around.",
                                "I managed to find one behind a spider egg, but once again I just couldn't reach it...",
                                "When in doubt, check behind cobwebs!",
                                "Look up around the caves, some spiders have their nest behind some cobwebs up there.",
                                "I won't lie, some of those I could find myself, but I hate cobwebs.",
                                "Spiders often dig up mushrooms and create tiny rooms below, make sure to check those!",
                                "There might be some below mushrooms too!",
                                "One of the Relics is inside a tiny hole inside the caves.",
                                "There are some relics up there hidden on the Spider's Den.",
                                "Always check the roof in the caves, there are holes behind the cobwebs.",
                                "You know that breakable wall in the caves? Look around it.",
                                "Walk around the edges, you will probably find some cave entrances.",
                                "I know a spot down in the caves but I just have no idea how to get into that room, the entrance is tiny.",
                                "There is a relic a little below a Fairy Soul in the caves.",
                                "Make sure you check all the mushrooms, sometimes there are tiny holes behind them.",
                                "Sometimes look behind leaves too, there are many hidden caves on this island!",
                                "I believe there is one down in the spider cave, behind a wall of cobwebs.",
                                "I heard rumors that there is a man-made hole inside the cave, with a Fairy Soul even!",
                                "Are you sure you checked every mushroom?",
                                "I found one below the house too but I really can't access it, I'm not sure how to get there...",
                                "Pretty sure there is one nearby the Dragon skull."
                        }).build(),
                DialogueSet.builder()
                        .key("after-finding-all-relic").lines(new String[]{
                                "Wow! You found all the Relics.",
                                "Take this as a reward, you've earned it."
                        }).build(),
                DialogueSet.builder()
                        .key("after-finding-all-relics-and-fairy-souls").lines(new String[]{
                                "I found an interesting specimen in the spider caves, perhaps there's other stuff to find in there."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
