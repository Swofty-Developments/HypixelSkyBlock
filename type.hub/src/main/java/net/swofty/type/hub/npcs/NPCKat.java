package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.kat.GUIKat;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointKat;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.KatComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCKat extends HypixelNPC {

    public NPCKat() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§bKat", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "aVn4ur6tr41EfzuJEo8VT/CkTVxcxlGTOWC40krGm1UbHGiszFipXqZzHGBP5hshZ4dQ+E6e9wg0YGMTeL6VL5klapc7P690pAoF0fTPMoXlaMgnZ8xGQFLLAIXMCp7Xyy7y+TulNdpNlrNM6o82Pu2ibKr6DSK7AS06WdO+cBmnUMu8KVDlGSZP7w8cCQNyY6UEm+I+Q1IypaKApU1yqrJ+S7yFZnPboKQXtYLybxNYSeHFkA9H0onT5UGXbC08crxO1L62eYnx+2Fe4G82URbDUMTDPxnziaLjB3LSK3OYwYN0XdWyF7P7+kLP2LaLO62rjdxTi0tkdJPiXLgJEYW25G6ORzCsuXAFp0VoUi0IY1VVtxDecKHs2llRY4Drw44JIRskyxfnUxI4Tx9NH8ILIHiKNUIvkp6qkTfH/ji9IZH8extCV7Road88pqF/3u/ts5iCpN5UxIDURC26PvTOx+Rzf3uRKP90EZEd2BtC/ucpagvI3ZEJwD0GQGaIb+2ncPqlqB/Uy9uXVXZ33xi4t/Ow7Cfs9ptI7xjpKyi2Jb6S33aOlDIxkJVS96H37nbkIpvoTMWyur7YodSP8ICvlDI3EB1y+pDAHB/lJGKOkCSlW5QdJCnKYYixoYao/JJL3FOMBz54fSTdwujP6gA5GNON9ynS7xLcLuLiVXA=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDc2MTAxNTIyMSwKICAicHJvZmlsZUlkIiA6ICJlYWUwODM5NGFiMDM0NmVlOTI0ZThmMTU3OTA3OGJiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCdW5ueWhvcCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYWFlODEyNzc1NzA2YjU2NTk2NjgzNDg2YWJlYWY4NTdlYTFjMDY4Y2IzNGE4YmMxZGU5YTU3YzYzOGNmNDExIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(31.5, 71, -101.5, 60, 0);
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
        DatapointKat.PlayerKat katData = player.getKatData();
        SkyBlockItem heldItem = new SkyBlockItem(player.getItemInMainHand());
        if (heldItem.hasComponent(KatComponent.class) && katData.getFinishTime() != 0 && katData.getFinishTime() > System.currentTimeMillis()) {
            long reducedTime = (long) heldItem.getComponent(KatComponent.class).getReducedDays() * 86400000;
            katData.setFinishedTime(katData.getFinishTime() - reducedTime);
            player.setItemInMainHand(ItemStack.AIR);
        } else if (katData.getPet() != null) {
            if (katData.getFinishTime() > System.currentTimeMillis()) {
                player.sendMessage("Kat didn't upgrade ur pet yet");
            } else {
                SkyBlockItem pet = katData.getPet();
                pet.getAttributeHandler().getRarity().upgrade();
                player.addAndUpdateItem(pet);
                katData.setKatMap(0L, null);
            }
        } else {
            new GUIKat().open(player);
        }
    }
}
