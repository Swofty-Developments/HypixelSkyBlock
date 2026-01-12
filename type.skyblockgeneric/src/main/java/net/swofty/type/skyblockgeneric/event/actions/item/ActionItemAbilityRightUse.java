package net.swofty.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AbilityComponent;
import net.swofty.type.skyblockgeneric.item.handlers.ability.RegisteredAbility;
import net.swofty.type.skyblockgeneric.user.PlayerAbilityHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemAbilityRightUse implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerUseItemEvent event) {
        ItemStack itemStack = event.getItemStack();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        UnderstandableSkyBlockItem understandable = item.toUnderstandable();
        String string = understandable.serialize();


        if (item.hasComponent(AbilityComponent.class)) {
            AbilityComponent abilityComponent = item.getComponent(AbilityComponent.class);

            RegisteredAbility ability = null;
            if (player.isSneaking()) {
                ability = abilityComponent.getAbility(RegisteredAbility.AbilityActivation.SNEAK_RIGHT_CLICK);
            }
            if (ability == null) {
                ability = abilityComponent.getAbility(RegisteredAbility.AbilityActivation.RIGHT_CLICK);
            }

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

                if(ability.execute(player, item)){
                    abilityHandler.startAbilityCooldown(item);
                }
            }
        }
    }
}
