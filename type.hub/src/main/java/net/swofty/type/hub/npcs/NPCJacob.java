package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCJacob extends HypixelNPC {

    public NPCJacob() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Jacob", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "ER8CsHYuprQMm5HqK1Kj960TrzSWJDYikes0JORZ+llY/f5ArCuF11c7vtzngFVXRpGKFMnSezYDag+iyPoc64KTH0KQx9XuzLz04LH49UWl3diVbIxGvq5VHao1GieKE308TJOQt4sFaz+Dai8z1rWJdo0MPnHf7jsEy+/M/mMtXZv3dsexEvTzOFm6tF1RTt8f1Zq4LLpY2bWjp4V8pcx6DbYsn3qc274B3shIIEkBPUI1ySaueh2yw5Y8QXIzsVRknpjRfO7WBkvQpoCmTC2tNF6F3KlWCGH6NcrXeH2rxTIehDOCqiq9tL2boR+7t4vgqetPArVvU+66aeMBsRoaLjQOI76sztpxh0hY1ThSuhpgSpYmjF3TBW1TBgu4oVRvhyr4bQFVMCPIlNR0ruzm3DgjsBCAb3ILF+1PkhXBm0u5ytdcDCgDU8u4ZM8BOFKwI8s4arHZi2HjFEwn+dZGkoNoxCdkJ+LUEJjCl31yyU8X2CkocZe4/dtLxB4akcvhoyif0+QqjxkIGdPD6bXkIdmV52rCS8t84Toz65BcYxKWaiTafPGlEQkSqYn9w04qAwBu2k1X5id0NYypdTsTb53UfHN5xScQKxWtrCFOnE0Uo90+At7H5Kr4wWDPR0GgjYF+kSBmeju9WKi6YOdmgvTf2fbRQi+3vj67kAI=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzg1MDcxMDY0NiwKICAicHJvZmlsZUlkIiA6ICJlYjIwYWVkYTRiYTM0NzVmOTczZGNmZjkzNTE2OGZhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa3lGYWxsZWVlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdiOGJiMWI0OGY0YmFiYzY3Y2UzOTU0NzIwOGZkYmVkNzIyY2E1OThjZGYzMDY4MWUzNjdjNjI0N2NhYjE5MTgiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(23, 71, -69, 45, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }

}
