package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUIShopMadRedstoneEngineer;
import net.swofty.type.hub.gui.GUIShopMineMerchant;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMineMerchant extends HypixelNPC {
    public NPCMineMerchant() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Mine Merchant", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "Di7T42dhAeGgxrj/q2tvlPFv1sOcZa/x6YMxoI/MICARNalC/4tQ/hihKYU3+4/1SYASbIwD3KoXng2bo70PDvjB9geRkYBxr4OeT/Bm3yzx7ABhpgl/Dkuuvy7Gb9n7xPz2xy5hLT4c3YA4Zmgkt59myg2v+91CTCFUnesFl+n8fIJbFtWudCf6sFjZJqA6+zi3WRr8SCyRL1bPPk3B/KOy/ZlD0Ay6edqRqdXLDxRYVctWG0wqtJyzchSgrd/gQWdp1lgTfVFqdn8+QJY5Rt9WAPSR+RGzpcwsSWiD/uy32bMkcoyHOERkibbMs/coEFivWDjfR8uy8LNwIzivdDQKkiLm7/jrkvsbR42fMkh/H4ygqRsg6THH6DArhHmz6uTTGJUSB1jJ81pi16/oU2eBagDvyiU87TJDAyzUwweLnty7LfsP8wGjqbxOXu+ZwRzfOaIud606lQ5hn0qkCKxd4rnxlrpYOqiYkD1t4ITz75icNk6KaDbStY0/Jv3084fih4BZ1AmLy1HUXQA0ViDuRPt2uN79SrZIz225ditlot+P/k8MNChkc3MmyqCR3k68tm9DT7/b0LAXBGYuNt0XipLUhRC9UQp0A4k+cIuRJHQmNEmKKiCmmwncipUWn34Cwa0mj6/7NPZ1lI1Ym8E/c2sgodmvAuJV+iAdG7Q=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1MjkyNTEzMzYxNDQsInByb2ZpbGVJZCI6ImMxZWQ5N2Q0ZDE2NzQyYzI5OGI1ODFiZmRiODhhMjFmIiwicHJvZmlsZU5hbWUiOiJ5b2xvX21hdGlzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MDg5OTA2NTY0OTJkNzIyYThlYTg5NmI4MTZkMDE5MTEzNjhiMzg3YTJhYzRiYzc0ZjcwYTBhZWRkN2ViN2Y4In19fQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-9, 68, -125, 65, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_MINE_MERCHANT);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_MINE_MERCHANT, true);
            });
            return;
        }

        player.openView(new GUIShopMineMerchant());
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "My specialities are ores, stone, and mining equipment.",
                                "Click me again to open the Miner Shop!"
                        }).build(),
        };
    }
}
