package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;

public class VillagerMathsEnjoyer extends NPCVillagerDialogue {

    public VillagerMathsEnjoyer() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§fMaths Enjoyer", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(56, 69, -40, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.LIBRARIAN;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return new DialogueSet[]{
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Maths Enjoyer: §fHey if you really want to know...",
                                "§e[NPC] Maths Enjoyer: §fThe formula to §6Magical Power §fis...",
                                "§e[NPC] Maths Enjoyer: §dStats Mult. §f= §b29.97§e(§aln(§b0.0019§6MP§a+1)§e)^§b1.2",
                                "§e[NPC] Maths Enjoyer: §fHave fun with that!"
                        }).build()
        };
    }
}
