package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.GUIHubSelector;
import net.swofty.type.skyblockgeneric.entity.npc.NPCParameters;
import net.swofty.type.skyblockgeneric.entity.npc.SkyBlockNPC;
import SkyBlockPlayer;

public class NPCHubSelector extends SkyBlockNPC {

    public NPCHubSelector() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Hub Selector", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "yGPTZDewV5jFB/WRbL58bu6WjeRJ9cEm6mR0iAr3F3xNO0L+n17+l5jNUYPdEZyuQDAq+7WdcbXwg5BxGv7pwpKSvlkhG7jQnbAzp0vjiB50/d1fMSIuZyYiGrMgnYwelyb/Pf+MbvBXicft53lecOVoP0ba1W4HsxKI1ItVaZvraN+cgUviWSuztDOQQcdkfpL1OOfTdSxoFFXhMpPbSxCyfKx1UovPNCeH1+UZDNrtZbYYUyUJ9mJUhX789V/XTcuCGx0MjQ/xLsxca6nm/xmY33xWjRZiETFLT+dfv8jYzUi8f5FIHHtK0cGJHLQy+ljwnE12T73aNpYR5LBxF6PDYeWG4DM2nssgA1FYwppAkmlJULzzFYI/IVRd8kfjRYDuCGg81Eg1L7Nzbjh/GNlPziHX2YXbD0fOgPaq38TT2/fFjfanIqCakAsiFBtGBqiD5aGQfpxIHSvnrq/J3pgskp0E5IZaJkU2UDsUGFh4mqS+cZoGhZokAOA3ivjU/5veKeG1EUXBlCHOHTPseWEYsSnezfIIqXnHcTJenoub1exiztVxUus/fUMajRpRPTF4qTvTpkG3Ora/kbJI73NZqiinJzFXrcGWoibcsyo07JtGPkGkBsp9z9ZiYyIhZ2Oft7xHlf84flKty3Pkjvxd2MxoYV02cmLUK+bYvYU=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY4MTkxMjM0MzUzNywKICAicHJvZmlsZUlkIiA6ICIwZDZjODU0ODA3ZGQ0NWZkYmMxZDEyMzY2OGY1ZWQwZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJXcWxmZnhJcmt0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUzMTRkMjc4NDRlYWRlYTU1MDM1ZTM1MDU3NWExOWVhMGU0NGMxNTVlMDhiZGNhYTEwOTFlYTExNDNiNmVkODMiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-10, 70, -67, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        new GUIHubSelector().open(e.player());
    }
}
