package net.swofty.type.thefarmingislands.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;

public class VillagerWindmillOperator extends NPCVillagerDialogue {
    public VillagerWindmillOperator() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Windmill Operator", "§e§lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(98.5, 89, -282.5, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.NONE;
            }
        });
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return new DialogueSet[0];
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
