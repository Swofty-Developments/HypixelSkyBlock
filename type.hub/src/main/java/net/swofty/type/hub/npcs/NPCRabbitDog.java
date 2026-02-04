package net.swofty.type.hub.npcs;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;

import java.util.stream.Stream;

public class NPCRabbitDog extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Dog";

    public NPCRabbitDog() {
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
                return new Pos(15.5, 69.25, 13.5, 16.9f, -21.1f);
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
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 26 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        Sound barkSound = Sound.sound(Key.key("entity.wolf.ambient"), Sound.Source.NEUTRAL, 1.0f, 1.0f);

        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "In the pursuit of excellence, one must remember that the journey is as significant as the destination.",
                                "True success lies not in the accolades, but in the lessons learned along the way."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "Consider the possibility that limitations are merely figments of our imagination, crafted by our fears and insecurities.",
                                "To break free, one must first believe in the boundlessness of their own potential."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Empathy is the silent language of the soul, spoken through actions rather than words.",
                                "To truly understand another, one must listen not only with ears but with the heart."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "The greatest paradox of life is that in seeking control, we often surrender it.",
                                "True power comes not from dominion over others, but from mastery over oneself."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "Wisdom often comes to us in whispers, through the leaves of the trees or the ripple of the waters.",
                                "It teaches us that every voice, no matter how small, has something of value to say."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "In every moment of decision, we stand at the crossroads of countless possibilities.",
                                "The paths we choose not only define our destiny but also reflect the essence of our being."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "Solitude is not the absence of company, but the moment when our soul is free to speak to us, unencumbered by the chaos of the world."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "Life, like a vast symphony, plays on the strings of our experiences, emotions, and encounters.",
                                "Each note, while fleeting, contributes to the eternal melody of our existence."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "Ah, Rabbit Bro, tirelessly toiling from the break of dawn, clinging to his routines with the desperation of a shipwreck survivor to a life raft.",
                                "One wonders if he runs from failure or merely jogs alongside it."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "And there strides Rabbit Sis, torchbearer of tumult, whose fervent protests might one day change the world, if they don't first incite her to single-handedly dismantle it, piece by bureaucratic piece."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "Ever observant Rabbit Daddy, guardian of the gold, manages finances with a paranoia that would make a conspiracy theorist blush.",
                                "His ledger is tighter than a drum, his brow perpetually furrowed in fiscal fear."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "Granny, with anecdotes as endless as eternity, dispenses wisdom like a vending machine stuck on dispense, whether you requested it or not.",
                                "Each story a subtle reminder that history repeats itself, especially at family gatherings."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-13").lines(new String[]{
                                "Observe the tranquil Rabbit Cuz, philosopher of sloth, whose profound punctuality issues suggest a man deeply at peace with life's ephemeral nature, or perhaps just deeply asleep."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-14").lines(new String[]{
                                "Bark!"
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-15").lines(new String[]{
                                "*pants*"
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-16").lines(new String[]{
                                "Woof!"
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-17").lines(new String[]{
                                "In the pursuit of excellence, one must remember that the journey is as significant as the destination.",
                                "True success lies not in the accolades, but in the lessons learned along the way."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-18").lines(new String[]{
                                "Consider the possibility that limitations are merely figments of our imagination, crafted by our fears and insecurities.",
                                "To break free, one must first believe in the boundlessness of their own potential."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-19").lines(new String[]{
                                "Empathy is the silent language of the soul, spoken through actions rather than words.",
                                "To truly understand another, one must listen not only with ears but with the heart."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-20").lines(new String[]{
                                "The greatest paradox of life is that in seeking control, we often surrender it.",
                                "True power comes not from dominion over others, but from mastery over oneself."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-21").lines(new String[]{
                                "Wisdom often comes to us in whispers, through the leaves of the trees or the ripple of the waters.",
                                "It teaches us that every voice, no matter how small, has something of value to say."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-22").lines(new String[]{
                                "In every moment of decision, we stand at the crossroads of countless possibilities.",
                                "The paths we choose not only define our destiny but also reflect the essence of our being."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-23").lines(new String[]{
                                "Solitude is not the absence of company, but the moment when our soul is free to speak to us, unencumbered by the chaos of the world."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-24").lines(new String[]{
                                "Life, like a vast symphony, plays on the strings of our experiences, emotions, and encounters.",
                                "Each note, while fleeting, contributes to the eternal melody of our existence."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-25").lines(new String[]{
                                "Ah, Rabbit Bro, tirelessly toiling from the break of dawn, clinging to his routines with the desperation of a shipwreck survivor to a life raft.",
                                "One wonders if he runs from failure or merely jogs alongside it."
                        }).sound(barkSound).build(),
                DialogueSet.builder()
                        .key("dialogue-26").lines(new String[]{
                                "And there strides Rabbit Sis, torchbearer of tumult, whose fervent protests might one day change the world, if they don't first incite her to single-handedly dismantle it, piece by bureaucratic piece."
                        }).sound(barkSound).build()
        ).toArray(DialogueSet[]::new);
    }
}
