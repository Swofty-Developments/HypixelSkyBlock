package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMinikloon extends HypixelNPC {

    public NPCMinikloon() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§cMinikloon", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "b1LoQqiHDCmpZDZD1WnNeK93IT5Svy2Zz1qZHP06mLo+yqe7452jTwRgPQue2Rb2E8nc+bvAIJKhmXx9JCcSNuoCL3NNmI5ujj5mfYfDF25AZo6ShwTk+O6yIUXuumAruOgGoxJnXNvrgXYCEU3AhdcNDoUsx01zcVnC3keZdUIA6gDV0Och8clNtQtO2pRHZDXmWSOHAeRETN+Jd++7oITYkLJCp4MXqw0dpbLMP2YMq4KsfQoM6QSZg1gHyizEPVD1q57jn6wfwyidN8N8oaOvTZ+dRoXP2TkrRtx9FfDnuVlOMu14gUx9FRhIY2biEA8pb4cPis9odRk0yUx6rIgySeZuiaV3HZh2OGAd1FhIqg+GijqbyO5u8NcPbTZAQ99eeqqNGVMRLbJW/IYl2iNWj6mtN4LXgforTo2fc/rNwgigve2PnO1avGnymPfq516fQRDrl6AyDI+QIa2lbKWNNiWMXqpl/ad/PnFKpbYh6Mmpu5lOVwYKScb1EpLEve/TFVpG/y/wNlWQqdVIJFnKn8XCmlMvz1jobuj9Adwxn1C60AGL++BFgfsv+hlcmVk7igJ1MHnP3Qujn+rKfQzER37Qi14iZNqEWNH142fTG8raJ0bjzo5cP7n6KEzJDGnQcsAdsZnr8+pbJwPSDygaL19Mwzq1LT95m3OwLrA=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjU4Mjk3MDEwOCwKICAicHJvZmlsZUlkIiA6ICIyMDkzNGVmOTQ4OGM0NjUxODBhNzhmODYxNTg2YjRjZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5pa2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJiZjk1ZDIyZTE1MmQ4YTNhMDQ3NjQ0NGM0MTE0MDQ0OTAzYjkzN2E2NzlhMDBiMTJiMmYxNzRhMTA3NTQ5OSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(83.5, 72, -117.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Don't forget to report bugs on discord.gg/atlasmc!",
                                "If you find an exploit, you can even report it directly to a staff member!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
