package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIYourMuseum;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCCurator extends NPCDialogue {

    public NPCCurator() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§fCurator", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "JQaG6dLgLgiEl6c/86bGPlXmXe8Lr2/NV1hPJOVGCHh037TQlcl9OyqOwhn2ofL4ITLS/HTnaX6GiIbDxVaeYPzD+M5hZDTZtgP10QmKLvQirhnRZ84YvyP+eOm7XB5BZj6IfE4yGLAO7ag85zjvUDpOivrna6KPrCMBodWsyafJ4dk8GLf3okyaVTV9y8O7/BwG8R2x2HSZ5nJ9/sFYYynRnx6tDpTN20OLdyZ9wlWOrahoFEQotgfMVhHNm85Ds2EU/Iyo8ngjPgl7shVtRqonD4qDNbS/i9ICNFzKTDqWGW82Wjmi0b5Hd7AG6Pzmlc0lXBwG5wIB53xJjHX5zpucj8rYdTQdV3F+a4uYyWVw0KLmxpapFTVtWhWdOUzK1Le8dKW8dvi8MnP4m0hn30Iad9BqMmg/ko1aGZhkvUv4IhjWSaYvSWMx16rJj8CZZzhjdLCRgyb1XPAZaHr26gZzgSiHnivb97gQIH4ML3MOjtbCGaLTirADSUovALDrlHB8RofFG3mQVzoTF7LSJIXKgpkRi000AJIabrBkW36SiJySTPpaEG/Flme/886bjtU0A31S2vjM19CjgXsXlvPRRbmS94pyLfoLYc23VlmZoaqrAdFDdMkXQYZ354VQeYJ0o82LCPNRoreLiZPa7AQtPrhh+tQVlz4olVqJB10=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzNTE2OTQwMTIwOSwKICAicHJvZmlsZUlkIiA6ICIxNzhmMTJkYWMzNTQ0ZjRhYjExNzkyZDc1MDkzY2JmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzaWxlbnRkZXRydWN0aW9uIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZmMjJiMDM0ZDM3MjFkMWZhZDcwODI4ZTM3NTkyNWJhNDFiNWY5NTI4NzNjZDlmYzZkYmJlMWU4MGJlN2JlYjUiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-62.5, 77, 84.5, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        SkyBlockPlayer player = e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_CURATOR);

        if (!hasSpokenBefore) {
            setDialogue(player, "initial-hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_CURATOR, true);
            });
            return;
        }

        new GUIYourMuseum().open(player);
    }

    @Override
    public DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Welcome, " + player.getShortenedDisplayName() + "§f!",
                                "After years of work, I finally opened the §9Museum§f!",
                                "This world has an incredible amount of interesting gear and rare items, which I would like to showcase.",
                                "Would you like to help me fill the Museum? My assistant, §6Madame Goldsworth§f, will even reward you!",
                                "I'll show you the list of items that I'm currently looking for.",
                                "You can donate any item from the list, and you'll still be able to use those items whenever you want!",
                                "However - once you donate an item, it will be §dCo-op Soulbound §fto you, meaning that you won't be able to trade, auction or sell the item anymore.",
                                "Choose carefully!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
