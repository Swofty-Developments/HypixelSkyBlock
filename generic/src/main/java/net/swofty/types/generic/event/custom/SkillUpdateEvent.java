package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class SkillUpdateEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkillCategories skillCategory;
    private final double oldValue;
    private final double newValue;

    public SkillUpdateEvent(SkyBlockPlayer player, SkillCategories skillCategory, double oldValue, double newValue) {
        this.player = player;
        this.skillCategory = skillCategory;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
