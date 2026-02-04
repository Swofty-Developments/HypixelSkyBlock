package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;

import java.util.stream.Stream;

public class NPCRabbitSis extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Sis";

    public NPCRabbitSis() {
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
                return new Pos(18.5, 69, 24.5, 127f, 0f);
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
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 19 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "Hire me, " + player.getUsername() + "!",
                                "Together we can abolish the patriarchy, and ensure clean chocolate for all rabbits!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "Down with Big Chocolate!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Regulate! Regulate! Regulate!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Chocolate is love, chocolate is life. But at what cost?",
                                "It's time this family faces the music and listens to the cocoa beans' side of the story!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "They call me a troublemaker, a rebel. I say, I'm the only one talking sense!",
                                "Wake up and smell the exploitation, family!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "A protest a day keeps the unethical practices away.",
                                "Dad might not see it now, but I'm doing this for the future of all chocolate bunnies."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "They say I'm disrupting the peace, but I'm just trying to sprinkle a little truth on our chocolate-covered lies.",
                                "The factory needs a new recipe...for justice."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "Bro's always talking about his 'sigma grindset'. Tried to get me to read a book on it.",
                                "I told him I'd start my own movement: the 'chocolate mindfulness mindset'."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "Cousin might be the laziest bunny I know, but he's onto something.",
                                "Why protest when you can just 'be the change'? By napping, apparently."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "I told §aHoppity§f we should have a line of eco-friendly chocolates.",
                                "He asked if green food coloring counted.",
                                "We're...working on it."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "§aHoppity§f actually listens to my protests.",
                                "Well, more like he can't avoid them since I do it in the lobby. But it's a start, right?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "Hire me, " + player.getUsername() + "!",
                                "Together we can abolish the patriarchy, and ensure clean chocolate for all rabbits!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-13").lines(new String[]{
                                "Down with Big Chocolate!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-14").lines(new String[]{
                                "Regulate! Regulate! Regulate!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-15").lines(new String[]{
                                "Chocolate is love, chocolate is life. But at what cost?",
                                "It's time this family faces the music and listens to the cocoa beans' side of the story!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-16").lines(new String[]{
                                "They call me a troublemaker, a rebel. I say, I'm the only one talking sense!",
                                "Wake up and smell the exploitation, family!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-17").lines(new String[]{
                                "A protest a day keeps the unethical practices away.",
                                "Dad might not see it now, but I'm doing this for the future of all chocolate bunnies."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-18").lines(new String[]{
                                "They say I'm disrupting the peace, but I'm just trying to sprinkle a little truth on our chocolate-covered lies.",
                                "The factory needs a new recipe...for justice."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-19").lines(new String[]{
                                "Bro's always talking about his 'sigma grindset'. Tried to get me to read a book on it.",
                                "I told him I'd start my own movement: the 'chocolate mindfulness mindset'."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
