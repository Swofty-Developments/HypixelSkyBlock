package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockAbility;
import net.swofty.types.generic.user.PlayerAbilityHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;

public class ActionItemAbilityLeftUse implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerHandAnimationEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() != null && item.getGenericInstance() instanceof CustomSkyBlockAbility ability) {
            if (ability.getAbilityActivation() == CustomSkyBlockAbility.AbilityActivation.LEFT_CLICK) {
                if (ability.getManaCost() > player.getMana()) {
                    player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                            .display("§c§lNOT ENOUGH MANA")
                            .ticksToLast(20 * 2)
                            .build(), StatisticDisplayReplacement.DisplayType.MANA);
                    return;
                }

                PlayerAbilityHandler abilityHandler = player.getAbilityHandler();

                if (!abilityHandler.canUseAbility(item, ability.getAbilityCooldownTicks())) {
                    player.sendMessage("§cThis ability is on cooldown for " +
                            Math.round((float) abilityHandler.getRemainingCooldown(item, ability.getAbilityCooldownTicks()) / 1000) + "s.");
                    return;
                }

                player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                        .display("§b-" + ability.getManaCost() + " (§6" + ability.getAbilityName() + "§b)")
                        .ticksToLast(20 * 2)
                        .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
                abilityHandler.startAbilityCooldown(item);
                player.setMana(player.getMana() - ability.getManaCost());

                ability.onAbilityUse(player, item);
            }
        }
    }
}
