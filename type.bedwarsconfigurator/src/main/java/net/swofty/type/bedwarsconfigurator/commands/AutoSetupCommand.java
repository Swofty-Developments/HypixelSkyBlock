package net.swofty.type.bedwarsconfigurator.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.GeneratorSpeed;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.PitchYawPosition;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.Position;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsconfigurator.TypeBedWarsConfiguratorLoader;
import net.swofty.type.bedwarsconfigurator.autosetup.AutoSetupSession;
import net.swofty.type.bedwarsconfigurator.autosetup.DebugMarkerManager;
import net.swofty.type.bedwarsconfigurator.autosetup.WorldScanner;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@CommandParameters(
        aliases = "setup mapsetup",
        description = "Automatic BedWars map configuration tool",
        usage = "/autosetup <subcommand>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class AutoSetupCommand extends HypixelCommand {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            sendHelp(sender);
        });

        registerScanCommand(command);
        registerBoundsCommand(command);
        registerTypeCommand(command);
        registerTeamCommand(command);
        registerGlobalCommand(command);
        registerLocationCommand(command);
        registerShowCommand(command);
        registerHideCommand(command);
        registerStatusCommand(command);
        registerSaveCommand(command);
        registerGeneratorSettingsCommand(command);
        registerMapInfoCommand(command);
    }

    private void sendHelp(net.minestom.server.command.CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== BedWars Auto Setup ==="));
        sender.sendMessage(Component.text("§e/autosetup scan §7- Scan world for beds, generators, etc."));
        sender.sendMessage(Component.text("§e/autosetup bounds <min|max> [x y z] §7- Set map bounds"));
        sender.sendMessage(Component.text("§e/autosetup type <add|remove> <type> §7- Configure game types"));
        sender.sendMessage(Component.text("§e/autosetup team <team> <spawn|bed|generator|itemshop|teamshop> [x y z] §7- Set team positions"));
        sender.sendMessage(Component.text("§e/autosetup global <diamond|emerald> <add|remove> [x y z] §7- Manage global generators"));
        sender.sendMessage(Component.text("§e/autosetup waiting [x y z] §7- Set waiting spawn"));
        sender.sendMessage(Component.text("§e/autosetup spectator [x y z] §7- Set spectator spawn"));
        sender.sendMessage(Component.text("§e/autosetup show §7- Show debug markers"));
        sender.sendMessage(Component.text("§e/autosetup hide §7- Hide debug markers"));
        sender.sendMessage(Component.text("§e/autosetup status §7- Show current configuration status"));
        sender.sendMessage(Component.text("§e/autosetup name <name> §7- Set map display name"));
        sender.sendMessage(Component.text("§e/autosetup generator <slow|medium|fast|very_fast> §7- Configure generator settings"));
        sender.sendMessage(Component.text("§e/autosetup save §7- Save configuration to maps.json"));
    }

    private void registerScanCommand(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            Instance instance = player.getInstance();
            if (instance == null) {
                player.sendMessage(Component.text("§cYou must be in a map instance to scan."));
                return;
            }

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), instance);

            if (!session.hasBounds()) {
                player.sendMessage(Component.text("§cPlease set bounds first using /autosetup bounds min and /autosetup bounds max"));
                return;
            }

            player.sendMessage(Component.text("§eScanning world... This may take a moment."));

            WorldScanner scanner = new WorldScanner(instance, session);
            WorldScanner.ScanResult result = scanner.fullScan();

            // Send results
            for (String msg : result.getMessages()) {
                player.sendMessage(Component.text("§a✔ " + msg));
            }
            for (String warning : result.getWarnings()) {
                player.sendMessage(Component.text("§6⚠ " + warning));
            }
            for (String error : result.getErrors()) {
                player.sendMessage(Component.text("§c✖ " + error));
            }

            if (!result.hasErrors()) {
                player.sendMessage(Component.text("§aScan complete! Use /autosetup show to visualize, /autosetup status to review."));
            }

            // Refresh markers if shown
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, instance);

        }, ArgumentType.Literal("scan"));
    }

    private void registerBoundsCommand(MinestomCommand command) {
        var cornerArg = ArgumentType.String("corner");
        cornerArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("min"));
            suggestion.addEntry(new SuggestionEntry("max"));
        });

        // /autosetup bounds <corner> - use player position
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String corner = context.get(cornerArg);
            Pos pos = player.getPosition();

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());

            if (corner.equalsIgnoreCase("min")) {
                session.setBoundsMin(pos.x(), pos.y(), pos.z());
                player.sendMessage(Component.text("§aSet bounds minimum to " + formatPos(pos)));
            } else if (corner.equalsIgnoreCase("max")) {
                session.setBoundsMax(pos.x(), pos.y(), pos.z());
                player.sendMessage(Component.text("§aSet bounds maximum to " + formatPos(pos)));
            } else {
                player.sendMessage(Component.text("§cInvalid corner. Use 'min' or 'max'."));
            }

            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("bounds"), cornerArg);

        // /autosetup bounds <corner> <x> <y> <z>
        ArgumentDouble xArg = ArgumentType.Double("x");
        ArgumentDouble yArg = ArgumentType.Double("y");
        ArgumentDouble zArg = ArgumentType.Double("z");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String corner = context.get(cornerArg);
            double x = context.get(xArg);
            double y = context.get(yArg);
            double z = context.get(zArg);

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());

            if (corner.equalsIgnoreCase("min")) {
                session.setBoundsMin(x, y, z);
                player.sendMessage(Component.text("§aSet bounds minimum to " + x + ", " + y + ", " + z));
            } else if (corner.equalsIgnoreCase("max")) {
                session.setBoundsMax(x, y, z);
                player.sendMessage(Component.text("§aSet bounds maximum to " + x + ", " + y + ", " + z));
            }

            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("bounds"), cornerArg, xArg, yArg, zArg);
    }

    private void registerTypeCommand(MinestomCommand command) {
        var actionArg = ArgumentType.String("action");
        actionArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("add"));
            suggestion.addEntry(new SuggestionEntry("remove"));
        });

        var typeArg = ArgumentType.String("typeName");
        typeArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            for (BedwarsGameType type : BedwarsGameType.values()) {
                suggestion.addEntry(new SuggestionEntry(type.name()));
            }
        });

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String action = context.get(actionArg);
            String typeName = context.get(typeArg);

            BedwarsGameType gameType = BedwarsGameType.from(typeName);
            if (gameType == null) {
                player.sendMessage(Component.text("§cInvalid game type: " + typeName));
                return;
            }

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());

            if (action.equalsIgnoreCase("add")) {
                if (!session.getGameTypes().contains(gameType)) {
                    session.getGameTypes().add(gameType);
                    player.sendMessage(Component.text("§aAdded game type: " + gameType.getDisplayName()));
                } else {
                    player.sendMessage(Component.text("§eGame type already added: " + gameType.getDisplayName()));
                }
            } else if (action.equalsIgnoreCase("remove")) {
                if (session.getGameTypes().remove(gameType)) {
                    player.sendMessage(Component.text("§cRemoved game type: " + gameType.getDisplayName()));
                } else {
                    player.sendMessage(Component.text("§eGame type not in list: " + gameType.getDisplayName()));
                }
            }

        }, ArgumentType.Literal("type"), actionArg, typeArg);
    }

    private void registerTeamCommand(MinestomCommand command) {
        var teamArg = ArgumentType.String("teamName");
        teamArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            for (TeamKey team : TeamKey.values()) {
                suggestion.addEntry(new SuggestionEntry(team.name()));
            }
        });

        var propertyArg = ArgumentType.String("property");
        propertyArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("spawn"));
            suggestion.addEntry(new SuggestionEntry("bed"));
            suggestion.addEntry(new SuggestionEntry("generator"));
            suggestion.addEntry(new SuggestionEntry("itemshop"));
            suggestion.addEntry(new SuggestionEntry("teamshop"));
        });

        // /autosetup team <team> <property> - use player position
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String teamName = context.get(teamArg);
            String property = context.get(propertyArg);

            TeamKey teamKey;
            try {
                teamKey = TeamKey.valueOf(teamName.toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("§cInvalid team: " + teamName));
                return;
            }

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            AutoSetupSession.TeamConfig teamConfig = session.getOrCreateTeam(teamKey);
            Pos pos = player.getPosition();

            setTeamProperty(player, teamConfig, property, pos);
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("team"), teamArg, propertyArg);

        // /autosetup team <team> <property> <x> <y> <z>
        ArgumentDouble xArg = ArgumentType.Double("x");
        ArgumentDouble yArg = ArgumentType.Double("y");
        ArgumentDouble zArg = ArgumentType.Double("z");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String teamName = context.get(teamArg);
            String property = context.get(propertyArg);
            double x = context.get(xArg);
            double y = context.get(yArg);
            double z = context.get(zArg);

            TeamKey teamKey;
            try {
                teamKey = TeamKey.valueOf(teamName.toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("§cInvalid team: " + teamName));
                return;
            }

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            AutoSetupSession.TeamConfig teamConfig = session.getOrCreateTeam(teamKey);
            Pos pos = new Pos(x, y, z, player.getPosition().yaw(), player.getPosition().pitch());

            setTeamProperty(player, teamConfig, property, pos);
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("team"), teamArg, propertyArg, xArg, yArg, zArg);
    }

    private void setTeamProperty(Player player, AutoSetupSession.TeamConfig teamConfig, String property, Pos pos) {
        switch (property.toLowerCase()) {
            case "spawn" -> {
                teamConfig.setSpawn(new PitchYawPosition(pos.x(), pos.y(), pos.z(), pos.pitch(), pos.yaw()));
                player.sendMessage(Component.text("§aSet team spawn to " + formatPos(pos)));
            }
            case "bed" -> {
                // For bed, we need both feet and head. Use player facing direction
                Position feet = new Position(pos.blockX(), pos.blockY(), pos.blockZ());
                Position head = calculateBedHead(pos);
                teamConfig.setBedFeet(feet);
                teamConfig.setBedHead(head);
                player.sendMessage(Component.text("§aSet team bed (feet: " + formatPosition(feet) + ", head: " + formatPosition(head) + ")"));
            }
            case "generator" -> {
                teamConfig.setGenerator(new Position(pos.x(), pos.y(), pos.z()));
                player.sendMessage(Component.text("§aSet team generator to " + formatPos(pos)));
            }
            case "itemshop" -> {
                teamConfig.setItemShop(new PitchYawPosition(pos.x(), pos.y(), pos.z(), pos.pitch(), pos.yaw()));
                player.sendMessage(Component.text("§aSet item shop to " + formatPos(pos)));
            }
            case "teamshop" -> {
                teamConfig.setTeamShop(new PitchYawPosition(pos.x(), pos.y(), pos.z(), pos.pitch(), pos.yaw()));
                player.sendMessage(Component.text("§aSet team shop to " + formatPos(pos)));
            }
            default -> player.sendMessage(Component.text("§cUnknown property: " + property));
        }
    }

    private Position calculateBedHead(Pos playerPos) {
        // Calculate head position based on player yaw (where they're looking)
        float yaw = playerPos.yaw();
        int dx = 0, dz = 0;

        if (yaw >= -45 && yaw < 45) { // South
            dz = 1;
        } else if (yaw >= 45 && yaw < 135) { // West
            dx = -1;
        } else if (yaw >= 135 || yaw < -135) { // North
            dz = -1;
        } else { // East
            dx = 1;
        }

        return new Position(playerPos.blockX() + dx, playerPos.blockY(), playerPos.blockZ() + dz);
    }

    private void registerGlobalCommand(MinestomCommand command) {
        var genTypeArg = ArgumentType.String("gentype");
        genTypeArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("diamond"));
            suggestion.addEntry(new SuggestionEntry("emerald"));
        });

        var actionArg = ArgumentType.String("action");
        actionArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("add"));
            suggestion.addEntry(new SuggestionEntry("remove"));
            suggestion.addEntry(new SuggestionEntry("clear"));
        });

        // /autosetup global <type> <action> - use player position
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String genType = context.get(genTypeArg);
            String action = context.get(actionArg);
            Pos pos = player.getPosition();

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            List<Position> generators = genType.equalsIgnoreCase("diamond")
                    ? session.getDiamondGenerators()
                    : session.getEmeraldGenerators();

            handleGlobalGeneratorAction(player, generators, action, pos, genType);
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("global"), genTypeArg, actionArg);

        // /autosetup global <type> <action> <x> <y> <z>
        ArgumentDouble xArg = ArgumentType.Double("x");
        ArgumentDouble yArg = ArgumentType.Double("y");
        ArgumentDouble zArg = ArgumentType.Double("z");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String genType = context.get(genTypeArg);
            String action = context.get(actionArg);
            double x = context.get(xArg);
            double y = context.get(yArg);
            double z = context.get(zArg);

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            List<Position> generators = genType.equalsIgnoreCase("diamond")
                    ? session.getDiamondGenerators()
                    : session.getEmeraldGenerators();

            handleGlobalGeneratorAction(player, generators, action, new Pos(x, y, z), genType);
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("global"), genTypeArg, actionArg, xArg, yArg, zArg);
    }

    private void handleGlobalGeneratorAction(Player player, List<Position> generators, String action, Pos pos, String genType) {
        switch (action.toLowerCase()) {
            case "add" -> {
                Position newPos = new Position(pos.x(), pos.y(), pos.z());
                generators.add(newPos);
                player.sendMessage(Component.text("§aAdded " + genType + " generator at " + formatPos(pos) + " (Total: " + generators.size() + ")"));
            }
            case "remove" -> {
                // Remove nearest generator within 2 blocks
                Position toRemove = null;
                double minDist = Double.MAX_VALUE;

                for (Position gen : generators) {
                    double dist = Math.sqrt(Math.pow(gen.x() - pos.x(), 2) + Math.pow(gen.y() - pos.y(), 2) + Math.pow(gen.z() - pos.z(), 2));
                    if (dist < minDist && dist < 2) {
                        minDist = dist;
                        toRemove = gen;
                    }
                }

                if (toRemove != null) {
                    generators.remove(toRemove);
                    player.sendMessage(Component.text("§cRemoved nearest " + genType + " generator (Total: " + generators.size() + ")"));
                } else {
                    player.sendMessage(Component.text("§cNo " + genType + " generator found within 2 blocks"));
                }
            }
            case "clear" -> {
                int count = generators.size();
                generators.clear();
                player.sendMessage(Component.text("§cCleared all " + count + " " + genType + " generators"));
            }
            default -> player.sendMessage(Component.text("§cUnknown action: " + action));
        }
    }

    private void registerLocationCommand(MinestomCommand command) {
        // /autosetup waiting [x y z]
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            Pos pos = player.getPosition();
            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            session.setWaitingLocation(new PitchYawPosition(pos.x(), pos.y(), pos.z(), pos.pitch(), pos.yaw()));
            player.sendMessage(Component.text("§aSet waiting spawn to " + formatPos(pos)));
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("waiting"));

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            Pos pos = player.getPosition();
            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            session.setSpectatorLocation(new PitchYawPosition(pos.x(), pos.y(), pos.z(), pos.pitch(), pos.yaw()));
            player.sendMessage(Component.text("§aSet spectator spawn to " + formatPos(pos)));
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("spectator"));

        // With coordinates
        ArgumentDouble xArg = ArgumentType.Double("x");
        ArgumentDouble yArg = ArgumentType.Double("y");
        ArgumentDouble zArg = ArgumentType.Double("z");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            double x = context.get(xArg);
            double y = context.get(yArg);
            double z = context.get(zArg);

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            session.setWaitingLocation(new PitchYawPosition(x, y, z, 0, 0));
            player.sendMessage(Component.text("§aSet waiting spawn to " + x + ", " + y + ", " + z));
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("waiting"), xArg, yArg, zArg);

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            double x = context.get(xArg);
            double y = context.get(yArg);
            double z = context.get(zArg);

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());
            session.setSpectatorLocation(new PitchYawPosition(x, y, z, 0, 0));
            player.sendMessage(Component.text("§aSet spectator spawn to " + x + ", " + y + ", " + z));
            DebugMarkerManager.refreshMarkers(player.getUuid(), session, player.getInstance());

        }, ArgumentType.Literal("spectator"), xArg, yArg, zArg);
    }

    private void registerShowCommand(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            AutoSetupSession session = AutoSetupSession.get(player.getUuid());
            if (session == null) {
                player.sendMessage(Component.text("§cNo configuration session active. Use /autosetup scan or set bounds first."));
                return;
            }

            DebugMarkerManager.showMarkers(player.getUuid(), session, player.getInstance());
            player.sendMessage(Component.text("§aShowing debug markers"));

        }, ArgumentType.Literal("show"));
    }

    private void registerHideCommand(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            DebugMarkerManager.hideMarkers(player.getUuid());
            player.sendMessage(Component.text("§cHidden debug markers"));

        }, ArgumentType.Literal("hide"));
    }

    private void registerStatusCommand(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            AutoSetupSession session = AutoSetupSession.get(player.getUuid());
            if (session == null) {
                player.sendMessage(Component.text("§cNo configuration session active."));
                return;
            }

            player.sendMessage(Component.text("§6§l=== Configuration Status ==="));
            player.sendMessage(Component.text("§eMap ID: §f" + (session.getMapId() != null ? session.getMapId() : "§c(not set)")));
            player.sendMessage(Component.text("§eMap Name: §f" + (session.getMapName() != null ? session.getMapName() : "§c(not set)")));
            player.sendMessage(Component.text("§eBounds: §f" + (session.hasBounds() ? "✔ Set" : "§c✖ Not set")));
            player.sendMessage(Component.text("§eGame Types: §f" + (session.getGameTypes().isEmpty() ? "§c(none)" : session.getGameTypes().toString())));
            player.sendMessage(Component.text("§eTeams Configured: §f" + session.getTeams().size()));

            for (var entry : session.getTeams().entrySet()) {
                TeamKey team = entry.getKey();
                AutoSetupSession.TeamConfig config = entry.getValue();
                String status = team.chatColor() + team.getName() + "§7: ";
                status += (config.getSpawn() != null ? "§aS" : "§cS") + " ";
                status += (config.getBedFeet() != null ? "§aB" : "§cB") + " ";
                status += (config.getGenerator() != null ? "§aG" : "§cG") + " ";
                status += (config.getItemShop() != null ? "§aIS" : "§cIS") + " ";
                status += (config.getTeamShop() != null ? "§aTS" : "§cTS");
                player.sendMessage(Component.text("  " + status));
            }

            player.sendMessage(Component.text("§eDiamond Generators: §f" + session.getDiamondGenerators().size()));
            player.sendMessage(Component.text("§eEmerald Generators: §f" + session.getEmeraldGenerators().size()));
            player.sendMessage(Component.text("§eWaiting Location: §f" + (session.getWaitingLocation() != null ? "✔" : "§c✖")));
            player.sendMessage(Component.text("§eSpectator Location: §f" + (session.getSpectatorLocation() != null ? "✔" : "§c✖")));

        }, ArgumentType.Literal("status"));
    }

    private void registerMapInfoCommand(MinestomCommand command) {
        ArgumentString nameArg = ArgumentType.String("name");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            AutoSetupSession session = AutoSetupSession.get(player.getUuid());
            if (session == null || session.getMapId() == null) {
                player.sendMessage(Component.text("§cNo map selected. Use /choosemap <map> first."));
                return;
            }

            String name = context.get(nameArg);
            session.setMapName(name);

            player.sendMessage(Component.text("§aSet map name to '" + name + "' (ID: " + session.getMapId() + ")"));

        }, ArgumentType.Literal("name"), nameArg);
    }

    private void registerGeneratorSettingsCommand(MinestomCommand command) {
        // Speed setting command
        var speedArg = ArgumentType.String("speed");
        speedArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("SLOW"));
            suggestion.addEntry(new SuggestionEntry("MEDIUM"));
            suggestion.addEntry(new SuggestionEntry("FAST"));
            suggestion.addEntry(new SuggestionEntry("SUPER_FAST"));
        });

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String speedStr = context.get(speedArg);
            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());

            try {
                GeneratorSpeed speed = GeneratorSpeed.valueOf(speedStr.toUpperCase());
                session.setGeneratorSpeed(speed);
                player.sendMessage(Component.text("§aSet generator speed to " + speed.name() +
                    " (" + speed.getIronAmount() + " iron/" + speed.getIronDelaySeconds() + "s, " +
                    speed.getGoldAmount() + " gold/" + speed.getGoldDelaySeconds() + "s)"));
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("§cInvalid speed: " + speedStr));
            }

        }, ArgumentType.Literal("generator"), ArgumentType.Literal("speed"), speedArg);

        // Diamond/Emerald settings (unchanged)
        var genTypeArg = ArgumentType.String("gentype");
        genTypeArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("diamond"));
            suggestion.addEntry(new SuggestionEntry("emerald"));
        });

        var settingArg = ArgumentType.String("setting");
        settingArg.setSuggestionCallback((sender, ctx, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("amount"));
            suggestion.addEntry(new SuggestionEntry("max"));
        });

        ArgumentDouble valueArg = ArgumentType.Double("value");

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            String genType = context.get(genTypeArg);
            String setting = context.get(settingArg);
            int value = context.get(valueArg).intValue();

            AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), player.getInstance());

            switch (genType.toLowerCase()) {
                case "diamond" -> {
                    if (setting.equals("amount")) session.setDiamondAmount(value);
                    else if (setting.equals("max")) session.setDiamondMax(value);
                }
                case "emerald" -> {
                    if (setting.equals("amount")) session.setEmeraldAmount(value);
                    else if (setting.equals("max")) session.setEmeraldMax(value);
                }
            }

            player.sendMessage(Component.text("§aSet " + genType + " " + setting + " to " + value));

        }, ArgumentType.Literal("generator"), genTypeArg, settingArg, valueArg);
    }

    private void registerSaveCommand(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            if (!permissionCheck(sender)) return;

            AutoSetupSession session = AutoSetupSession.get(player.getUuid());
            if (session == null) {
                player.sendMessage(Component.text("§cNo configuration session active."));
                return;
            }

            // Validate required fields
            List<String> errors = validateSession(session);
            if (!errors.isEmpty()) {
                player.sendMessage(Component.text("§cCannot save - missing required configuration:"));
                for (String error : errors) {
                    player.sendMessage(Component.text("§c  • " + error));
                }
                return;
            }

            try {
                saveToConfig(session);
                player.sendMessage(Component.text("§a✔ Configuration saved to maps.json!"));
                player.sendMessage(Component.text("§7Map ID: " + session.getMapId()));
            } catch (Exception e) {
                player.sendMessage(Component.text("§cFailed to save: " + e.getMessage()));
                Logger.error("Failed to save map configuration", e);
            }

        }, ArgumentType.Literal("save"));
    }

    private List<String> validateSession(AutoSetupSession session) {
        List<String> errors = new ArrayList<>();

        if (session.getMapId() == null || session.getMapId().isEmpty()) {
            errors.add("No map selected (use /choosemap <map> first)");
        }
        if (session.getMapName() == null || session.getMapName().isEmpty()) {
            errors.add("Map name not set (use /autosetup name <name>)");
        }
        if (!session.hasBounds()) {
            errors.add("Bounds not set");
        }
        if (session.getGameTypes().isEmpty()) {
            errors.add("No game types set");
        }
        if (session.getTeams().isEmpty()) {
            errors.add("No teams configured");
        }
        if (session.getWaitingLocation() == null) {
            errors.add("Waiting location not set");
        }
        if (session.getSpectatorLocation() == null) {
            errors.add("Spectator location not set");
        }

        for (var entry : session.getTeams().entrySet()) {
            AutoSetupSession.TeamConfig config = entry.getValue();
            String teamName = entry.getKey().getName();
            if (config.getSpawn() == null) {
                errors.add(teamName + " team: spawn not set");
            }
            if (config.getBedFeet() == null || config.getBedHead() == null) {
                errors.add(teamName + " team: bed not set");
            }
            if (config.getGenerator() == null) {
                errors.add(teamName + " team: generator not set");
            }
        }

        return errors;
    }

    private void saveToConfig(AutoSetupSession session) throws IOException {
        Path mapsPath = Path.of("./configuration/bedwars/maps.json");

        BedWarsMapsConfig config;
        if (Files.exists(mapsPath)) {
            String json = Files.readString(mapsPath, StandardCharsets.UTF_8);
            config = GSON.fromJson(json, BedWarsMapsConfig.class);
        } else {
            config = new BedWarsMapsConfig();
            config.setMaps(new ArrayList<>());
        }

        // Remove existing entry with same ID
        config.getMaps().removeIf(entry -> entry.getId().equals(session.getMapId()));

        // Add new entry
        config.getMaps().add(session.toMapEntry());

        // Write back
        String output = GSON.toJson(config);
        Files.writeString(mapsPath, output, StandardCharsets.UTF_8);

        // Reload config in memory
        TypeBedWarsConfiguratorLoader.reloadMapsConfig();
    }

    private String formatPos(Pos pos) {
        return String.format("%.2f, %.2f, %.2f", pos.x(), pos.y(), pos.z());
    }

    private String formatPosition(Position pos) {
        return String.format("%.2f, %.2f, %.2f", pos.x(), pos.y(), pos.z());
    }
}

