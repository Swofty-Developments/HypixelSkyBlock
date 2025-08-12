package net.swofty.type.skyblockgeneric.user;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.HashMap;
import java.util.Map;

public class PlayerAbilityHandler {
    private final Map<String, Long> abilityCooldowns = new HashMap<>();

    public boolean canUseAbility(SkyBlockItem item, int coolDownDurationTicks) {
        String itemType = item.getAttributeHandler().getTypeAsString();
        Long lastUsedTime = abilityCooldowns.get(itemType);

        long cooldownDurationMillis = coolDownDurationTicks * 50L;
        return lastUsedTime == null || (System.currentTimeMillis() - lastUsedTime) >= cooldownDurationMillis;
    }

    public long getRemainingCooldown(SkyBlockItem item, int coolDownDurationTicks) {
        String itemType = item.getAttributeHandler().getTypeAsString();
        Long lastUsedTime = abilityCooldowns.get(itemType);
        long cooldownDurationMillis = coolDownDurationTicks * 50L;
        if (lastUsedTime == null) {
            return 0;
        }
        long timePassed = System.currentTimeMillis() - lastUsedTime;
        return timePassed < cooldownDurationMillis ? cooldownDurationMillis - timePassed : 0;
    }

    public void startAbilityCooldown(SkyBlockItem item) {
        abilityCooldowns.put(item.getAttributeHandler().getTypeAsString(), System.currentTimeMillis());
    }
}
