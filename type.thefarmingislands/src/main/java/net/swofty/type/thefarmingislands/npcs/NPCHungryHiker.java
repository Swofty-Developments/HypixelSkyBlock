package net.swofty.type.thefarmingislands.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCHungryHiker extends HypixelNPC {
    public NPCHungryHiker() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Hungry Hiker", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "cIr8X0Mtz1Ok7wybAmWuIpersxILt4FbKNsIyDaQ3g90dd1Uq5qlvUT4CvRJigKBZZhMAeJOrRVWe+U+f4BqAXwo5V7lhLxhvBhVNYzE6+AsAh7V8kTJtMiymLtVVeRsuVWaeH2C5lMPQUHl9bzEgCPnMdLGEF4CRogVSXMoXcSj+ev6q7Vk7eNPEIPD2ws4p+N+MZJQ9lQHvh1GuCMqomW4TQ805fUJtY1d23xQ7fSVZr4M81kBEQSQWdVLoq38+LPYUr5RYxUfXYPyb10bET2mIS27blnwrEdpv+h5nFrpibCXHvxEQzfL7r8jf7jmn08pzdptbOebrnktzbsa+LQw5yHfkAgK0Z7NJMBADpeG3ek/mv5TGbIerNQo4KVx/OZNfR+jCr1gxnSKDKUB5ye59BuSqKVzi2XICvNynxnl6UfN3CQXK3iZPVXA7laFDmULftKXx5FFCQCFUzitxdOEcDO6GBhp0ZIC2FyIMGfA9attcmu6QDx0L0H6O54sDrOQRVBKfyBR5DW0isQQ6OpBVxsEQtL0ny+wZ8VAlQNuVaiYTQMp7O9J0sjJfjtgjyfGvRFHF0G+MFCH3NuY/ch8jt02CKEUDEaSSaCtbvPM/b9DtEPSUkEMrXWiVyvhLfiFuolQnfX8KXS6l6Dbq5wpTS/DQIPtUTLTDO8zLnI=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNzAzNjc3MTUyOSwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNmY5YjliNjI0MDRjNWNmOTRjMzg1YzdmMWM4NmFkZGUwZDlhZWY5YTljZDI5ZTJhYjU4M2ZhMWRlNjNmNDI2IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(269, 48, -480, -130, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().notImplemented();
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "Hello there, stranger!",
                                "I fell down into this ravine a couple days ago and can't climb out.",
                                "My friend Jake said he would come get me but he hasn't arrived yet.",
                                "Could you bring me food until he gets here?",
                                "Could you get me # of this food?", // TODO: Add food number to dialogue
                                "The food I want is <food>." // TODO: Add food item to dialogue
                        }).build(),
                DialogueSet.builder()
                        .key("incorrect-food").lines(new String[]{
                                "This isn't the food I wanted, please get me the correct food.",
                                "I asked for food that is a <food>." // TODO: Add food item to dialogue
                        }).build(),
                DialogueSet.builder()
                        .key("incorrect-amount").lines(new String[]{
                                "This isn't the correct amount I asked for.",
                                "I asked for <amount> of this item." // TODO: Add food number to dialogue
                        }).build(),
                DialogueSet.builder()
                        .key("correct-food-and-amount").lines(new String[]{
                                "Thanks for the food.",
                                "This should fill me up for 144 SkyBlock days.",
                                "Come back before then so I don't perish!"
                        }).build(),
                DialogueSet.builder()
                        .key("finished-quest").lines(new String[]{
                                "Thanks for the food!",
                                "I think I see Jake on the edge of the ravine!",
                                "Thank you for feeding me all these days, meet me at the house near the portal and I will give you something to show my gratitude!"
                        }).build(),
                DialogueSet.builder()
                        .key("after-quest-is-completed-in-his-hut").lines(new String[]{
                                "Thank you for feeding me all those days.",
                                "To show my gratitude, please have this!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
