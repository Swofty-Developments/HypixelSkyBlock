package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.bazaar.BazaarCategories;
import net.swofty.type.skyblockgeneric.gui.inventories.bazaar.GUIBazaar;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCBazaarAgent extends HypixelNPC {
    public NPCBazaarAgent() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§6Auction Agent", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "uBZtSbevRudSrX4uG3hxrRKGqULSiiAW80AJg7RfrK8/I7aJnDMTZ7z4Sz7ck2i2dugnBL3CTs0RyK3QGRVC20yx4YijE50AenwGw65pNHed2l54eThuC01xcPsPM1LQ/qBxldHYy+iLMSDMxf4gYszD50fWLGU2H1tfi9CWQFoea3SngFpUvozICfbCe7ZzlhybE15XmlkAUFR293tYTP6CxA8hIjuisNnW5LE1m1EHNH8K0vR87T4OrMJ4tXKFRlN362v6gUkXC3jPEzAzVAln3oBfUeSvgYne0nyYxFZjTg3+xiP4fd3ULzDIouEBer0VUOB1CIPeoGxK72UviDuXSdOoQonQOdadn4nj2i7MsrIrVzYYz1sWTLUsdfNilZb8rH5T/Z4MvKmktxhe0eeN19NnEWdEwgkZSqi7ivoACWEdFRqr62d3Bkf9kqk/j/cz9Z20VBr57THOFRgSNIznA7eV7Mhdao/G3QGPmbtNCWjhmDQ+UJ7ADbIN1M+p/h7vi/z9nYBpb/ei9U7xPfVKslyY2hgv+eH4dYO3whwWhfGhsjgbOA1Ibp9dln9t612adJh2XSlaJH7AFr2HS40tG6HOWhwxghJl+qScVdoyJ/fm4bfubZYrR3E8QDvQtYqAEuWmLFOiXtOg3SJgi/BfjjXHGfV3AydH0PFAI3A=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5NzMwNTM5MDExMSwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg2MTQyNDFiOTgwMzE5YzAyZjVlZTNhZTFhN2ZjN2ViZjhiM2ZkZDUzMDFlZDNkNGUyMTU5YTgwZGFlMWQyYyIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-33.5, 73, -17.5, 0, 0);
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
        if (isInDialogue(player)) return;
        SkyBlockLevelRequirement lvl = player.getSkyBlockExperience().getLevel();
        if (lvl.asInt() >= 7 || player.getRank().isEqualOrHigherThan(Rank.STAFF)) {
            player.getLogHandler().debug("As a staff member, you have bypassed the bazaar requirement.");
            new GUIBazaar(BazaarCategories.FARMING).open(player);
            return;
        }
        setDialogue(player, "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§cYou need SkyBlock Level 7 to access this feature!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
