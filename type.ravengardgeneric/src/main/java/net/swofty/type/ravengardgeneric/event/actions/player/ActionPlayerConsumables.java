package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardgeneric.classes.RavengardAbilityService;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;
import net.swofty.type.ravengardgeneric.item.attribute.RavengardItemAttributeHandler;
import net.swofty.type.ravengardgeneric.item.components.StandardItemComponent;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Consumables heal what their captured effect line says: the amount and optional duration are
 * read straight from the item's effect text, so a new potion works by existing in the config.
 */
public class ActionPlayerConsumables implements HypixelEventClass {
    private static final Pattern EFFECT = Pattern.compile("\\+?(\\d+) HP(?: over (\\d+) seconds?)?");

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onUse(PlayerUseItemEvent event) {
        if (!(event.getPlayer() instanceof RavengardPlayer player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        RavengardItemType type = new RavengardItemAttributeHandler(stack).getType();
        if (type == null) {
            return;
        }
        StandardItemComponent standard = type.component(StandardItemComponent.class);
        if (standard == null || !"CONSUMABLE".equals(standard.getStandardItemType())
                || type.getEffect() == null) {
            return;
        }

        event.setCancelled(true);
        Matcher matcher = EFFECT.matcher(type.getEffect());
        if (!matcher.find()) {
            return;
        }
        int amount = Integer.parseInt(matcher.group(1));
        int seconds = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
        RavengardAbilityService.consumeHeal(player, amount, seconds);

        ItemStack held = player.getItemInMainHand();
        if (held.amount() > 1) {
            player.setItemInMainHand(held.withAmount(held.amount() - 1));
        } else {
            player.setItemInMainHand(ItemStack.AIR);
        }
    }
}
