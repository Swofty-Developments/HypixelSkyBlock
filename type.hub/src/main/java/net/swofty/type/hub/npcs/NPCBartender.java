package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.GUIShopAlchemist;
import net.swofty.type.hub.gui.GUIShopBartender;
import net.swofty.type.skyblockgeneric.mission.missions.MissionKillZombies;
import net.swofty.type.skyblockgeneric.mission.missions.MissionTalkToBartender;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCBartender extends HypixelNPC {

    public NPCBartender() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Bartender", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "XV4wRQHNf+t8UNPxyCQWe9OTKABW2H2q8dKQJD6opc/UpjN8Ho5BZkqeCbCJ0Zdkq6YVzyQTctxOAVx99gi7FCUmtT02Z5lujim8zSemuzAN5ndYvHOBAjOJL51sbftnuGoCPBklmEAJ4uWWl+77mHe2GfXZkHTrBw0yvw777u2vtA8QJwoq2eh/8OPUFWSRtJVeW9kIggwfjJbVYjP7w1im5DKklvL7Tw71TuRx+1VebWhpD3lOTtfq1Vo6ri+LOs4o36Ix/Ec2xnmjeV2BF0CK6gkIbzaMcF4efFHxonmW2GRXL+E/tIpvAm4sY5JR5z/jV4Mp6qEN0CaU5WR8DSdkwLMTrRGuzRoZUjvZL2B6kZ7yaVmpOo7PeNVAr8hPRjAB489qJVLDawfpVCNt4jgQMMDBJUPk8F4DJPBaUMFZXNM4BO9B6DH6xVHNsaZhOZZu5tKKyXBg0yuT7FA1OgaFcye8z+JSIDHNd3kxcR02idHDmI1pDL6da2pdPoAmz19I5Ao7rI9kXPMdJUPmY7aIEd4j6RXXXnJ3UqUcKnDQqf3ElwSbZXayTo/Wn6P9KFa2aTjR/gfvIjf7+Jn9vyVGtbFG8x+xg1oSZR4RE2rmOhHQKEETaXakqbRMWUt1EHWm4c/HpxpxuRSNvFAwkvdrV4mt8VrCo0x+A/Z/3cQ=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYwODIxODk0MDY5NiwKICAicHJvZmlsZUlkIiA6ICI3MzgyZGRmYmU0ODU0NTVjODI1ZjkwMGY4OGZkMzJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJb3lhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYzkyNTgwODcwNzRlYjEzMTRkN2EwZGQzM2QxZmNiNWNlYmYzZjZmMWE0ZThkMjM1NzIxYmMyNTliNGU0OTZhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-83.5, 71, -47.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (!player.getMissionData().hasCompleted(MissionKillZombies.class)) {
            if (isInDialogue(player)) return;
            if (player.getMissionData().isCurrentlyActive(MissionKillZombies.class))
                setDialogue(player, "quest-talk");
            else
                setDialogue(player, "quest-hello").thenRun(() -> {
                    player.getMissionData().startMission(MissionKillZombies.class);
                });
            return;
        }
        if (!player.getMissionData().hasCompleted(MissionTalkToBartender.class)) {
            if (isInDialogue(player)) return;
            setDialogue(player, "quest-complete").thenRun(() -> {
                player.getMissionData().endMission(MissionTalkToBartender.class);
            });
        }

        player.openView(new GUIShopBartender());
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Welcome to the Bar, friend!",
                                "These are trying times, indeed. The §cGraveyard §fis overflowing with monsters! Anyone who comes in is spooked off by the grunts of zombies in the distance.",
                                "Could you give me a hand? If you help clear out some of these monsters, I'll pay you for it."
                        }).build(),
                DialogueSet.builder()
                        .key("quest-talk").lines(new String[]{
                                "Clear out some more of those Zombies and I'll pay you greatly for it!"
                        }).build(),
                DialogueSet.builder()
                        .key("quest-complete").lines(new String[]{
                                "Words cannot describe how thankful I am!",
                                "That whole area is very dangerous, but can be quite rewarding for a warrior such as yourself.",
                                "If you're up for the challenge, both the §cGraveyard§f and the §cSpider's Den §fbeyond it are great training grounds for improving your §aCombat Skill§f.",
                                "For now, here's a reward for helping me out!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}