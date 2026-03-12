package net.swofty.type.garden.npc;

import net.swofty.type.garden.gui.GUIManageChips;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class NPCJeff extends AbstractGardenNpc {
    private static final String TEXTURE = "eyJ0aW1lc3RhbXAiOjE1NTA2Nzg1OTkwMDQsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M4Y2NkNGZkZjU4YjMwYWE4MzAxN2NmYTVmZWQ5NzcxOTZjMDI0YzhkMWEyNzYwMDRlOTA2OGU4ZWNiYjBiNzkifX19";
    private static final String SIGNATURE = "EzyAHb6TAVKVuO3R6cTt6eNJYXdU6C1fpPByuOEL/FUIIHqW5QpUnQLP7s3EjLhhzRagDi/eU/xGe09Ucsb7s6tSavn1jzfqwnmVG7C2FJ30ELl35y3pYbNKwmBl8I2fDY9pQrmfJbWRVhv9Gw8W4h8YRZARnW5PfVdsL1ddbTTsssaxapU8YTfUc88h2egnTD/bEHaqYEgfLBzjyMAyK9pDUIqe0NDmBJLbjPZXIVImRbMKanwgLRxmUkjGLONerb0HE8Kx6QoJEumoLOBrOLA5BJF7Jwghrv2d1W9S6hr89Ul6R8CnxQwHFfBMejccm0hLZein4DrKbiFHC8c/hs4jCoC4JT4rvOd/Yp8zNr3Y/dtUk5uTOguk/gYExI+p+1xc8HwTK3sK75LiFl+Ryu4LlKv5GBEznsnRHv1Ufeia3NeuVXDLi/W3zR8VG95Hf0lmKHdwJ/R9E56TxNYRh7wpma37ZTfEpUpKE1o7Z2m5c3jmDxLRdQg8dK1ZYMjlul36Qa8SXYTM4T+bdB1577M/44Vyde1NFVepYK0vRXDDNRal2LoDRM9buoTuN2taeP3pmt5C+pL554r7tWgOdCHUz51E9hwsOA9VCVxIA5eS+bgzSBLkWbXZYNo+zi/0bVr9OGdP2hCTJDsd0x7YEL2P7qribidVjnRLWWP8a2k=";

    public NPCJeff() {
        super("jeff", "Jeff", TEXTURE, SIGNATURE);
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        if (isInDialogue(player)) {
            return;
        }
        if (!hasSpoken(player)) {
            setDialogue(player, "hello").thenRun(() -> markSpoken(player));
            return;
        }
        player.openView(new GUIManageChips());
    }

    @Override
    protected DialogueSet[] dialogues(net.swofty.type.generic.user.HypixelPlayer player) {
        return new DialogueSet[]{
            DialogueSet.builder()
                .key("hello")
                .lines(new String[]{
                    "Garden Chips get stronger as you redeem more copies and spend Sowdust.",
                    "Use my menu to redeem chips and level them up."
                })
                .build()
        };
    }
}
