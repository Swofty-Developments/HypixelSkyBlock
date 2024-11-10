package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.AbilityComponent;
import net.swofty.types.generic.item.handlers.ability.RegisteredAbility;
import net.swofty.types.generic.user.PlayerAbilityHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionItemAbilityRightUse implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerUseItemEvent event) {
        ItemStack itemStack = event.getItemStack();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.hasComponent(AbilityComponent.class)) {
            AbilityComponent abilityComponent = item.getComponent(AbilityComponent.class);
            RegisteredAbility ability = abilityComponent.getAbility(RegisteredAbility.AbilityActivation.LEFT_CLICK);
            if (ability != null) {
                if (!ability.getCost().canUse(player)) {
                    ability.getCost().onFail(player);
                    return;
                }

                PlayerAbilityHandler abilityHandler = player.getAbilityHandler();
                if (!abilityHandler.canUseAbility(item, ability.getCooldownTicks())) {
                    player.sendMessage("§cThis ability is on cooldown for " +
                            Math.round((float) abilityHandler.getRemainingCooldown(item, ability.getCooldownTicks()) / 1000) + "s.");
                    return;
                }

                abilityHandler.startAbilityCooldown(item);
                ability.getCost().onUse(player, ability);
                ability.execute(player, item);
            }
        }
    }
}
