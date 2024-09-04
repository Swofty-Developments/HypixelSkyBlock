package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.swofty.type.hub.gui.kat.GUIKat;
import net.swofty.types.generic.data.datapoints.DatapointKat;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.KatItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCKat extends SkyBlockNPC {

    public NPCKat() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§bKat", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "aVn4ur6tr41EfzuJEo8VT/CkTVxcxlGTOWC40krGm1UbHGiszFipXqZzHGBP5hshZ4dQ+E6e9wg0YGMTeL6VL5klapc7P690pAoF0fTPMoXlaMgnZ8xGQFLLAIXMCp7Xyy7y+TulNdpNlrNM6o82Pu2ibKr6DSK7AS06WdO+cBmnUMu8KVDlGSZP7w8cCQNyY6UEm+I+Q1IypaKApU1yqrJ+S7yFZnPboKQXtYLybxNYSeHFkA9H0onT5UGXbC08crxO1L62eYnx+2Fe4G82URbDUMTDPxnziaLjB3LSK3OYwYN0XdWyF7P7+kLP2LaLO62rjdxTi0tkdJPiXLgJEYW25G6ORzCsuXAFp0VoUi0IY1VVtxDecKHs2llRY4Drw44JIRskyxfnUxI4Tx9NH8ILIHiKNUIvkp6qkTfH/ji9IZH8extCV7Road88pqF/3u/ts5iCpN5UxIDURC26PvTOx+Rzf3uRKP90EZEd2BtC/ucpagvI3ZEJwD0GQGaIb+2ncPqlqB/Uy9uXVXZ33xi4t/Ow7Cfs9ptI7xjpKyi2Jb6S33aOlDIxkJVS96H37nbkIpvoTMWyur7YodSP8ICvlDI3EB1y+pDAHB/lJGKOkCSlW5QdJCnKYYixoYao/JJL3FOMBz54fSTdwujP6gA5GNON9ynS7xLcLuLiVXA=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDc2MTAxNTIyMSwKICAicHJvZmlsZUlkIiA6ICJlYWUwODM5NGFiMDM0NmVlOTI0ZThmMTU3OTA3OGJiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCdW5ueWhvcCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYWFlODEyNzc1NzA2YjU2NTk2NjgzNDg2YWJlYWY4NTdlYTFjMDY4Y2IzNGE4YmMxZGU5YTU3YzYzOGNmNDExIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(31.5, 71, -101.5, 60, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        DatapointKat.PlayerKat katData = e.player().getKatData();
        SkyBlockItem heldItem = new SkyBlockItem(e.player().getItemInMainHand());
        if (heldItem.getGenericInstance() instanceof KatItem katItem && katData.getFinishTime() != 0 && katData.getFinishTime() > System.currentTimeMillis()) {
            Long reducedTime = katItem.reducedDays().longValue();
            katData.setFinishedTime(reducedTime);
            e.player().setItemInMainHand(ItemStack.AIR);
        } else if (katData.getPet() != null) {
            if (katData.getFinishTime() > System.currentTimeMillis()) {
                e.player().sendMessage("Kat didnt upgrade ur pet yet");
            } else {
                SkyBlockItem pet = katData.getPet();
                pet.getAttributeHandler().getRarity().upgrade();
                e.player().addAndUpdateItem(pet);
                katData.setKatMap(0L, null);
            }
        } else {
            new GUIKat().open(e.player());
        }
    }
}
