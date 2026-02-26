package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCFisherwomanEnid extends HypixelNPC {

    public NPCFisherwomanEnid() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Fisherwoman Enid", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "I+sHx+MZakXIPUh9jbCMzbknhSJScGyDnbjKVxBUqss4/uvOyy+O0lqa3xOLKMPScFtOguogAc61eQyESNZYbtFE5oRHmNmBntPe09FQLJydxeC2Kre/V8s9oxHQ8+aSa4a6UfnhsSEbM5Xe9gEipy+SJZNuEtm1LEK/Nkwcxqn3MzXLPLUcYKGOs8bmH+umqM9c/CILh/paKOBZw0hkzz9bpx3JENPF0NEdRopAqdxE3EzNDg/o90jpbqKjYXSE0NXt1PPmJjC1tn/lqWkU+n+rIAI975X5ViE5cHNOkn+CecweVawZ9eq3hgVMNV9y5jN1utN38nmNUijZcz46rvsB27JjfsCk5tgFDcXjbFB3aMgqmzYwpC8/UALCiAi/GVWPyig1odm+8fvPPPmVD/yEesIcEL4SSRDxWv2CJocm/mQfU4cQ86AcCKPbKaxy5DRJrpm6d9x7ewhVgIwKgrVMR3SGNTlfTdm2UZTtyZZmDhbOAjvGA0b2GNpi47ahdBGqIvHKjpx02CUF21V9MND1zpo2DoAirnNHz5moUp3YpeV4p70vXB0RfwlZq3Caqwe8O7gxvPVYdR4ucg5SG6vHZ6FVMpRD3aqFwTdSa92JufJi7PL8ZDX1Ew9DQFNKL5N1ttp6Z33i7XqcD/lCLvTIBN9xESuTjousXJfD79w=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTc0MTEwMTMwMzExNSwKICAicHJvZmlsZUlkIiA6ICIzN2JhNjRkYzkxOTg0OGI4YjZhNDdiYTg0ZDgwNDM3MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb3lLb3NhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IzNWNiMzhiODZjNGViYjYzN2ZjMDJkMzdmNzNkZmJmZWIxN2Y0NjI2N2Q4MTliMzgyOGFkZDQ2OWQ5M2UzOWEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(41.500, 70.000, -22.250, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {

    }
}
