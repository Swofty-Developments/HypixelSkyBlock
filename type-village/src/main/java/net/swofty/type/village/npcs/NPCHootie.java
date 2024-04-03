package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCHootie extends SkyBlockNPC { //not a real npc but a speaking skull

    public NPCHootie() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Hootie", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "Hf4t/1VNSZel9vmpdMT6KwgHJys/wdWAJ+0gCefkSPMm/ITnnhEuv313EMVCTHqOJa320d28uokesyi/MV2Zk56jyIFA54pj/wC3n0JQCi1+AD3E2Sb9YhjzrVOMpbtizcjQ1V0+zRnHqmHPe0rCmulnTCAusezZsJ9DA/a/uZwsXeXwVE5UPWbkcf/aOXbxTtNdyN5YNezKF8vlWbhAZbpvkzVvEwJzPM4NwCCsfl0JPNHCf5nphcMLHkKWGsxXCEG4kSGpj1kGWCOyzlWueTBivvo6y8N5GKAOatFL1l2MdRqX0ci7a9Fxd3EvcZ2yzMxY2aMyVwqqt7TEEN/Si9s6SC+64D0btEsa/coxhpP8xS1TUEpD5Ma9lcZvmLZFNAVeHJPivEGUYNLKhx4Ra2ABJue3WNJeJUmMd8H8y+dvfNNX7bFYvtBdffS+hKHntQg2Lwxrumz6BreE8H3rlIOQh3dz/8ry5JS9iTemlhaBVrrp5GzQoEDCx3fNia4y2ylcB8x+yB6h6mnl7DcriivvD65nAkBfFlxmqTPEQrkT0j8SJJ0/dvomi8maOTi/coBsuJXeFaD4zhq5+XarV+u0NZS+FbOgL9itUUTQSrnrINILninqqRFIWv5Z5lGxiQKpYe6rrCErgxjFRXKykR+sAhQEYLiEYTPQW38XK2c=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzNjY4MTA1MTY4MywKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYTMyMTZkYTU0ZTczNjhmYjQwYjcyMTIzOWFkOTVlMDdlZjRmOTdkOTNmMWM0MmZmMzE5YmFiOWE1Mzg4MmFmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(38.5, 70, -90.5, 90, 0);
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
