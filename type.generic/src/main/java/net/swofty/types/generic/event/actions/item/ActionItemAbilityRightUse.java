package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockAbility;
import net.swofty.types.generic.user.PlayerAbilityHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;

public class ActionItemAbilityRightUse implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerUseItemEvent event) {
        ItemStack itemStack = event.getItemStack();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() != null && item.getGenericInstance() instanceof CustomSkyBlockAbility abilityClass) {
            CustomSkyBlockAbility.Ability ability = abilityClass.getFromActivation(CustomSkyBlockAbility.AbilityActivation.RIGHT_CLICK);
            if (ability != null) {
                if (!ability.getAbilityCost().canUse(player)) {
                    ability.getAbilityCost().onFail(player);
                    return;
                }

                PlayerAbilityHandler abilityHandler = player.getAbilityHandler();
                if (!abilityHandler.canUseAbility(item, ability.getCooldownTicks())) {
                    player.sendMessage("§cThis ability is on cooldown for " +
                            Math.round((float) abilityHandler.getRemainingCooldown(item, ability.getCooldownTicks()) / 1000) + "s.");
                    return;
                }

                abilityHandler.startAbilityCooldown(item);
                ability.getAbilityCost().onUse(player, ability);
                ability.onUse(player, item);
            }
        }
    }
}
