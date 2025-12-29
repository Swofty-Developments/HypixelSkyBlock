package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUISeymour;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCSeymour extends HypixelNPC {

    public NPCSeymour() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Seymour", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "hGGbYl4OuItrGiGXYYh6mCy1a5xknDipoYay1DFARt03+YJ6ebFagIMH7JgEY3CFed8WIxFcNivfpZ3q47e2KhNugR+6/X4KBrHtyz9fWVte2HGW7p2ShiYipcUL+8wBjvJ2ssEsWTUeGgqnBgo/sA3UdsWhB6E9iP34x4nm5lPfnKT2Jl9QyhsqXSOQYmidDUY5z0kGhmy0IXRoh92vF/lrzmdpS4TamfogRLGV1BivxZ8C71ImVczEm/JHWTGCdjwFBTdoZzkUOJ9IE+tbBUlOWWFMvjW+TY4Y3pBM5lzY3TMTpvG+rHZ0042E2SNfp2RmHaEAqMNb9JI57qfXKZ8zJB1/8gU+pFjuuXRsWuV0tWKLIUGSH3nIho/BidPBoe6YUsWCe9ySSrFprocKU96Ct6z5l8bsoJ5xtiOGSn/5JdUexc4IUF9ICFh7Xeu8rvGufH7s1BIyLgUBQQSvVpj31VharFkV0IVnwG/4c/YBYaaUUH07CW0woj577fd5nCVEs8pfJ7KNrChtna0LzDZQuELzDwmQO5mdOxWEwGurPvPx1uFm3tCDVBRUrj+CCVCQqIflg9s3nVTRSPZhl3ZlNW+L8/wVdjuXtGTXGWT9ou+nfGRT8c0ENrsVE3dkWe4o2BaokIIdCJ1isO+GS6oMNP6I07EGgxUZFe2kk8A=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5OTQ2MDQ5OTc1NCwKICAicHJvZmlsZUlkIiA6ICI3NTE0NDQ4MTkxZTY0NTQ2OGM5NzM5YTZlMzk1N2JlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGFua3NNb2phbmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhhYWM0ZTYzMTM5OGMwZjQ3ZmU4NTYxNWU3ZjQ5MjE1NTAwMDQwYmI5MDgxOWQ1NjhlODI0YWNmOTRiZmE0ZSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(25, 65, -41, 112, 0);
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
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_SEYMOUR);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_SEYMOUR, true);
            });
            return;
        }

        new GUISeymour().open(player);
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Looking to buy something fancy?",
                        }).build(),
        };
    }
}
