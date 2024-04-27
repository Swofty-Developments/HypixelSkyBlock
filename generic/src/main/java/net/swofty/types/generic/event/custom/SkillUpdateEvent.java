package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class SkillUpdateEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkillCategories skillCategory;
    private final double oldValueRaw;
    private final double oldValueCumulative;
    private final double newValueRaw;
    private final double newValueCumulative;

    public SkillUpdateEvent(SkyBlockPlayer player, SkillCategories skillCategory, double oldValueRaw, double oldValueCumulative, double newValueRaw, double newValueCumulative) {
        this.player = player;
        this.skillCategory = skillCategory;
        this.oldValueRaw = oldValueRaw;
        this.oldValueCumulative = oldValueCumulative;
        this.newValueRaw = newValueRaw;
        this.newValueCumulative = newValueCumulative;
    }
}
