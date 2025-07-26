package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SkillableMineComponent;
import net.swofty.types.generic.skill.SkillCategories;

public class ActionSkillMiningHandler implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
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
