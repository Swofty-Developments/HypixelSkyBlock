package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCMadameEleanorQGoldsworthIII extends SkyBlockNPC {

    public NPCMadameEleanorQGoldsworthIII() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Madame Eleanor Q. Goldsworth III", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "VnhMZ2a55oTztZzmxmmkoTQ8vmKUFyh1QUFBLHeVGB6AsaFeWhef+NLUtftoWAf64m78I+OkmIQcexSHtCLZm6xSGitEcx8i/TRx5X25ZsV+jHKd2jJQMMA/BFrnnfawvV6KZ7zqbc7m/5Uv4aOHZxcYt/EBJyNiDBpqhEGDX/Ulo34Ti87JTHp5lT4qG1pmND9FQw4T/1JNdJTI+Wlxw2Ux9k2tRloDAUtyb9YDwYjMn27Ua49eYtVv3tFawuibXFtIj0u/Ni88PF25zAU2kE+1i3dtmj6htQ/Nzgc8gaFB/cETHD997D4/DkpPPnCPL1sd8iO63ncma5aFxvaBwAh97bGIwNUrsJJy2AtYlluD3PHrIIukKCuN+v37+Tn8KM9AbYVVfpJ6Z1Xot+s3BClWuzo4+sAvfBER6QiOvCYuSBjlGxCagSEIaBkxc6YIFhVs5Wa1ijpPcebB+HROePr2lQNkRFjiA4QYDprZJLm6HeGpXAhHtKAEb/D965sYe1EY3zDSPB33ZdO/Yq0u5oq+jyzAIurmS/oHWbMJ8VPWF39jUzc7ykSosEUDwOt1N6bO67pOB7Axjkt/45zJiYgAiU9XOEAsfkeSQcHKeJve3yKKizs0j6nPXGsF9z+mYwVokYLFbisanum6GtXHmhCNNJEkHbX6cgOid5V0I4U=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzNTE2OTIwNTU3NywKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I3ZTBmYzhkZjkwNjdkMzkzYzk3N2YxZjM0MDI1NjM0MjIzZGIxNDVlOTQwM2RjMzJjMzExODg5ZTMzNjU5NjUiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position() {
                return new Pos(-50, 77, 76, 90, 0);
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
