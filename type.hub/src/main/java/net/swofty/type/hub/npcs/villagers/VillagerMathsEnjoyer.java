package net.swofty.type.hub.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;


import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class VillagerMathsEnjoyer extends HypixelNPC {

    public VillagerMathsEnjoyer() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§fMaths Enjoyer", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(56, 69, -40, 180, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.LIBRARIAN;
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
        return new DialogueSet[]{
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§fHey if you really want to know...",
                                "§fThe formula to §6Magical Power §fis...",
                                "§dStats Mult. §f= §b29.97§e(§aln(§b0.0019§6MP§a+1)§e)^§b1.2",
                                "§fHave fun with that!"
                        }).build()
        };
    }
}
