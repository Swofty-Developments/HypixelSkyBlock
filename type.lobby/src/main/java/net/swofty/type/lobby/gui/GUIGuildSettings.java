package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.guild.GuildData;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.guild.GuildManager;

import java.util.List;

public class GUIGuildSettings implements View<GUIGuildSettings.GuildSettingsState> {

    @Override
    public ViewConfiguration<GuildSettingsState> configuration() {
        return new ViewConfiguration<>("Guild Settings", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<GuildSettingsState> layout, GuildSettingsState state, ViewContext ctx) {
        GuildData guild = state.guild();

        String currentTag = guild.getTag() != null ? guild.getTag() : "None";
        layout.slot(10, ItemStackCreator.getStack(
                "§aGuild Tag",
                Material.NAME_TAG,
                1,
                "§7Current: §6" + currentTag,
                "§7Changes the tag next to your guild",
                "§7members' names.",
                "",
                "§eClick to edit!"
        ), (click, viewCtx) -> new HypixelSignGUI(viewCtx.player())
                .open(new String[]{"Guild Tag", "Enter new tag"})
                .thenAccept(value -> {
                    if (value == null || value.isBlank()) {
                        return;
                    }
                    GuildManager.changeSetting(viewCtx.player(), "tag", value.trim());
                }));

        String tagColor = guild.getTagColor() != null ? guild.getTagColor() : "§7";
        List<String> colors = List.of("§7", "§f", "§a", "§b", "§3", "§6", "§d", "§c", "§e");
        int colorIndex = Math.max(0, colors.indexOf(tagColor));
        String nextColor = colors.get((colorIndex + 1) % colors.size());
        layout.slot(11, ItemStackCreator.getStack(
                "§aGuild Tag Color",
                Material.RED_DYE,
                1,
                "§7Current: " + tagColor + "Sample",
                "§7Changes the color of the tag next to",
                "§7your guild members' names.",
                "",
                "§eClick to cycle colors!"
        ), (click, viewCtx) -> GuildManager.changeSetting(viewCtx.player(), "tagcolor", nextColor));

        layout.slot(12, ItemStackCreator.getStack(
                "§aGuild Permissions",
                Material.COMPARATOR,
                1,
                "§7Modify your guild's ranks & their",
                "§7permissions.",
                "",
                "§eClick to edit!"
        ), (click, viewCtx) -> viewCtx.player().sendMessage("§cGuild rank permission editor is not available yet."));

        String finderStatus = guild.isListedInFinder() ? "§aON" : "§cOFF";
        layout.slot(13, ItemStackCreator.getStack(
                "§aShown in Guild Finder",
                Material.SUNFLOWER,
                1,
                "§7Whether or not players can find the",
                "§7guild in Guild Finder and request to",
                "§7join.",
                "§7Currently " + finderStatus,
                "",
                "§eClick to toggle!"
        ), (click, viewCtx) -> GuildManager.changeSetting(viewCtx.player(), "finder", "toggle"));

        layout.slot(14, ItemStackCreator.getStack(
                "§aGuild Games",
                Material.COMPASS,
                1,
                "§7Changes the Guild's list of games",
                "§7used in the Guild Finder.",
                "",
                "§eClick to pick games!"
        ), (click, viewCtx) -> viewCtx.player().sendMessage("§cGuild games selector is not available yet."));

        String description = guild.getDescription() != null && !guild.getDescription().isEmpty()
                ? guild.getDescription() : "Not set";
        layout.slot(15, ItemStackCreator.getStack(
                "§aGuild Description",
                Material.WRITABLE_BOOK,
                1,
                "§7Current: §f" + description,
                "§7Changes the Guild's description as",
                "§7shown in the Guild Finder.",
                "",
                "§eClick to edit!"
        ), (click, viewCtx) -> new HypixelSignGUI(viewCtx.player())
                .open(new String[]{"Description", "Enter description"})
                .thenAccept(value -> {
                    if (value == null || value.isBlank()) {
                        return;
                    }
                    GuildManager.changeSetting(viewCtx.player(), "description", value.trim());
                }));

        String slowStatus = guild.isSlowChat() ? "§aON" : "§cOFF";
        layout.slot(16, ItemStackCreator.getStack(
                "§aPersonal Guild Settings",
                Material.ORANGE_DYE,
                1,
                "§7Slow Chat: " + slowStatus,
                "",
                "§eClick to toggle!"
        ), (click, viewCtx) -> GuildManager.changeSetting(viewCtx.player(), "slow", "toggle"));

        layout.slot(31, ItemStackCreator.getStack(
                "§aGo Back",
                Material.ARROW,
                1
        ), (click, viewCtx) -> viewCtx.navigator().pop());
    }

    public record GuildSettingsState(GuildData guild) { }
}
