package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.type.generic.data.datapoints.DatapointSkills;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.SkillableMineComponent;
import net.swofty.type.generic.skill.SkillCategories;

public class ActionSkillMiningHandler implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        SkyBlockItem item = new SkyBlockItem(event.getMaterial());

        if (!item.hasComponent(SkillableMineComponent.class)) return;

        SkillableMineComponent skillableMine = item.getComponent(SkillableMineComponent.class);
        SkillCategories skillCategory = skillableMine.getCategory();
        DatapointSkills.PlayerSkills skills = event.getPlayer().getSkills();

        skills.increase(event.getPlayer(), skillCategory, skillableMine.getMiningValue());
    }
}
