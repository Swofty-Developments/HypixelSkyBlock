package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.guild.GuildData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.guild.GuildManager;

public class GUIGuildTagColor extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Guild Tag Color", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        add(layout, 10, "§7Gray", Material.LIGHT_GRAY_DYE, "§7", 5);
        add(layout, 11, "§3Dark Aqua", Material.CYAN_DYE, "§3", 15);
        add(layout, 12, "§2Dark Green", Material.GREEN_DYE, "§2", 25);
        layout.slot(31, ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1),
            (click, viewCtx) -> viewCtx.navigator().pop());
    }

    private void add(ViewLayout<DefaultState> layout, int slot, String name, Material material, String color, int requiredLevel) {
        layout.slot(slot, (s, c) -> {
                final GuildData data = GuildManager.getGuildFromPlayer(c.player());
                if (data == null) return ItemStackCreator.getStack(name, material, 1,
                    "§7Preview: " + color + "[GUILD]", "", "§cYou must be in a guild to preview the tag color!");

                boolean unlocked = data.getLevel() >= requiredLevel;
                return ItemStackCreator.getStack(unlocked ? name : "§c" + name.substring(2), unlocked ? material : Material.GRAY_DYE, 1,
                    "§7Preview: " + color + "[" + data.getTag() + "]", "",
                    unlocked ? "§eClick to pick this color!" : "§cRequires Guild Level " + requiredLevel);
            },
            (click, ctx) -> {
                GuildData data = GuildManager.getGuildFromPlayer(ctx.player());
                if (data != null && data.getLevel() >= requiredLevel)
                    GuildManager.changeSetting(ctx.player(), "tagcolor", color);
                else ctx.player().sendMessage("§cThis tag color requires Guild Level " + requiredLevel + "!");
            });
    }
}
