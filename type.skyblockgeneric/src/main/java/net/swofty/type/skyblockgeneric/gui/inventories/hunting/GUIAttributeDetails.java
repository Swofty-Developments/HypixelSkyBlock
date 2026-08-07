package net.swofty.type.skyblockgeneric.gui.inventories.hunting;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHunting;
import net.swofty.type.skyblockgeneric.hunting.AttributeDefinition;
import net.swofty.type.skyblockgeneric.hunting.AttributeText;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public final class GUIAttributeDetails extends StatelessView {
    private final AttributeDefinition definition;

    public GUIAttributeDetails(AttributeDefinition definition) {
        this.definition = definition;
    }

    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(definition.name(), InventoryType.CHEST_5_ROW);
    }

    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        DatapointHunting.HuntingData data = ((SkyBlockPlayer) ctx.player()).getHuntingData();
        int level = data.level(definition.id());
        int syphoned = data.syphoned(definition.id());
        layout.slot(4, AttributeGUIItems.attribute(definition, data, true));

        List<String> maxLore = new ArrayList<>(AttributeText.wrap(AttributeText.atLevel(definition, 10), "§7", 34));
        maxLore.add("");
        int remaining = Math.max(0, definition.rarity().cumulativeForLevel(10) - syphoned);
        if (remaining > 0) {
            maxLore.add("§7Requires §b" + remaining + " §7more Shards to max out");
            maxLore.add("§7(§a" + syphoned + "§7/§c" + definition.rarity().cumulativeForLevel(10) + "§7).");
        } else {
            maxLore.add("§7Max level reached!");
        }
        layout.slot(20, ItemStackCreator.getStack((level >= 10 ? "§a" : "§c") + "Max Level - §6" + definition.name(),
                Material.EXPERIENCE_BOTTLE, 1, maxLore));

        List<String> huntLore = new ArrayList<>();
        huntLore.add("");
        huntLore.addAll(AttributeText.huntInfo(definition));
        layout.slot(22, ItemStackCreator.getStack("§aHow to Hunt", Material.LEAD, 1, huntLore));
        layout.slot(24, ItemStackCreator.getStack(data.enabled(definition.id()) ? "§cToggle" : "§aToggle", Material.OAK_BUTTON, 1,
                        "§7Currently: " + (data.enabled(definition.id()) ? "§aON" : "§cOFF"), "", level == 0 ? "§cNot Unlocked!" : "§eClick to toggle!"),
                (_, c) -> {
                    data.toggle(definition.id());
                    c.session(DefaultState.class).setState(state);
                });
        Components.back(layout, 39, ctx, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Attribute Menu"));
        Components.close(layout, 40);
    }
}
