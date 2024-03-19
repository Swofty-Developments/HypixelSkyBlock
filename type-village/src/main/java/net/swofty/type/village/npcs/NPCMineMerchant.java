package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.village.gui.GUIShopMineMerchant;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCMineMerchant extends SkyBlockNPC {
    public NPCMineMerchant() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Mine Merchant", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "Di7T42dhAeGgxrj/q2tvlPFv1sOcZa/x6YMxoI/MICARNalC/4tQ/hihKYU3+4/1SYASbIwD3KoXng2bo70PDvjB9geRkYBxr4OeT/Bm3yzx7ABhpgl/Dkuuvy7Gb9n7xPz2xy5hLT4c3YA4Zmgkt59myg2v+91CTCFUnesFl+n8fIJbFtWudCf6sFjZJqA6+zi3WRr8SCyRL1bPPk3B/KOy/ZlD0Ay6edqRqdXLDxRYVctWG0wqtJyzchSgrd/gQWdp1lgTfVFqdn8+QJY5Rt9WAPSR+RGzpcwsSWiD/uy32bMkcoyHOERkibbMs/coEFivWDjfR8uy8LNwIzivdDQKkiLm7/jrkvsbR42fMkh/H4ygqRsg6THH6DArhHmz6uTTGJUSB1jJ81pi16/oU2eBagDvyiU87TJDAyzUwweLnty7LfsP8wGjqbxOXu+ZwRzfOaIud606lQ5hn0qkCKxd4rnxlrpYOqiYkD1t4ITz75icNk6KaDbStY0/Jv3084fih4BZ1AmLy1HUXQA0ViDuRPt2uN79SrZIz225ditlot+P/k8MNChkc3MmyqCR3k68tm9DT7/b0LAXBGYuNt0XipLUhRC9UQp0A4k+cIuRJHQmNEmKKiCmmwncipUWn34Cwa0mj6/7NPZ1lI1Ym8E/c2sgodmvAuJV+iAdG7Q=";
            }

            @Override
            public String texture() {
                return "eyJ0aW1lc3RhbXAiOjE1MjkyNTEzMzYxNDQsInByb2ZpbGVJZCI6ImMxZWQ5N2Q0ZDE2NzQyYzI5OGI1ODFiZmRiODhhMjFmIiwicHJvZmlsZU5hbWUiOiJ5b2xvX21hdGlzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MDg5OTA2NTY0OTJkNzIyYThlYTg5NmI4MTZkMDE5MTEzNjhiMzg3YTJhYzRiYzc0ZjcwYTBhZWRkN2ViN2Y4In19fQ==";
            }

            @Override
            public Pos position() {
                return new Pos(-8.5, 68, -125.5, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        new GUIShopMineMerchant().open(e.player());
    }
}
