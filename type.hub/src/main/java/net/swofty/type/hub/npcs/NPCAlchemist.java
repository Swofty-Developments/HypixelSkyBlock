package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUIShopAdventurer;
import net.swofty.type.hub.gui.GUIShopAlchemist;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCAlchemist extends HypixelNPC {
    public NPCAlchemist() {
        super(new AnimalConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Alchemist", "§e§lCLICK"};
            }

            @Override
            public float hologramYOffset() {
                return 0.1f;
            }

            @Override
            public EntityType entityType() {
                return EntityType.WITCH;
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(41.5, 70, -63.5);
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

        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_ALCHEMIST);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_ALCHEMIST, true);
            });
            return;
        }

        player.openView(new GUIShopAlchemist());
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "There is a darkness in you, " + player.getUsername() + ".",
                                "I've seen it in my flames, you are destined for great things.",
                                "For now, you shouldn't let it get to your head."
                        }).build(),
        };
    }
}
