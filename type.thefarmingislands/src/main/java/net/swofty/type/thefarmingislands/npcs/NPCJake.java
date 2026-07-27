package net.swofty.type.thefarmingislands.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCJake extends HypixelNPC {
    public NPCJake() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Jake", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "pMvZ+g1p1HCldbVKd1w05q4HyluUjAeEJipZQe7omlFQdiF5PIoHo6sY7k1/dwPQwXgUGsytvxnFfIiWAflryAC5FrxLpTLhrghbuf0zgbNtrjhA6s67txvbp1jpAs7k4ydufiQ39Ob/Y7+Ojx8ZiPMb6UK5OrwPmUmyq1KxloEl6N2qzA1odf7sSXuVxrFlbPwT8p9rEsV8ogCUmzmTiPqkIAGUCWJ3ZbRDOFjOKbrD8Ff0/7qQ6g0BDrOLNPWeHl/eJHT8puCOktNIQs1lwYyNF9gllpkicFOQ9XqCJH8END6jyyF0wgfubFAFB1eoQKGRenmuY4ktJQT+D3yZlOzV20zbtGzUvVvkC7ooYyWFLY3L6m5NCEUNwU5XJbc1hK9PQH1zajY+v1Lp09DsVmiib4NjDsUvb6nF6BISH6w3J9ZNLlwWcoawXs5EqLQbpQxDXuNkD1q3FszvuCoRYOgmXSZqverQ+v5L18BnUjDMM1bUm5r/gH/hPp06Xc5jZmzhYsajqKwhCnsyYY8NS3PD3BIM96a3XhheEVoFZRvg7OHDQtQgzAydJE9bnJmFTFyThUR5ZH11OlSPmTMToKdOUaj+TbhEPGUXPolQ6gpHbcpRVD93RUbKqfPYIaQvgnPMiF6rWJlOHaYetFEmgIeAQGHzEG8KzTvKQChZQ14=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5NjA3ODI4NjQ3MywKICAicHJvZmlsZUlkIiA6ICJkODAwZDI4MDlmNTE0ZjkxODk4YTU4MWYzODE0Yzc5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVCTFJ4eCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNTFjYzdjMWQxMWFhMGEwOGE3M2NiYzE4NDU0NzA1MWFlYmQwN2IzYmI1MGQ4NmYxMTg4ZmQ3YTdkYWQyZWQzIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(261.5, 184, -565.5, 90, 0);
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
                                "Hi I'm Jake. I've been living on this island for 25 years.",
                                "I used to study animals from all over the land!",
                                "Unfortunately, I suffered an injury which restricts me from leaving my house.",
                                "I really enjoyed studying animals.",
                                "Could you bring me an animal? You can find them all over the island!"
                        }).build(),
                DialogueSet.builder()
                        .key("without-an-animal").lines(new String[]{
                                "Could you bring me an animal? You can find them all over the island!"
                        }).build(),
                DialogueSet.builder()
                        .key("another-player-brings-an-animal").lines(new String[]{
                                "Someone already brought me an animal!",
                                "I'm feeling generous, I'll show you my store."
                        }).build(),
                DialogueSet.builder()
                        .key("same-animal-given-while-shop-is-open").lines(new String[]{
                                "Someone already brought me this animal recently, but since you put in all the effort I'll open my store for you.",
                                "I'm feeling generous, I'll show you my store."
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-cow").lines(new String[]{
                                "Wow a cow!",
                                "How did you get this over the void? Incredible!",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-mooshroom").lines(new String[]{
                                "Wow a mushroom Cow! How did you get it out of the Gorge?",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-chicken").lines(new String[]{
                                "Wow a chicken!",
                                "Did it fly over here?",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-pig").lines(new String[]{
                                "Wow a pig!",
                                "How did you get this over the void? You must be really strong.",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-rabbit").lines(new String[]{
                                "Thank you for bringing me this rabbit!",
                                "To show my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-a-sheep").lines(new String[]{
                                "Thank you for bringing me this sheep!",
                                "To show my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-an-oasis-rabbit").lines(new String[]{
                                "This rabbit looks a little funny, did you fish it out of the Oasis?",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-an-oasis-sheep").lines(new String[]{
                                "This sheep looks a little funny, did you fish it out of the Oasis?",
                                "To show you my gratitude, I have a special item to sell you!"
                        }).build(),
                DialogueSet.builder()
                        .key("brought-the-loch-emperor-with-necromancy").lines(new String[]{
                                "WOW!",
                                "The legendary Skeleton Emperor!",
                                "I searched all my life to find this creature but was never able to find it!",
                                "To show my gratitude, I have a special item to sell you!"
                        }).build()
                // todo: more dialogues
        ).toArray(DialogueSet[]::new);
    }
}
