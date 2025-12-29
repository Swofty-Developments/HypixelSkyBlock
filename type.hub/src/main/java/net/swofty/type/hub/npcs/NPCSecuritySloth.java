package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCSecuritySloth extends HypixelNPC {

    public NPCSecuritySloth() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§a§lSTAY SAFE!", "§cSecurity Sloth", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "X/tHnZt+iPtRGOCFm9cKVU2akoCqYJmo6k/xkmAJOr/TMmOJ4geo+ix3srms8kaqGX3lwMMzlEMq5qud8uj11sMtV7X4RZO+ivm2uFPVRbG6LeN8mYMXHf96RfL8ix0jqgk7Yyz5tsSwMzZaasAetkcxCSVPToNMHvVGxywCm35Ga3e8RVFx1OHhJ5zsoZ+IiEmtSE3fBx1N6cTWCyRxH3gHXjWJK3mBisY/0s/x3p26+e7k0w27zyh6Sxd0FIc1QUOKsF3DejLt2nEUmM9T7tZweM1hJ7+XKWL8DxprFqFLzrLFG4HjV/UQqsXuZYeu99fqunxv7o8uemOsp+A1V+X+XRQ6u9SbsmzVlwjAkNPhjANsQE75ctRVR/ox4W1VT/HYTRooA9GB/IXA6WiL79XpGarppcP74M/LF/LGouTZRib078I7tDfWwLTEgPqngecY8VxmXZP2L7nV+XUYTtvrWpKZRl1wkbv2+228gqevAWvimY2OTn9O2SMX8blYE4Xpspn6fRcCUa0kQz8CeuOX2YsRiTDFBFPLMGkhK06Kng+IupQu6hHilQmbwUlPZMDD8yOijSOFf50ykN7TuV8y1m2Fux98nRVpGidLvneQtEqrYAjMoeYg8wZSOkdQMGdWGjffdJURQjnLBxAtDRf2DCjVarjMbmOUzaNZTTM=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY4OTg4MDUyOTE2NywKICAicHJvZmlsZUlkIiA6ICI1ZWQ4OTJiN2UyZGU0ZjYyYjIyNmFjNjQwZDA0YmJiOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJmcm9zdGVkc3Vuc2hpbmUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRjZjQzMTJlNzUyNWNhZTAzOTUzODQwZThmOGVhYWIzY2E0NjcxNjU3ZWNkZjIxMTRhMDU3ZDQwMTI1NWY0YyIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(2.5, 70, -75.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }
    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;

        setDialogue(e.player(), "hello").thenAccept(key -> {
            e.player().sendMessage(Component.text("§eGeneral Security Support Article §b§lCLICK HERE")
                    .clickEvent(ClickEvent.openUrl("https://support.hypixel.net/hc/en-us/articles/360019538060-How-to-Keep-Your-Account-Secure-on-Hypixel")));
        });
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Downloading suspicious mods or visiting untrusted discord servers can put your account at risk. It is upto you to keep your account secure!",
                                "Here are some helpful support articles that will help you keep your account more secure and avoid losing valuable progress or items."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
