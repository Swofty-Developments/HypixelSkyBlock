package net.swofty.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class GUISelectAnOption extends StatelessView {

    private static final int[] RESOURCE_SLOTS = {10, 12, 14, 16};
    private static final int[] TEAM_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19};

    private final String title;
    private final Function<ViewContext, List<Option>> optionSupplier;

    public GUISelectAnOption() {
        this("Select an option:", ignored -> buildResourceOptions("I'm collecting"));
    }

    private GUISelectAnOption(String title, Function<ViewContext, List<Option>> optionSupplier) {
        this.title = title;
        this.optionSupplier = optionSupplier;
    }

    public static GUISelectAnOption forResourceCommunication(String prefix) {
        return new GUISelectAnOption("Select an option:", ignored -> buildResourceOptions(prefix));
    }

    public static GUISelectAnOption forTeamCommunication(String prefix) {
        return new GUISelectAnOption("Select a team:", ctx -> buildTeamOptions(ctx, prefix));
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString((_, _) -> title, InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        List<Option> options = optionSupplier.apply(ctx);
        if (options.isEmpty()) {
            layout.slot(13, ItemStackCreator.getStack(
                "§cNo options available",
                Material.BARRIER,
                1,
                "§7There are no valid options",
                "§7for this selection."
            ));
        } else {
            for (Option option : options) {
                layout.slot(option.slot(), buildOptionItem(option), (click, context) -> {
                    if (!(click.player() instanceof BedWarsPlayer player)) {
                        return;
                    }

                    GUIQuickCommunications.sendTeamQuickMessage(player, option.message());
                    GUIQuickCommunications.playClickSound(player);
                    player.closeInventory();
                });
            }
        }

        Components.back(layout, 31, ctx);
    }

    private static ItemStack.Builder buildOptionItem(Option option) {
        return ItemStackCreator.getStack(
            option.displayName(),
            option.icon(),
            1,
            "§7Click to send the message: \"" + option.message() + "§7\"",
            "§7to your teammates!",
            "",
            "§eClick to send!"
        );
    }

    private static List<Option> buildResourceOptions(String prefix) {
        List<Option> options = new ArrayList<>(RESOURCE_SLOTS.length);
        options.add(new Option(
            RESOURCE_SLOTS[0],
            "§a" + prefix + " §b§lDIAMOND",
            Material.DIAMOND,
            "§a" + prefix + " §b§lDIAMOND"
        ));
        options.add(new Option(
            RESOURCE_SLOTS[1],
            "§a" + prefix + " §f§lIRON",
            Material.IRON_INGOT,
            "§a" + prefix + " §f§lIRON"
        ));
        options.add(new Option(
            RESOURCE_SLOTS[2],
            "§a" + prefix + " §6§lGOLD",
            Material.GOLD_INGOT,
            "§a" + prefix + " §6§lGOLD"
        ));
        options.add(new Option(
            RESOURCE_SLOTS[3],
            "§a" + prefix + " §2§lEMERALD",
            Material.EMERALD,
            "§a" + prefix + " §2§lEMERALD"
        ));
        return options;
    }

    private static List<Option> buildTeamOptions(ViewContext ctx, String prefix) {
        if (!(ctx.player() instanceof BedWarsPlayer player)) {
            return List.of();
        }

        BedWarsGame game = player.getGame();
        TeamKey ownTeam = GUIQuickCommunications.resolveTeamKey(player);
        if (game == null || ownTeam == null) {
            return List.of();
        }

        List<TeamKey> teams = game.getTeams().stream()
            .filter(BedWarsTeam::hasPlayers)
            .map(BedWarsTeam::getTeamKey)
            .filter(team -> team != ownTeam)
            .sorted(Comparator.comparingInt(Enum::ordinal))
            .toList();

        List<Option> options = new ArrayList<>();
        int optionCount = Math.min(TEAM_SLOTS.length, teams.size());
        for (int i = 0; i < optionCount; i++) {
            TeamKey team = teams.get(i);
            String coloredTeamName = team.chatColor() + "§l" + team.getName().toUpperCase(Locale.ROOT);
            String message = "§a" + prefix + " " + coloredTeamName;
            options.add(new Option(
                TEAM_SLOTS[i],
                message,
                getWool(team),
                message
            ));
        }

        return options;
    }

    private static Material getWool(TeamKey teamKey) {
        return switch (teamKey) {
            case RED -> Material.RED_WOOL;
            case BLUE -> Material.BLUE_WOOL;
            case GREEN -> Material.GREEN_WOOL;
            case YELLOW -> Material.YELLOW_WOOL;
            case AQUA -> Material.LIGHT_BLUE_WOOL;
            case WHITE -> Material.WHITE_WOOL;
            case PINK -> Material.PINK_WOOL;
            case GRAY -> Material.GRAY_WOOL;
        };
    }

    private record Option(int slot, String displayName, Material icon, String message) {
    }
}
