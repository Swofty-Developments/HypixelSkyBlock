package net.swofty.type.village.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;

import java.util.stream.Stream;

public class VillagerMathsEnjoyer extends SkyBlockVillagerNPC {

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
    public void onClick(PlayerClickVillagerNPCEvent event) {
        Player player = event.player();
        if (isInDialogue(player)) return;

        setDialogue(player);
    }

    private boolean isInDialogue(Player player) {
        return false;
    }

    private void setDialogue(Player player) {
        player.sendMessage("Hello! This is the dialogue for the Maths Enjoyer NPC.");
        player.sendMessage("The formula to Magical Power is:");
        player.sendMessage("Stats Mult. = 29.97 * (ln(0.0019 * MP + 1))^1.2");
        player.sendMessage("Have fun with that!");
    }

    @Override
    public NPCVillagerDialogue.DialogueSet[] getDialogueSets() {
        return Stream.of(
                NPCVillagerDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Maths Enjoyer: §fHey if you really want to know...",
                                "§e[NPC] Maths Enjoyer: The formula to §6Magical Power §fis...",
                                "§e[NPC] Maths Enjoyer: §dStats Mult. §f= §b29.97§e(§aln(§b0.0019§6MP§a+1)§e)^§b1.2",
                                "§e[NPC] Maths Enjoyer: Have fun with that!"
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
