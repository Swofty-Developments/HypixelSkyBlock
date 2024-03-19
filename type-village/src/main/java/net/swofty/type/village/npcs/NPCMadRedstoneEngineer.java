package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCMadRedstoneEngineer extends SkyBlockNPC {

    public NPCMadRedstoneEngineer() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Mad Redstone Engineer", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "jrqHDHo7IldEcPPiuAdTrdjTeMxOWaMjN8jPKu1MCvhLHEy1VRJNwb+rZC6QNxq4Tyd3PwfoP5VCs2qWAhA40+IOvM2xHBOIFj4fZRUpSyTouJ0qv9ZZDrF+vVQguQpmRXZaj2m2+1FHPSJ3xim9wjN+xqWQXlVqGUXpXNqa/V3RbBLOyDCpP/yIW35Md58L2HxBnObOcSbrgks0APPcaFlCADn/RjC/AwogW3yx6DqtHdktpvb+I5l1EpzAPnnWjKfAYwYKaxxcSsX6/xKiRTkatA0NrXik9Xzwwr7Bvqdm0Po+7mpOnEU6UAc9H2GYXVLicDMNPmd7LMBYCl9PBmAUsLgHRFVjC9U0enivwhZ2IUlWcdDjtX3pj9EzvN6Xy8ehQBERZZMR3858W6WLtbqkj2vTkmtezrgPUnHQ5g2jhhbd03F1OSJRY/8K4DI+lnKVokK+A8tVM6dux95EbiqcOt7r3kntwdOF8Wgy57KhxID/huydeNguDUdNm5LodZK8yvKGYqndWxyPQ1apDc9KKcgbowNelbKajyOZ1ben1Xn3ZJBOx+6QonSRIcf4fqqq+iIWmWYIuZaX1aedOMaoLard2C4novWWCY2H3PYjMmFNM5ekH65JtEHerHd/9RkHEnZX+6DjpUCrQvZQzGdoTdFS3PXAm1v8M8tKHPo=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMDc5MjI1OTA1NiwKICAicHJvZmlsZUlkIiA6ICI4NDIzMzM5MTYwMjY0NjBiOWQ1MGFjZWU3MThiMjkxNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmaXJlc2xpbmdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMDZhZmY3MGM1YmIwMzE3NTU0NjhkNTQ5Zjk2NzVjMTkxYzlhYmQxNGE1NmM0ZGVkMzVlMjUzOTBkYTk0MWRhIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position() {
                return new Pos(-52, 65, -29, 90, 0);
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
