package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;

import java.util.stream.Stream;

public class NPCRabbitCousin extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Cousin";

    public NPCRabbitCousin() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{RANK.getHologramLine(), RANK.getChatName(NPC_NAME), "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                // TODO: Add skin signature
                return "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                // TODO: Add skin texture
                return "";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(18.5, 69, 17.5, 71.7f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public String chatName() {
                return RANK.getChatName(NPC_NAME);
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 8 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "My parents have been hounding me to get a job! Please hire me!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "I need a job soon, or my parents will kick me out of the warren."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "I tell ya, family gatherings got a lot more interesting when §aHoppity§f started bringing those experimental chocolates.",
                                "Remember the carrot crunch debacle?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Working hard or hardly working? With chocolate, it's both. I might drift in past noon, but when I'm on, I'm on fire.",
                                "They say genius often looks like laziness. Guess I'm living proof."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "When I finally get to it, even §aGranny's§f impressed with the flavors I whip up."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "I suggested chocolate-covered carrots to §aHoppity§f once. He laughed until he tried it. Now, who's laughing?",
                                "Still him, because it was a terrible idea."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "My parents have been hounding me to get a job! Please hire me!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "I need a job soon, or my parents will kick me out of the warren."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
