package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCScoop extends SkyBlockNPC {

    public NPCScoop() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Scoop", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "ajRNgiIyBcaggtRbNw2dzO5idCuNeEeidEBgWVJl+VqJWpmgr89LSmE4VFHwZDwnaTtWMgN2kX5LRY/3AoOXVzR4x5pXNp31eaM3i0WC9wG8bLzMU5VCrbdCPrDnlX6BRwdhmSAfeixqPWqlUrDBllolh52/Hg6HFk7zd4dO3uxrYpwUfb5EFdFFMdPwJwUwzKx/NRhPWn3YRu2dyMRigzXaPaiEGCTJCkgmp365NNGGclcbYDHK/b6cXvo9Ng+5PS9coebrSe5BQ8sYTbXw2+VzTQwlsXBlmVhylv+UD+C4PuX3g0AnnATKVJsTlei0H7oamKMUM11vU49r5DZth27WjHnfuVSRq5C7zM4kugPRc02P5Pox7IyxoGlRmm2fSDA6wm4LjbSQYVSOhrAF9XooS0hNSE819bBGu9ZHv9tmgzaQtp1tPB7vNOdlx0su//R7RhjVPelFDC+2+AMDPNyoZzjYBDS0mZKc8fVee3ZFnItALEG+GaOq99+ce3maiXLYpHpx1fZDrx4pDyZW66q3RkT59QoZk9qv1jPhgtbq5EmuhMRSTt8fdCJd6UWe/UhmgvPiUj+Ss9sFGa90upn5CgAgKtytpj+GzMVLfm7XVI+8KJSNeTBSvqJSVNKDBASnhOAAHBqRDDsHf+K7/b0773MgX9XiJsWYI4XBmXk=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYwNDc1NzkwNzQ0NSwKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hODk4YWQ1OGQzNjY0Y2ZhY2YxYmYxMWFhZDlmNTEzZDI1YThiOWJiOTFhNzczODJiMzJjMTBiN2QwYTRiYzliIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position() {
                return new Pos(5, 181, 25, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it done quickly!");
    }

}
