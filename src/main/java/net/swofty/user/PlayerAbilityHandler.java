package net.swofty.user;

import net.swofty.item.SkyBlockItem;

import java.util.HashMap;
import java.util.Map;

public class PlayerAbilityHandler {
    private final Map<String, Long> abilityCooldowns = new HashMap<>();

    public boolean canUseAbility(SkyBlockItem item, int coolDownDurationTicks) {
        String itemType = getItemType(item);
        Long lastUsedTime = abilityCooldowns.get(itemType);

        long cooldownDurationMillis = coolDownDurationTicks * 50L;
        return lastUsedTime == null || (System.currentTimeMillis() - lastUsedTime) >= cooldownDurationMillis;
    }

    public long getRemainingCooldown(SkyBlockItem item, int coolDownDurationTicks) {
        String itemType = getItemType(item);
        Long lastUsedTime = abilityCooldowns.get(itemType);
        long cooldownDurationMillis = coolDownDurationTicks * 50L;
        if (lastUsedTime == null) {
            return 0;
        }
        long timePassed = System.currentTimeMillis() - lastUsedTime;
        return timePassed < cooldownDurationMillis ? cooldownDurationMillis - timePassed : 0;
    }

    public void startAbilityCooldown(SkyBlockItem item) {
        abilityCooldowns.put(getItemType(item), System.currentTimeMillis());
    }

    // Internal methods for Items
    private String getItemType(SkyBlockItem item) {
        return item.getAttributeHandler().getItemType();
    }
}
