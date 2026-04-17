package net.swofty.type.replayviewer.view;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.TimedPotion;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUIViewPlayer implements StatefulView<GUIViewPlayer.State> {

    public record State(ReplayPlayerEntity entity) {
    }

    @Override
    public State initialState() {
        return new State(null);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> {
            var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
            if (sessionOpt.isEmpty()) {
                return "Player";
            }

            return state.entity != null ? getDisplayName(state.entity) : "Player";
        }, InventoryType.CHEST_2_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
        if (sessionOpt.isEmpty()) {
            layout.slot(4, ItemStackCreator.getStack(
                "§cNo Replay Session",
                Material.BARRIER,
                1,
                "§7You are not currently watching",
                "§7a replay."
            ));
            Components.back(layout, 13, ctx);
            return;
        }

        ReplaySession replaySession = sessionOpt.get();
        ReplayPlayerEntity replayPlayer = state.entity;

        if (replayPlayer == null) {
            layout.slot(4, ItemStackCreator.getStack(
                "§cPlayer Not Found",
                Material.BARRIER,
                1,
                "§7This player entity is not",
                "§7available at this timestamp."
            ));
            Components.back(layout, 49, ctx);
            return;
        }

        String displayName = getDisplayName(replayPlayer);
        ItemStack.Builder head = replayPlayer.getSkin() != null
            ? ItemStackCreator.getStackHead(
            displayName,
            replayPlayer.getSkin(),
            1,
            "§7Health: §f" + Math.max(0, Math.round(replayPlayer.getHealth())),
            "",
            "§eRight Click for first person!"
        )
            : ItemStackCreator.getStack(
            displayName,
            Material.PLAYER_HEAD,
            1,
            "§7Health: §f" + Math.max(0, Math.round(replayPlayer.getHealth())),
            "",
            "§eRight Click for first person!"
        );

        layout.slot(0, head, (click, c) -> {
            if (click.click() instanceof Click.Right) {
                replaySession.followEntity(c.player(), state.entity.getInternalId());
                c.player().closeInventory();
                return;
            }

            c.player().teleport(replayPlayer.getPosition());
        });

        layout.slot(1, createEffectsItem(replayPlayer));
        layout.autoUpdating(3, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.MAIN_HAND), "§cEmpty main hand slot."), Duration.ofSeconds(1));
        layout.autoUpdating(5, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.HELMET), "§cEmpty helmet slot."), Duration.ofSeconds(1));
        layout.autoUpdating(6, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.CHESTPLATE), "§cEmpty chestplate slot."), Duration.ofSeconds(1));
        layout.autoUpdating(7, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.LEGGINGS), "§cEmpty leggings slot."), Duration.ofSeconds(1));
        layout.autoUpdating(8, (_, _) -> createEquipmentItem(replayPlayer.getEquipment(EquipmentSlot.BOOTS), "§cEmpty boots slot."), Duration.ofSeconds(1));

        layout.slot(9, ItemStackCreator.getStack(
            "§aReport Player",
            Material.ANVIL,
            1,
            "§7Report this player for breaking the",
            "§7rules. This replay will be saved",
            "§7along with the report to be reviewed.",
            "",
            "§eClick to report!"
        ), (_, c) -> c.player().notImplemented());
    }

    private static String getDisplayName(ReplayPlayerEntity replayPlayer) {
        try {
            return HypixelPlayer.getDisplayName(replayPlayer.getActualUuid());
        } catch (Exception ignored) {
            return "§7" + replayPlayer.getPlayerName();
        }
    }

    private static ItemStack.Builder createEffectsItem(ReplayPlayerEntity replayPlayer) {
        List<TimedPotion> effects = new ArrayList<>(replayPlayer.getActiveEffects());
        if (effects.isEmpty()) {
            return ItemStackCreator.getStack(
                "§aActive Status Effects",
                Material.POTION,
                1,
                "§7No status effects."
            );
        }

        List<String> lore = new ArrayList<>();
        for (TimedPotion timedPotion : effects) {
            String effectName = formatEffectName(timedPotion.potion().effect().toString());
            int amplifier = timedPotion.potion().amplifier() + 1;
            lore.add("§7- §a" + effectName + " " + StringUtility.getAsRomanNumeral(amplifier));
        }

        return ItemStackCreator.getStack(
            "§aActive Status Effects",
            Material.POTION,
            1,
            lore
        );
    }

    private static ItemStack.Builder createEquipmentItem(ItemStack itemStack, String emptyText) {
        if (itemStack == null || itemStack.isAir()) {
            return ItemStackCreator.getStack(
                emptyText,
                Material.RED_STAINED_GLASS_PANE,
                1
            );
        }
        return itemStack.builder();
    }

    private static String formatEffectName(String raw) {
        String cleaned = raw.toLowerCase(Locale.ROOT)
            .replace("minecraft:", "")
            .replace('_', ' ');

        String[] words = cleaned.split(" ");
        StringBuilder out = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                out.append(word.substring(1));
            }
        }
        return out.toString();
    }
}
