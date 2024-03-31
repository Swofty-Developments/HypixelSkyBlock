package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCLumberJack extends SkyBlockNPC {

    public NPCLumberJack() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Lumber Jack", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "kyxxya3FqBJo6onG3bC1BjsjrF4uWTDd7Qitz14iNFwvZOn6FsW1A7dkNiZmiR1nVBfex7b4XHYAb6f3jXx8wIEvoDzSjzVLkCkzbQ9aMTTlDSAvkZ/fqtgMJbbXnppUSETmbWm7fdPjr4P5J30+Mz5vb66kNYu8QXsWqQ36YxI6sS2P77+vLdq+n1l389Npw1uMBpMLPjXaTsjAMrq1U6bDbTj7YwYqtURh0hxJm6v3q9d+oXD989fvYu04DYtEiW6H3VDjtvoEAb+m3H9tlDt74SNVXlIJ0lGa6RNpidBhKgSS38F0P5nMo1XbHJ/FcrWP+UZ6D4rT5TuW0T1J7n+5q+/LMMOR2hofFHgdqmTD85tTOmTmKKtIBPW9yreEKNZg7Ah/s3jStnVosp6A9qkpTcmdneRJwL+ZvHcpCbZpJq9Ii/NV+cBNrL49ylVCDZnRN4I7xENYfAq/Xe241cs2bEErrpu9uDH7dXSnhwQn4PdtMN6ZZrr4IJS2sjAyPjcuN/A006gs3Cu+9Cb4MViRCdIgOZDsIV/C0yDqr3+/SgKa+GRp7qIiphIMgRXC03GDSd/btYea1g8qJhhnkAL5MStubm/rdPaEf8wBc3y8Kc4pdEemo5kHQ8SPGvrk+xI8sGWKmlf/bZkyXKw9Wdhg/npeptxjNb2rUuaIDvk=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MTQ2MzM5MzU5MywKICAicHJvZmlsZUlkIiA6ICJmMjljOTIyMmVjNmY0NjExOTc3YWNkMmFjYzExNDAxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJSVJleWRlbklJIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NkMGFiZGQ5ZjZlZTdkODM3MGQyNjIxMDljYjg1M2JhY2QzZjQzZjhjZTQyNDExNjRkMDhlM2QyYTU2ODNlMzIiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position() {
                return new Pos(-112.5, 74, -36.5, -90, 0);
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
