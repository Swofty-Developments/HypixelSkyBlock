package net.swofty.type.zombiesconfigurator.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.zombies.ZombiesMap;
import net.swofty.commons.zombies.map.ZombiesMapsConfig;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.zombiesconfigurator.MapConfigurationSession;
import net.swofty.type.zombiesconfigurator.TypeZombiesConfiguratorLoader;

import java.util.Arrays;
import java.util.stream.Collectors;

@CommandParameters(
        aliases = "zombiesconfig",
        description = "Configure Zombies maps",
        usage = "/zombiesconfig <subcommand>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class ZombiesConfigCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        // Default - show usage
        command.setDefaultExecutor((sender, context) -> {
            sender.sendMessage("§e=== Zombies Map Configuration ===");
            sender.sendMessage("§7/zombies new <id> <name> §f- Start new session");
            sender.sendMessage("§7/zombies setMap <mapType> §f- Set map type");
            sender.sendMessage("§7/zombies setSpawn §f- Set spawn position");
            sender.sendMessage("§7/zombies setSpectator §f- Set spectator position");
            sender.sendMessage("§7/zombies setPracticeZombie §f- Set practice zombie position");
            sender.sendMessage("§7/zombies setBounds <minX> <minY> <minZ> <maxX> <maxY> <maxZ> §f- Set map bounds");
            sender.sendMessage("§7/zombies addDefaultBossSpawn <Name> §f- Add default boss spawn");
            sender.sendMessage("§7/zombies addBossSpawn <ZombieType> <Name> §f- Add boss spawn");
            sender.sendMessage("§7/zombies createArea <1/2> §f- Create area positions");
            sender.sendMessage("§7/zombies saveBlockWindow <WindowName> <Name> §f- Save window blocks");
            sender.sendMessage("§7/zombies saveZombieWindowArea <WindowName> <Name> §f- Save zombie window area");
            sender.sendMessage("§7/zombies savePlayerWindowArea <WindowName> <Name> §f- Save player window area");
            sender.sendMessage("§7/zombies saveDoor <Gold> <DoorName> <Name> §f- Save door");
            sender.sendMessage("§7/zombies saveFakeDoor <Gold> <DoorName> <FakeDoorName> <Name> §f- Save fake door");
            sender.sendMessage("§7/zombies toggleDoor <DoorName> <RoundId> <Name> §f- Toggle door round");
            sender.sendMessage("§7/zombies togglePowerSwitchDoor <DoorName> <Name> §f- Toggle power switch door");
            sender.sendMessage("§7/zombies connectDoors <DoorName> <DoorName,...> <Name> §f- Connect doors");
            sender.sendMessage("§7/zombies addDefaultSpawner <SpawnerName> <Name> §f- Add default spawner");
            sender.sendMessage("§7/zombies addSpawner <SpawnerName> <DoorName> <Name> §f- Add spawner");
            sender.sendMessage("§7/zombies connectSpawner <SpawnerName> <WindowName> <Name> §f- Connect spawner");
            sender.sendMessage("§7/zombies saveArmorPackShop <Gold> <Leather|Gold|Iron|Diamond> <Up|Down> <Name> §f- Save armor shop");
            sender.sendMessage("§7/zombies saveWeaponShop <WeaponGold> <AmmoGold> <WeaponName> <Name> §f- Save weapon shop");
            sender.sendMessage("§7/zombies savePerkShop <PerkGold> <PerkName> <Name> §f- Save perk shop");
            sender.sendMessage("§7/zombies savePowerSwitch <PowerSwitchGold> <Name> §f- Save power switch");
            sender.sendMessage("§7/zombies addWeaponsChest <Name> §f- Add weapons chest");
            sender.sendMessage("§7/zombies addTeamMachine <Name> §f- Add team machine");
            sender.sendMessage("§7/zombies saveUltimateMachine <Gold> <Name> §f- Save ultimate machine");
            sender.sendMessage("§7/zombies saveStatsHologram <Name> §f- Save stats hologram");
            sender.sendMessage("§7/zombies save §f- Save configuration to file");
            sender.sendMessage("§7/zombies status §f- Show current status");
        });

        // /zombies new <id> <name>
        var newLit = ArgumentType.Literal("new");
        var idArg = ArgumentType.String("id");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String id = context.get(idArg);
            String name = context.get(nameArg);
            MapConfigurationSession session = new MapConfigurationSession(id, name);
            TypeZombiesConfiguratorLoader.setCurrentSession(session);
            sender.sendMessage("§aStarted new configuration session for map: " + name + " (id: " + id + ")");
        }, newLit, idArg, nameArg);

        // /zombies setMap <mapType>
        var setMapLit = ArgumentType.Literal("setMap");
        var mapTypeArg = ArgumentType.String("mapType");
        mapTypeArg.setSuggestionCallback((sender, context, suggestion) -> {
            for (ZombiesMap map : ZombiesMap.values()) {
                suggestion.addEntry(new SuggestionEntry(map.name()));
            }
        });
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session! Use /zombies new <id> <name> first.");
                return;
            }
            String mapTypeName = context.get(mapTypeArg);
            try {
                ZombiesMap mapType = ZombiesMap.valueOf(mapTypeName.toUpperCase());
                session.setMapType(mapType);
                sender.sendMessage("§aSet map type to: " + mapType.name());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid map type! Available: " +
                    Arrays.stream(ZombiesMap.values()).map(Enum::name).collect(Collectors.joining(", ")));
            }
        }, setMapLit, mapTypeArg);

        // /zombies setSpawn
        var setSpawnLit = ArgumentType.Literal("setSpawn");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.setSpawnPosition(player.getPosition());
            sender.sendMessage("§aSet spawn position to " + formatPos(player.getPosition()));
        }, setSpawnLit);

        // /zombies setSpectator
        var setSpectatorLit = ArgumentType.Literal("setSpectator");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.setSpectatorPosition(player.getPosition());
            sender.sendMessage("§aSet spectator position to " + formatPos(player.getPosition()));
        }, setSpectatorLit);

        // /zombies setPracticeZombie
        var setPracticeZombieLit = ArgumentType.Literal("setPracticeZombie");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.setPracticeZombiePosition(player.getPosition());
            sender.sendMessage("§aSet practice zombie position to " + formatPos(player.getPosition()));
        }, setPracticeZombieLit);

        // /zombies setBounds <minX> <minY> <minZ> <maxX> <maxY> <maxZ>
        var setBoundsLit = ArgumentType.Literal("setBounds");
        var minXArg = ArgumentType.Double("minX");
        var minYArg = ArgumentType.Double("minY");
        var minZArg = ArgumentType.Double("minZ");
        var maxXArg = ArgumentType.Double("maxX");
        var maxYArg = ArgumentType.Double("maxY");
        var maxZArg = ArgumentType.Double("maxZ");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.setBoundsMinX(context.get(minXArg));
            session.setBoundsMinY(context.get(minYArg));
            session.setBoundsMinZ(context.get(minZArg));
            session.setBoundsMaxX(context.get(maxXArg));
            session.setBoundsMaxY(context.get(maxYArg));
            session.setBoundsMaxZ(context.get(maxZArg));
            sender.sendMessage("§aSet bounds successfully!");
        }, setBoundsLit, minXArg, minYArg, minZArg, maxXArg, maxYArg, maxZArg);

        // /zombies addDefaultBossSpawn <Name>
        var addDefaultBossSpawnLit = ArgumentType.Literal("addDefaultBossSpawn");
        var bossNameArg = ArgumentType.String("bossName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String bossName = context.get(bossNameArg);
            MapConfigurationSession.BossSpawnData bossSpawn = new MapConfigurationSession.BossSpawnData();
            bossSpawn.setName(bossName);
            bossSpawn.setLocation(player.getPosition());
            bossSpawn.setDefault(true);
            session.getBossSpawns().put(bossName, bossSpawn);
            sender.sendMessage("§aAdded default boss spawn: " + bossName);
        }, addDefaultBossSpawnLit, bossNameArg);

        // /zombies addBossSpawn <ZombieType> <Name>
        var addBossSpawnLit = ArgumentType.Literal("addBossSpawn");
        var zombieTypeArg = ArgumentType.String("zombieType");
        var bossSpawnNameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String zombieType = context.get(zombieTypeArg);
            String name = context.get(bossSpawnNameArg);
            MapConfigurationSession.BossSpawnData bossSpawn = new MapConfigurationSession.BossSpawnData();
            bossSpawn.setName(name);
            bossSpawn.setLocation(player.getPosition());
            bossSpawn.setZombieType(zombieType);
            bossSpawn.setDefault(false);
            session.getBossSpawns().put(name, bossSpawn);
            sender.sendMessage("§aAdded boss spawn: " + name + " (" + zombieType + ")");
        }, addBossSpawnLit, zombieTypeArg, bossSpawnNameArg);

        registerWindowCommands(command);
        registerDoorCommands(command);
        registerSpawnerCommands(command);
        registerShopCommands(command);
        registerMachineCommands(command);
        registerMiscCommands(command);
    }

    private void registerWindowCommands(MinestomCommand command) {
        // /zombies saveBlockWindow <WindowName> <Name>
        var saveBlockWindowLit = ArgumentType.Literal("saveBlockWindow");
        var windowNameArg = ArgumentType.String("windowName");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String windowName = context.get(windowNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.WindowData window = session.getWindows()
                .computeIfAbsent(name, k -> new MapConfigurationSession.WindowData());
            window.setName(windowName);
            window.getBlockPositions().add(player.getPosition());
            sender.sendMessage("§aAdded block position to window: " + name + " (" + window.getBlockPositions().size() + " blocks)");
        }, saveBlockWindowLit, windowNameArg, nameArg);

        // /zombies saveZombieWindowArea <WindowName> <Name>
        var saveZombieWindowAreaLit = ArgumentType.Literal("saveZombieWindowArea");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String windowName = context.get(windowNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.WindowData window = session.getWindows()
                .computeIfAbsent(name, k -> new MapConfigurationSession.WindowData());
            window.setName(windowName);

            if (window.getZombieAreaPos1() == null) {
                window.setZombieAreaPos1(player.getPosition());
                sender.sendMessage("§aSet zombie window area position 1 for: " + name);
            } else {
                window.setZombieAreaPos2(player.getPosition());
                sender.sendMessage("§aSet zombie window area position 2 for: " + name);
            }
        }, saveZombieWindowAreaLit, windowNameArg, nameArg);

        // /zombies savePlayerWindowArea <WindowName> <Name>
        var savePlayerWindowAreaLit = ArgumentType.Literal("savePlayerWindowArea");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String windowName = context.get(windowNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.WindowData window = session.getWindows()
                .computeIfAbsent(name, k -> new MapConfigurationSession.WindowData());
            window.setName(windowName);

            if (window.getPlayerAreaPos1() == null) {
                window.setPlayerAreaPos1(player.getPosition());
                sender.sendMessage("§aSet player window area position 1 for: " + name);
            } else {
                window.setPlayerAreaPos2(player.getPosition());
                sender.sendMessage("§aSet player window area position 2 for: " + name);
            }
        }, savePlayerWindowAreaLit, windowNameArg, nameArg);
    }

    private void registerDoorCommands(MinestomCommand command) {
        // /zombies saveDoor <Gold> <DoorName> <Name>
        var saveDoorLit = ArgumentType.Literal("saveDoor");
        var goldArg = ArgumentType.Integer("gold");
        var doorNameArg = ArgumentType.String("doorName");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int gold = context.get(goldArg);
            String doorName = context.get(doorNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.DoorData door = session.getDoors()
                .computeIfAbsent(name, k -> new MapConfigurationSession.DoorData());
            door.setName(doorName);
            door.setGoldCost(gold);

            if (door.getAreaPos1() == null) {
                door.setAreaPos1(player.getPosition());
                sender.sendMessage("§aSet door area position 1 for: " + name);
            } else {
                door.setAreaPos2(player.getPosition());
                sender.sendMessage("§aSet door area position 2 for: " + name + " (Complete)");
            }
        }, saveDoorLit, goldArg, doorNameArg, nameArg);

        // /zombies saveFakeDoor <Gold> <DoorName> <FakeDoorName> <Name>
        var saveFakeDoorLit = ArgumentType.Literal("saveFakeDoor");
        var fakeDoorNameArg = ArgumentType.String("fakeDoorName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int gold = context.get(goldArg);
            String doorName = context.get(doorNameArg);
            String fakeDoorName = context.get(fakeDoorNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.DoorData door = session.getDoors()
                .computeIfAbsent(name, k -> new MapConfigurationSession.DoorData());
            door.setName(doorName);
            door.setGoldCost(gold);
            door.setFakeDoorName(fakeDoorName);

            if (door.getAreaPos1() == null) {
                door.setAreaPos1(player.getPosition());
                sender.sendMessage("§aSet fake door area position 1 for: " + name);
            } else {
                door.setAreaPos2(player.getPosition());
                sender.sendMessage("§aSet fake door area position 2 for: " + name + " (Complete)");
            }
        }, saveFakeDoorLit, goldArg, doorNameArg, fakeDoorNameArg, nameArg);

        // /zombies toggleDoor <DoorName> <RoundId> <Name>
        var toggleDoorLit = ArgumentType.Literal("toggleDoor");
        var roundIdArg = ArgumentType.Integer("roundId");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String doorName = context.get(doorNameArg);
            int roundId = context.get(roundIdArg);
            String name = context.get(nameArg);

            MapConfigurationSession.DoorData door = session.getDoors().get(name);
            if (door == null) {
                sender.sendMessage("§cDoor not found: " + name);
                return;
            }
            door.setOpenOnRound(roundId);
            sender.sendMessage("§aSet door " + name + " to open on round: " + roundId);
        }, toggleDoorLit, doorNameArg, roundIdArg, nameArg);

        // /zombies togglePowerSwitchDoor <DoorName> <Name>
        var togglePowerSwitchDoorLit = ArgumentType.Literal("togglePowerSwitchDoor");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String doorName = context.get(doorNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.DoorData door = session.getDoors().get(name);
            if (door == null) {
                sender.sendMessage("§cDoor not found: " + name);
                return;
            }
            door.setPowerSwitchDoor(!door.isPowerSwitchDoor());
            sender.sendMessage("§aToggled power switch door for " + name + ": " + door.isPowerSwitchDoor());
        }, togglePowerSwitchDoorLit, doorNameArg, nameArg);

        // /zombies connectDoors <DoorName> <DoorName,...> <Name>
        var connectDoorsLit = ArgumentType.Literal("connectDoors");
        var connectedDoorsArg = ArgumentType.String("connectedDoors");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String doorName = context.get(doorNameArg);
            String connectedDoorsStr = context.get(connectedDoorsArg);
            String name = context.get(nameArg);

            MapConfigurationSession.DoorData door = session.getDoors().get(name);
            if (door == null) {
                sender.sendMessage("§cDoor not found: " + name);
                return;
            }
            door.setConnectedDoors(Arrays.asList(connectedDoorsStr.split(",")));
            sender.sendMessage("§aConnected doors for " + name + ": " + connectedDoorsStr);
        }, connectDoorsLit, doorNameArg, connectedDoorsArg, nameArg);
    }

    private void registerSpawnerCommands(MinestomCommand command) {
        // /zombies addDefaultSpawner <SpawnerName> <Name>
        var addDefaultSpawnerLit = ArgumentType.Literal("addDefaultSpawner");
        var spawnerNameArg = ArgumentType.String("spawnerName");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String spawnerName = context.get(spawnerNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.SpawnerData spawner = new MapConfigurationSession.SpawnerData();
            spawner.setName(spawnerName);
            spawner.setLocation(player.getPosition());
            spawner.setDefault(true);
            session.getSpawners().put(name, spawner);
            sender.sendMessage("§aAdded default spawner: " + name);
        }, addDefaultSpawnerLit, spawnerNameArg, nameArg);

        // /zombies addSpawner <SpawnerName> <DoorName> <Name>
        var addSpawnerLit = ArgumentType.Literal("addSpawner");
        var doorNameArg = ArgumentType.String("doorName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String spawnerName = context.get(spawnerNameArg);
            String doorName = context.get(doorNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.SpawnerData spawner = new MapConfigurationSession.SpawnerData();
            spawner.setName(spawnerName);
            spawner.setLocation(player.getPosition());
            spawner.setRequiredDoor(doorName);
            spawner.setDefault(false);
            session.getSpawners().put(name, spawner);
            sender.sendMessage("§aAdded spawner: " + name + " (requires door: " + doorName + ")");
        }, addSpawnerLit, spawnerNameArg, doorNameArg, nameArg);

        // /zombies connectSpawner <SpawnerName> <WindowName> <Name>
        var connectSpawnerLit = ArgumentType.Literal("connectSpawner");
        var windowNameArg = ArgumentType.String("windowName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String spawnerName = context.get(spawnerNameArg);
            String windowName = context.get(windowNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.SpawnerData spawner = session.getSpawners().get(name);
            if (spawner == null) {
                sender.sendMessage("§cSpawner not found: " + name);
                return;
            }
            spawner.getConnectedWindows().add(windowName);
            sender.sendMessage("§aConnected spawner " + name + " to window: " + windowName);
        }, connectSpawnerLit, spawnerNameArg, windowNameArg, nameArg);
    }

    private void registerShopCommands(MinestomCommand command) {
        // /zombies saveArmorPackShop <Gold> <Leather|Gold|Iron|Diamond> <Up|Down> <Name>
        var saveArmorPackShopLit = ArgumentType.Literal("saveArmorPackShop");
        var goldArg = ArgumentType.Integer("gold");
        var armorTypeArg = ArgumentType.String("armorType");
        var armorPartArg = ArgumentType.String("armorPart");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int gold = context.get(goldArg);
            String armorType = context.get(armorTypeArg).toUpperCase();
            String armorPart = context.get(armorPartArg).toUpperCase();
            String name = context.get(nameArg);

            MapConfigurationSession.ShopData shop = new MapConfigurationSession.ShopData();
            shop.setType(ZombiesMapsConfig.ShopType.ARMOR);
            shop.setLocation(player.getPosition());
            shop.setGoldCost(gold);

            MapConfigurationSession.ArmorShopData armorConfig = new MapConfigurationSession.ArmorShopData();
            armorConfig.setArmorType(armorType);
            armorConfig.setArmorPart(armorPart);
            shop.setArmorConfig(armorConfig);

            session.getShops().put(name, shop);
            sender.sendMessage("§aAdded armor shop: " + name + " (" + armorType + " " + armorPart + ")");
        }, saveArmorPackShopLit, goldArg, armorTypeArg, armorPartArg, nameArg);

        // /zombies saveWeaponShop <WeaponGold> <AmmoGold> <WeaponName> <Name>
        var saveWeaponShopLit = ArgumentType.Literal("saveWeaponShop");
        var weaponGoldArg = ArgumentType.Integer("weaponGold");
        var ammoGoldArg = ArgumentType.Integer("ammoGold");
        var weaponNameArg = ArgumentType.String("weaponName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int weaponGold = context.get(weaponGoldArg);
            int ammoGold = context.get(ammoGoldArg);
            String weaponName = context.get(weaponNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.ShopData shop = new MapConfigurationSession.ShopData();
            shop.setType(ZombiesMapsConfig.ShopType.WEAPON);
            shop.setLocation(player.getPosition());
            shop.setGoldCost(weaponGold);

            MapConfigurationSession.WeaponShopData weaponConfig = new MapConfigurationSession.WeaponShopData();
            weaponConfig.setWeaponName(weaponName);
            weaponConfig.setWeaponGold(weaponGold);
            weaponConfig.setAmmoGold(ammoGold);
            shop.setWeaponConfig(weaponConfig);

            session.getShops().put(name, shop);
            sender.sendMessage("§aAdded weapon shop: " + name + " (" + weaponName + ")");
        }, saveWeaponShopLit, weaponGoldArg, ammoGoldArg, weaponNameArg, nameArg);

        // /zombies savePerkShop <PerkGold> <PerkName> <Name>
        var savePerkShopLit = ArgumentType.Literal("savePerkShop");
        var perkGoldArg = ArgumentType.Integer("perkGold");
        var perkNameArg = ArgumentType.String("perkName");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int perkGold = context.get(perkGoldArg);
            String perkName = context.get(perkNameArg);
            String name = context.get(nameArg);

            MapConfigurationSession.ShopData shop = new MapConfigurationSession.ShopData();
            shop.setType(ZombiesMapsConfig.ShopType.PERK);
            shop.setLocation(player.getPosition());
            shop.setGoldCost(perkGold);

            MapConfigurationSession.PerkShopData perkConfig = new MapConfigurationSession.PerkShopData();
            perkConfig.setPerkName(perkName);
            shop.setPerkConfig(perkConfig);

            session.getShops().put(name, shop);
            sender.sendMessage("§aAdded perk shop: " + name + " (" + perkName + ")");
        }, savePerkShopLit, perkGoldArg, perkNameArg, nameArg);
    }

    private void registerMachineCommands(MinestomCommand command) {
        // /zombies savePowerSwitch <PowerSwitchGold> <Name>
        var savePowerSwitchLit = ArgumentType.Literal("savePowerSwitch");
        var powerSwitchGoldArg = ArgumentType.Integer("powerSwitchGold");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int gold = context.get(powerSwitchGoldArg);
            String name = context.get(nameArg);

            MapConfigurationSession.MachineData machine = new MapConfigurationSession.MachineData();
            machine.setType("POWER_SWITCH");
            machine.setLocation(player.getPosition());
            machine.setGoldCost(gold);

            session.getMachines().put(name, machine);
            sender.sendMessage("§aAdded power switch: " + name);
        }, savePowerSwitchLit, powerSwitchGoldArg, nameArg);

        // /zombies saveUltimateMachine <Gold> <Name>
        var saveUltimateMachineLit = ArgumentType.Literal("saveUltimateMachine");
        var goldArg = ArgumentType.Integer("gold");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            int gold = context.get(goldArg);
            String name = context.get(nameArg);

            MapConfigurationSession.MachineData machine = new MapConfigurationSession.MachineData();
            machine.setType("ULTIMATE_MACHINE");
            machine.setLocation(player.getPosition());
            machine.setGoldCost(gold);

            session.getMachines().put(name, machine);
            sender.sendMessage("§aAdded ultimate machine: " + name);
        }, saveUltimateMachineLit, goldArg, nameArg);

        // /zombies addWeaponsChest <Name>
        var addWeaponsChestLit = ArgumentType.Literal("addWeaponsChest");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String name = context.get(nameArg);
            session.getWeaponChests().add(player.getPosition());
            sender.sendMessage("§aAdded weapon chest at current position (" + session.getWeaponChests().size() + ")");
        }, addWeaponsChestLit, nameArg);

        // /zombies addTeamMachine <Name>
        var addTeamMachineLit = ArgumentType.Literal("addTeamMachine");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            String name = context.get(nameArg);
            session.getTeamMachines().add(player.getPosition());
            sender.sendMessage("§aAdded team machine at current position (" + session.getTeamMachines().size() + ")");
        }, addTeamMachineLit, nameArg);
    }

    private void registerMiscCommands(MinestomCommand command) {
        // /zombies saveStatsHologram <Name>
        var saveStatsHologramLit = ArgumentType.Literal("saveStatsHologram");
        var nameArg = ArgumentType.String("name");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof HypixelPlayer player)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.setStatsHologram(player.getPosition());
            sender.sendMessage("§aSet stats hologram position");
        }, saveStatsHologramLit, nameArg);

        // /zombies save
        var saveLit = ArgumentType.Literal("save");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            session.saveToFile();
            sender.sendMessage("§aSaved configuration to file!");
        }, saveLit);

        // /zombies status
        var statusLit = ArgumentType.Literal("status");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            MapConfigurationSession session = TypeZombiesConfiguratorLoader.getCurrentSession();
            if (session == null) {
                sender.sendMessage("§cNo active session!");
                return;
            }
            sender.sendMessage("§e=== Zombies Map Configuration Status ===");
            sender.sendMessage("§7Map ID: §f" + session.getMapId());
            sender.sendMessage("§7Map Name: §f" + session.getMapName());
            sender.sendMessage("§7Map Type: §f" + session.getMapType().name());
            sender.sendMessage("§7Windows: §f" + session.getWindows().size());
            sender.sendMessage("§7Doors: §f" + session.getDoors().size());
            sender.sendMessage("§7Spawners: §f" + session.getSpawners().size());
            sender.sendMessage("§7Boss Spawns: §f" + session.getBossSpawns().size());
            sender.sendMessage("§7Shops: §f" + session.getShops().size());
            sender.sendMessage("§7Machines: §f" + session.getMachines().size());
            sender.sendMessage("§7Weapon Chests: §f" + session.getWeaponChests().size());
            sender.sendMessage("§7Team Machines: §f" + session.getTeamMachines().size());
        }, statusLit);
    }

    private static String formatPos(Pos pos) {
        return String.format("(%.1f, %.1f, %.1f)", pos.x(), pos.y(), pos.z());
    }
}
