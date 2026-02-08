package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;

public class NPCCoachJackrabbit extends HypixelNPC {

    public NPCCoachJackrabbit() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§dCoach Jackrabbit", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMzAyMjkyOTYwNCwKICAicHJvZmlsZUlkIiA6ICI2NGY0MGFiNzFmM2E0NGZiYjg0N2I5ZWFhOWZjNDRlNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJvZGF2aWRjZXNhciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iYzBjYzY3ZTc5YzIyOGU1NDFlNjhhZWIxZDgxZWQ3YWY1MTE2NjYyMmFkNGRiOTQxN2Q3YTI5ZDFiODlhZjk1IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-7.5, 69, 1.5, -74.5f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public String chatName() {
                return "§dCoach Jackrabbit";
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;
        ((SkyBlockPlayer) e.player()).openView(new GUIChocolateFactory()).refreshEvery(Duration.ofSeconds(1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return DialogueSet.EMPTY;
    }
}
