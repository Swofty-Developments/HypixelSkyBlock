package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.GUIShopMadRedstoneEngineer;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCMadRedstoneEngineer extends NPCDialogue {

    public NPCMadRedstoneEngineer() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Mad Redstone Engineer", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "jrqHDHo7IldEcPPiuAdTrdjTeMxOWaMjN8jPKu1MCvhLHEy1VRJNwb+rZC6QNxq4Tyd3PwfoP5VCs2qWAhA40+IOvM2xHBOIFj4fZRUpSyTouJ0qv9ZZDrF+vVQguQpmRXZaj2m2+1FHPSJ3xim9wjN+xqWQXlVqGUXpXNqa/V3RbBLOyDCpP/yIW35Md58L2HxBnObOcSbrgks0APPcaFlCADn/RjC/AwogW3yx6DqtHdktpvb+I5l1EpzAPnnWjKfAYwYKaxxcSsX6/xKiRTkatA0NrXik9Xzwwr7Bvqdm0Po+7mpOnEU6UAc9H2GYXVLicDMNPmd7LMBYCl9PBmAUsLgHRFVjC9U0enivwhZ2IUlWcdDjtX3pj9EzvN6Xy8ehQBERZZMR3858W6WLtbqkj2vTkmtezrgPUnHQ5g2jhhbd03F1OSJRY/8K4DI+lnKVokK+A8tVM6dux95EbiqcOt7r3kntwdOF8Wgy57KhxID/huydeNguDUdNm5LodZK8yvKGYqndWxyPQ1apDc9KKcgbowNelbKajyOZ1ben1Xn3ZJBOx+6QonSRIcf4fqqq+iIWmWYIuZaX1aedOMaoLard2C4novWWCY2H3PYjMmFNM5ekH65JtEHerHd/9RkHEnZX+6DjpUCrQvZQzGdoTdFS3PXAm1v8M8tKHPo=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMDc5MjI1OTA1NiwKICAicHJvZmlsZUlkIiA6ICI4NDIzMzM5MTYwMjY0NjBiOWQ1MGFjZWU3MThiMjkxNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmaXJlc2xpbmdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMDZhZmY3MGM1YmIwMzE3NTU0NjhkNTQ5Zjk2NzVjMTkxYzlhYmQxNGE1NmM0ZGVkMzVlMjUzOTBkYTk0MWRhIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-52, 65, -29, 180, 0);
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
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_MAD_REDSTONE_ENGINEER);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_MAD_REDSTONE_ENGINEER, true);
            });
            return;
        }

        new GUIShopMadRedstoneEngineer().open(player);
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return new NPCDialogue.DialogueSet[] {
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Every problem in life can be solved with a little redstone."
                        }).build(),
        };
    }

}
