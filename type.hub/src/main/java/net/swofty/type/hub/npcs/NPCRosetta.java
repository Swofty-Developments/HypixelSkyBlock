package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.rosetta.GUIRosetta;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCRosetta extends HypixelNPC {

    public NPCRosetta() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§aStarter Gear", "§fRosetta", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "kSFsbn+nM9SpCrWQCBLIetm++NUHmeGM+ZwAqggEo2rRUHLIMUr1FLSb/Q9iz80F+uwq4g3D37+HprftaVD7RXsHZ5q3ABubkNMLw3x2QHoryc/XXYHA6pMPc0fnzQMYdeavQHB55VBicPhNTwJloMp7j/ujyUvSq0trgxJyJLuJt2ZN8iAVA5zg3W0s4PaQcSPo63OKGf8op50fKnzOcJFpplNbory1qTBSq5LRVV6Mu4MzkyRqtAYbMaecmUa/tA+To3zyOHcuJfnIGXCCDi1ud7dm5FOKrz73y8HIOcho2gOtfqtD/PoXtYr6wQI+xK0lpbWySXxVeNf23tahOSdn79PaAmYKXx+SjDK+0MvVg5DMKOCPn8g7CQICpaMSNdUpir7HCJtxIl/Acs6yTQ0yv6TJ9kvX9B3E6MKmTKkaJ3cLLSjO0kXhzlB9apKf/kvGTuoWXh5/qYZnSXRIb1xeTxwClOU8T9CTkcqr+wTIJ+HaxJUpZyKQ7M+Ki+8QLGKsXqSnjhBheAqkroqc+KuBSFZjBJqXmiV3kHGDsJZDK/xd0hwf/zAUmjq/2VNHlOU2ViqeVGk2/J6psAPmkCGKesXTQoB9xgyL2IzYGiy3HLFE2ahno6aUAaEB7Y5mKalm3qhI5UOCNu1d1mANAEpLk9lm3Ojs/CII170//6Y=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5NjE4NzA0NTAxNCwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEwMzA3M2Y3MTZmZmQzNzE4ODJkOTRiYTVhZjBlNWU1YzBkMzc2MTljNGExZGJhNTk5NmFmZDczMjI1ODczYiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-11.5, 68, -140.5, 45, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
       new GUIRosetta().open(e.player());
    }
}
