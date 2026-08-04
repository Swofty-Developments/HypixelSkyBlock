package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.entity.animation.AnimReviewService;
import net.swofty.type.ravengardgeneric.entity.animation.RavengardReviewClip;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUIAnimReview extends RavengardView {
    private final String mob;

    public GUIAnimReview(String mob) {
        this.mob = mob;
    }

    @Override
    protected String title() {
        return mob == null ? "Animation Review" : "Captures: " + mob;
    }

    @Override
    protected boolean usesChrome() {
        return false;
    }

    private static Map<String, List<String>> collated() {
        Map<String, List<String>> byMob = new LinkedHashMap<>();
        for (String name : RavengardReviewClip.available()) {
            String mobName = name.replaceAll("_\\d+$", "");
            byMob.computeIfAbsent(mobName, key -> new ArrayList<>()).add(name);
        }
        return byMob;
    }

    @Override
    protected void content(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        if (mob == null) {
            int slot = 0;
            for (var entry : collated().entrySet()) {
                if (slot >= 54) break;
                String mobName = entry.getKey();
                int captures = entry.getValue().size();
                layout.slot(slot++, item(Material.SKELETON_SKULL, "§e" + mobName,
                                "§7" + captures + (captures == 1 ? " capture" : " captures"),
                                "§eClick to browse!"),
                        (click, viewContext) -> {
                            if (viewContext.player() instanceof RavengardPlayer player) {
                                ViewNavigator.get(player).push(new GUIAnimReview(mobName));
                            }
                        });
            }
            return;
        }

        layout.slot(53, item(Material.BARRIER, "§cAll mobs", "§eClick to go back!"),
                (click, viewContext) -> {
                    if (viewContext.player() instanceof RavengardPlayer player) {
                        ViewNavigator.get(player).push(new GUIAnimReview(null));
                    }
                });
        List<String> clips = collated().getOrDefault(mob, List.of());
        int slot = 0;
        for (String clipName : clips) {
            if (slot >= 45) break;
            layout.slot(slot++, item(Material.PAPER, "§f" + clipName,
                            "§7Raw capture replay.",
                            "§eClick to review on the stage!"),
                    (click, viewContext) -> {
                        if (viewContext.player() instanceof RavengardPlayer player) {
                            player.closeInventory();
                            AnimReviewService.start(player, clipName);
                        }
                    });
        }
    }

    private static ItemStack.Builder item(Material material, String name, String... lore) {
        List<Component> lines = new ArrayList<>();
        for (String line : lore) {
            lines.add(Component.text(line).decoration(TextDecoration.ITALIC, false));
        }
        return ItemStack.builder(material)
                .customName(Component.text(name).decoration(TextDecoration.ITALIC, false))
                .lore(lines);
    }
}
