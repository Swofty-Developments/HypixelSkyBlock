package net.swofty.type.spidersden.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCOption;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.missions.spidersden.MissionTheFlintBros;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.List;

public class NPCMichael extends HypixelNPC {
    public NPCMichael() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        ChatColor.GOLD + "Michael",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "hYEEZ1GDXbaBQ1dhtSksEmonm3NnUFbpachHVqYutTfbr6NopYa+KaVsMvPuc6m/2H94/OGd+3Zz1Rz6uLr30yUqTM3xCuGA31uEVnnvRKvctmmwnvRAcQvVAAZ2ALM0nCM89rpiKiLxQrkp3KEEnxnrxNuM3t+72tJztqU1r2IutGVpgR+HbhjZDFq0ZFDzBBWpQKeP/JKcqUqy7sIievjiYmplMUKnSFVKoNUoiHmxyuqLza//GYnUsngQUacuYSA5qi/3yYaQgSpRysr0ucklBMtQbk5NHij1FZsqnkGQEIm+BU81BGn3lmSHDYZWjzoYt+yKndm7Ui526TC+s3xmHHd+Nf1Zua2LaGNVGAIxBbiIKP0s4WDu9WElDRyBVf84DkpTVR3txldex4KUlMJclcXwjX389YCBzGg1ZhTrPgts9WN7Z2lHPWhKl5Dz9wZ1H0hTM2NEMwYfLBKVbcO3FXczrdGY5l8Sk6TcXIxpxTWhu6Tl0M2hpTSdvEowo5+G4p01rBmSixg7B5/uaCKcL93sec+nMYakNnFbxVElIJtjw/Yu8vjFMPhWaXDRnQW4UjfzuQ8gm2+nDeVYX3Cjvh0+CKbWqLvGAPLV46o5EAGdWl3r8GzJV6cKt8+e+8uIT9oH05F7v7Ik68QXFrGDL5YQfYmFkt+R+hVZqg8=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTc0NTg0NTgyNTYwOSwKICAicHJvZmlsZUlkIiA6ICJiOWQwMWZhYmIwZWY0ZmVlOWUxMTAwMDA4MWRmMzY5ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJtMXdhYV8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk0Nzc2ZGM1YzQ2MzA3YjFmMWI3MzBkMzU5NmNjMzM4MzhhZTkwNjM0ZjgxMDBkYTU5OGJjY2ZlMzc0NjZmYSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-264.5, 94.5, -284.5, -90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (dialogue().isInDialogue(player)) return;

        setDialogue(player, "initial-hello").thenRun(() -> {
            NPCOption.sendOption(player, "michael", true, List.of(
                    new NPCOption.Option(
                            "dont-know",
                            NamedTextColor.GREEN,
                            false,
                            "No, I am verry silly",
                            (p) -> {
                                setDialogue(player, "selected-dont-know-mob-types").thenRun(() -> {
                                    setDialogue(player, "explaining-mob-types");
                                });
                            }
                    ),
                    new NPCOption.Option(
                            "already-know",
                            NamedTextColor.RED,
                            false,
                            "Yes, I am very smart",
                            (p) -> {
                                setDialogue(player, "selected-know-mob-types").thenRun(() -> {
                                    setDialogue(player, "explaining-mob-types");
                                });
                            }
                    )
            ));
        });
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("talk-to-ike-before-michael").lines(new String[] {
                                "Hey, come talk to me first!"
                        }).build(),
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[] {
                                "Wassup! We're out here on a camping trip!",
                                "I don't really know why we chose the " + ChatColor.GRAY + "⏣ " + ChatColor.RED + "Spider's Den" + ChatColor.WHITE + ", though...",
                                "Still, I've been playing lots of games to pass the time.",
                                "Quick! Trivia question! Do you know what " + ChatColor.RED + "Mob Types" + ChatColor.WHITE + " are?"
                        }).build(),
                DialogueSet.builder()
                        .key("selected-know-mob-types").lines(new String[] {
                                "Cool, cool...",
                                "Well, I'm going to explain it anyways, otherwise my very existence lacks purpose!"
                        }).build(),
                DialogueSet.builder()
                        .key("selected-dont-know-mob-types").lines(new String[] {
                                "No problem! Allow me to explain."
                        }).build(),
                DialogueSet.builder()
                        .key("explaining-mob-types").lines(new String[] {
                                "So. " + ChatColor.RED + "Mob Types" + ChatColor.WHITE + ".",
                                "Every Mob has at least " + ChatColor.GREEN + "one" + ChatColor.WHITE + " type, which you can see next to its name.",
                                "For example,  Spiders are classified as " + ChatColor.DARK_RED + "Arthropods" + ChatColor.WHITE + ", which you can tell because of the symbol next to their name.",
                                "Most mobs have " + ChatColor.GREEN + "one" + ChatColor.WHITE + " type, but some have " + ChatColor.RED + "more" + ChatColor.WHITE + ".",
                                "My mate " + ChatColor.BLUE + "Ike" + ChatColor.WHITE + " knows which types all the different symbols represent.",
                                "Go talk to him if you want to learn more!"
                        }).build(),
        };
    }
}
