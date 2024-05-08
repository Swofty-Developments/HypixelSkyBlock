package net.swofty.type.thefarmingislands.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCTalbot extends SkyBlockNPC { //only there if Finnegan is mayor
    public NPCTalbot() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Talbot", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "SOiez71CTMEiRGjZhU+BJlkFak14KBAdG22yCgckBL/Xmwh2s2vQ/gju/HawBDSUqIVNpEqUkibJiD7Ci2tGsic1A0lQ3k2nA6qyCxgStNOGEtbwVPb4dAT8D6Ibw76JPSWboznVY4zlyW8e7h+MHYirwrW69sBVSDLBzf7P8UjfFgYHWCPsggXqSsr1eNbaklftRm+nEh38Cl6k2flxgMfBRuXNBtmwp++IPurO3vmpDJXUEXAic36EAxCQi2p6tmdDBR6wus6/yu3PHckvHkL0YnTPikFYH0yjhcad2/FJTS6oRkr4OFx8LKo+Ljx0ncQhSdTzuAnf7j4QpnAlTjTYZWPGxecSn1TcGzFsaLJT8oPUIod+6f2G+M4ycOCf4DGSS/TVipt40audnaDNZC45e6dRoL9WRgrOCZ22Ip3PNpQ41/+WLWWnd95HLup6x6RUVokcF/aqvS8R4dXL5vkl7wCYOVUdCLa/G+1QlbS+3qpTxGV0yuPQezlG8YeMGI8DXxND5RijEcESvb/RptGoWyUWeoiwLlOuBCrQhb41fCxZ163QKkvKZiKelW2MnbEKVPHwY8td2nPRjjBrQhKBloHSo8uZ4CirPdGFwyZuK+N3e5/WIMdnw8Fo3GGxdUHKyNGMDesqtrm8PiMC/6geV3GhJpfd31jP/dZS21A=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYyMTI5MDI4OTIwNywKICAicHJvZmlsZUlkIiA6ICIyNDBiYThlMzNlN2M0YzE0ODhiNzJmYmU1Njg2ZjhlNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJQbGF4Q3JhZnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVlNzhlNGI4ZTk4MGZiODY4YzdkOGJjNWI4YTlhZmQ1MjJlNjVhZmRlNWQ0NWRlMGRkNzU1MDhlYTVkOTE3OGQiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(285, 104, -549, 0, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
