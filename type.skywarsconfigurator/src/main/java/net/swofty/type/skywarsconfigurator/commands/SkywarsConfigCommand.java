package net.swofty.type.skywarsconfigurator.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skywarsconfigurator.MapConfigurationSession;
import net.swofty.type.skywarsconfigurator.TypeSkywarsConfiguratorLoader;

import java.util.stream.Collectors;

/**
 * Command for configuring SkyWars maps.
 */
@CommandParameters(
        aliases = "swconfig",
        description = "Configure SkyWars maps",
        usage = "/swconfig <subcommand>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class SkywarsConfigCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        // Default - show usage
        command.setDefaultExecutor((sender, context) -> {
            sender.sendMessage("§eUsage:");
            sender.sendMessage("§7/swconfig new <id> <name> §f- Start new session");
            sender.sendMessage("§7/swconfig type <type> §f- Toggle game type");
            sender.sendMessage("§7/swconfig center §f- Set map center");
            sender.sendMessage("§7/swconfig void <y> §f- Set void Y level");
            sender.sendMessage("§7/swconfig bounds <minX> <minZ> <maxX> <maxZ> §f- Set bounds");
            sender.sendMessage("§7/swconfig island §f- Add island spawn");
            sender.sendMessage("§7/swconfig save §f- Save configuration");
            sender.sendMessage("§7/swconfig status §f- Show current status");
            sender.sendMessage("§8(Chests are auto-detected at runtime)");
        });

        // /swconfig new <id> <name>
        var newLit = ArgumentType.Literal("new");
        var idArg = ArgumentType.String("id");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String id = context.get(idArg);
            String name = context.get(nameArg);
            MapConfigurationSession session = new MapConfigurationSession(id, name);
            TypeSkywarsConfiguratorLoader.setCurrentSession(session);
            sender.sendMessage("§aStarted new configuration session for map: " + name + " (id: " + id + ")");
        }, newLit, idArg, nameArg);

        // /swconfig type <type>
        var typeLit = ArgumentType.Literal("type");
        var typeArg = ArgumentType.String("gameType");
        typeArg.setSuggestionCallback((sender, context, suggestion) -> {
            for (SkywarsGameType type : SkywarsGameType.values()) {
                suggestion.addEntry(new SuggestionEntry(type.name()));
            }
        });
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String typeName = context.get(typeArg);
            SkywarsGameType type = SkywarsGameType.from(typeName);
            if (type == null) {
                sender.sendMessage("§cInvalid type! Available: SOLO_NORMAL, SOLO_INSANE, DOUBLES_NORMAL, SOLO_LUCKY_BLOCK");
                return;
            }
            if (session.getTypes().contains(type)) {
                session.removeType(type);
                sender.sendMessage("§cRemoved type: §f" + type.name());
            } else {
                session.addType(type);
                sender.sendMessage("§aAdded type: §f" + type.name());
            }
            sender.sendMessage("§7Current types: §f" + session.getTypes().stream()
                    .map(Enum::name).collect(Collectors.joining(", ")));
        }, typeLit, typeArg);

        // /swconfig center
        var centerLit = ArgumentType.Literal("center");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session! Use /swconfig new <id> <name> first.");
                return;
            }
            Pos pos = player.getPosition();
            session.setCenter(pos.x(), pos.y(), pos.z());
            sender.sendMessage("§aSet map center to " + formatPos(pos));
        }, centerLit);

        // /swconfig void <y>
        var voidLit = ArgumentType.Literal("void");
        var yArg = ArgumentType.Integer("y");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int y = context.get(yArg);
            session.setVoidY(y);
            sender.sendMessage("§aSet void Y level to " + y);
        }, voidLit, yArg);

        // /swconfig bounds <minX> <minZ> <maxX> <maxZ>
        var boundsLit = ArgumentType.Literal("bounds");
        var minXArg = ArgumentType.Integer("minX");
        var minZArg = ArgumentType.Integer("minZ");
        var maxXArg = ArgumentType.Integer("maxX");
        var maxZArg = ArgumentType.Integer("maxZ");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int minX = context.get(minXArg);
            int minZ = context.get(minZArg);
            int maxX = context.get(maxXArg);
            int maxZ = context.get(maxZArg);
            session.setBounds(minX, minZ, maxX, maxZ);
            sender.sendMessage("§aSet bounds: (" + minX + ", " + minZ + ") to (" + maxX + ", " + maxZ + ")");
        }, boundsLit, minXArg, minZArg, maxXArg, maxZArg);

        // /swconfig island
        var islandLit = ArgumentType.Literal("island");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            Pos pos = player.getPosition();
            session.addIsland(pos);
            sender.sendMessage("§aAdded island #" + (session.getIslands().size() - 1) + " at " + formatPos(pos));
        }, islandLit);

        // /swconfig save
        var saveLit = ArgumentType.Literal("save");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.saveToFile();
            sender.sendMessage("§aSaved configuration to file!");
        }, saveLit);

        // /swconfig status
        var statusLit = ArgumentType.Literal("status");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeSkywarsConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            sender.sendMessage("§e=== Configuration Status ===");
            sender.sendMessage("§7Map ID: §f" + session.getMapId());
            sender.sendMessage("§7Map Name: §f" + session.getMapName());
            sender.sendMessage("§7Types: §f" + (session.getTypes().isEmpty() ? "SOLO_NORMAL (default)" :
                    session.getTypes().stream().map(Enum::name).collect(Collectors.joining(", "))));
            sender.sendMessage("§7Islands: §f" + session.getIslands().size());
            sender.sendMessage("§7Center: §f(" + session.getCenterX() + ", " + session.getCenterY() + ", " + session.getCenterZ() + ")");
            sender.sendMessage("§7Void Y: §f" + session.getVoidY());
            sender.sendMessage("§8(Chests are auto-detected at runtime)");
        }, statusLit);
    }

    private static String formatPos(Pos pos) {
        return String.format("(%.1f, %.1f, %.1f)", pos.x(), pos.y(), pos.z());
    }
}
