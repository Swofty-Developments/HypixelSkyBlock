package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;

import java.util.stream.Stream;

public class NPCRabbitUncle extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Uncle";

    public NPCRabbitUncle() {
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
                return new Pos(-18.5, 71, 16.5, -105.5f, 0f);
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
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 25 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "Grandma always said I could be somebody if I put my mind to it.",
                                "She was wrong."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                player.getUsername() + "...you have to hire me. Please."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "I should be in the Hall of Fame by now.",
                                "Instead, I am here. Please, hire me."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Back in high school, I was just one play away from that state championship.",
                                "If only I had zigged instead of zagged..."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "I used to chuck that ball like nobody's business. Those were the days, eh?",
                                "Still got some of those moves, you know."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "Every time I see a football, I can't help but wonder 'What if?'",
                                "Was so close to grabbing that championship ring."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "Sometimes, late at night, I replay that final drive in my head.",
                                "I could have been a legend, you know?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "Man."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "I've got the entire high school trophy case memorized.",
                                "Sometimes, I give tours. You know, just to keep the legacy going."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "Cuz might not have the typical athlete's discipline, but he's clutch.",
                                "Just like my old teammate who didn't show up to every practice, but could score tuddies when it mattered most."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "§dHoppity§f's like the star quarterback of this chocolate game. Always looking for that next big play.",
                                "He's got that championship mindset, just like I had."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "Sis has that fire, like the team captains back in my day.",
                                "She doesn't just play - she changes the game.",
                                "Always rallying the troops for her cause, much like a good quarterback does in the fourth quarter."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-13").lines(new String[]{
                                "Sure, Rabbit Bro's up at dawn doing push-ups and planning his day.",
                                "I used to be like him, then I tore my ACL.",
                                "My knee has never been the same since, man."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-14").lines(new String[]{
                                "Cuz showing up to work is like a trick play.",
                                "You never see it coming, and when it happens you can't help but wonder if it was a fluke."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-15").lines(new String[]{
                                "Grandma always said I could be somebody if I put my mind to it.",
                                "She was wrong."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-16").lines(new String[]{
                                player.getUsername() + "...you have to hire me. Please."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-17").lines(new String[]{
                                "I should be in the Hall of Fame by now.",
                                "Instead, I am here. Please, hire me."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-18").lines(new String[]{
                                "Back in high school, I was just one play away from that state championship.",
                                "If only I had zigged instead of zagged..."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-19").lines(new String[]{
                                "I used to chuck that ball like nobody's business. Those were the days, eh?",
                                "Still got some of those moves, you know."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-20").lines(new String[]{
                                "Every time I see a football, I can't help but wonder 'What if?'",
                                "Was so close to grabbing that championship ring."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-21").lines(new String[]{
                                "Sometimes, late at night, I replay that final drive in my head.",
                                "I could have been a legend, you know?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-22").lines(new String[]{
                                "Man."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-23").lines(new String[]{
                                "I've got the entire high school trophy case memorized.",
                                "Sometimes, I give tours. You know, just to keep the legacy going."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-24").lines(new String[]{
                                "Cuz might not have the typical athlete's discipline, but he's clutch.",
                                "Just like my old teammate who didn't show up to every practice, but could score tuddies when it mattered most."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-25").lines(new String[]{
                                "§dHoppity§f's like the star quarterback of this chocolate game. Always looking for that next big play.",
                                "He's got that championship mindset, just like I had."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
