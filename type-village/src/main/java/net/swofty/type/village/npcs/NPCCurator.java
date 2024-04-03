package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCCurator extends SkyBlockNPC {

    public NPCCurator() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Curator", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "JQaG6dLgLgiEl6c/86bGPlXmXe8Lr2/NV1hPJOVGCHh037TQlcl9OyqOwhn2ofL4ITLS/HTnaX6GiIbDxVaeYPzD+M5hZDTZtgP10QmKLvQirhnRZ84YvyP+eOm7XB5BZj6IfE4yGLAO7ag85zjvUDpOivrna6KPrCMBodWsyafJ4dk8GLf3okyaVTV9y8O7/BwG8R2x2HSZ5nJ9/sFYYynRnx6tDpTN20OLdyZ9wlWOrahoFEQotgfMVhHNm85Ds2EU/Iyo8ngjPgl7shVtRqonD4qDNbS/i9ICNFzKTDqWGW82Wjmi0b5Hd7AG6Pzmlc0lXBwG5wIB53xJjHX5zpucj8rYdTQdV3F+a4uYyWVw0KLmxpapFTVtWhWdOUzK1Le8dKW8dvi8MnP4m0hn30Iad9BqMmg/ko1aGZhkvUv4IhjWSaYvSWMx16rJj8CZZzhjdLCRgyb1XPAZaHr26gZzgSiHnivb97gQIH4ML3MOjtbCGaLTirADSUovALDrlHB8RofFG3mQVzoTF7LSJIXKgpkRi000AJIabrBkW36SiJySTPpaEG/Flme/886bjtU0A31S2vjM19CjgXsXlvPRRbmS94pyLfoLYc23VlmZoaqrAdFDdMkXQYZ354VQeYJ0o82LCPNRoreLiZPa7AQtPrhh+tQVlz4olVqJB10=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzNTE2OTQwMTIwOSwKICAicHJvZmlsZUlkIiA6ICIxNzhmMTJkYWMzNTQ0ZjRhYjExNzkyZDc1MDkzY2JmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzaWxlbnRkZXRydWN0aW9uIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZmMjJiMDM0ZDM3MjFkMWZhZDcwODI4ZTM3NTkyNWJhNDFiNWY5NTI4NzNjZDlmYzZkYmJlMWU4MGJlN2JlYjUiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position() {
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
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it added quickly!");
    }

}
