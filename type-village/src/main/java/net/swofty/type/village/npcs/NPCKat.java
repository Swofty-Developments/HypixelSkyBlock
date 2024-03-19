package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCKat extends SkyBlockNPC {

    public NPCKat() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Kat", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "aVn4ur6tr41EfzuJEo8VT/CkTVxcxlGTOWC40krGm1UbHGiszFipXqZzHGBP5hshZ4dQ+E6e9wg0YGMTeL6VL5klapc7P690pAoF0fTPMoXlaMgnZ8xGQFLLAIXMCp7Xyy7y+TulNdpNlrNM6o82Pu2ibKr6DSK7AS06WdO+cBmnUMu8KVDlGSZP7w8cCQNyY6UEm+I+Q1IypaKApU1yqrJ+S7yFZnPboKQXtYLybxNYSeHFkA9H0onT5UGXbC08crxO1L62eYnx+2Fe4G82URbDUMTDPxnziaLjB3LSK3OYwYN0XdWyF7P7+kLP2LaLO62rjdxTi0tkdJPiXLgJEYW25G6ORzCsuXAFp0VoUi0IY1VVtxDecKHs2llRY4Drw44JIRskyxfnUxI4Tx9NH8ILIHiKNUIvkp6qkTfH/ji9IZH8extCV7Road88pqF/3u/ts5iCpN5UxIDURC26PvTOx+Rzf3uRKP90EZEd2BtC/ucpagvI3ZEJwD0GQGaIb+2ncPqlqB/Uy9uXVXZ33xi4t/Ow7Cfs9ptI7xjpKyi2Jb6S33aOlDIxkJVS96H37nbkIpvoTMWyur7YodSP8ICvlDI3EB1y+pDAHB/lJGKOkCSlW5QdJCnKYYixoYao/JJL3FOMBz54fSTdwujP6gA5GNON9ynS7xLcLuLiVXA=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDc2MTAxNTIyMSwKICAicHJvZmlsZUlkIiA6ICJlYWUwODM5NGFiMDM0NmVlOTI0ZThmMTU3OTA3OGJiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCdW5ueWhvcCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYWFlODEyNzc1NzA2YjU2NTk2NjgzNDg2YWJlYWY4NTdlYTFjMDY4Y2IzNGE4YmMxZGU5YTU3YzYzOGNmNDExIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(31, 70, -102, 90, 0);
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
