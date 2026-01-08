package net.swofty.type.bedwarsconfigurator.commands;

import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.bedwarsconfigurator.TypeBedWarsConfiguratorLoader;
import net.swofty.type.bedwarsconfigurator.autosetup.AutoSetupSession;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@CommandParameters(aliases = "choose choosemap selectmap select",
		description = "Choose a BedWars map to configure",
		usage = "/choosemap <map>",
		permission = Rank.STAFF,
		allowsConsole = false)
public class ChooseMapCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		var mapArg = ArgumentType.String("map");
		mapArg.setSuggestionCallback((sender, context, suggestion) -> {
			Set<String> addedIds = new HashSet<>();
			for (BedWarsMapsConfig.MapEntry entry : TypeBedWarsConfiguratorLoader.getMapsConfig().getMaps()) {
				suggestion.addEntry(new SuggestionEntry(entry.getId(), Component.text(entry.getName() + " §7(configured)")));
				addedIds.add(entry.getId().toLowerCase());
			}

			File bedwarsDir = new File("./configuration/bedwars/");
			if (bedwarsDir.exists() && bedwarsDir.isDirectory()) {
				File[] polarFiles = bedwarsDir.listFiles((dir, name) -> name.endsWith(".polar"));
				if (polarFiles != null) {
					for (File polarFile : polarFiles) {
						String mapId = polarFile.getName().replace(".polar", "");
						if (!addedIds.contains(mapId.toLowerCase())) {
							suggestion.addEntry(new SuggestionEntry(mapId, Component.text(mapId + " §e(unconfigured)")));
						}
					}
				}
			}
		});

		command.addSyntax((sender, context) -> {
			if (!(sender instanceof Player player)) {
				sender.sendMessage(Component.text("§cThis command can only be executed by a player."));
				return;
			}
			String mapId = context.get("map");

			BedWarsMapsConfig.MapEntry selectedMap = null;
			for (BedWarsMapsConfig.MapEntry entry : TypeBedWarsConfiguratorLoader.getMapsConfig().getMaps()) {
				if (entry.getId().equalsIgnoreCase(mapId)) {
					selectedMap = entry;
					break;
				}
			}

			File polarFile = new File("./configuration/bedwars/" + mapId + ".polar");
			if (!polarFile.exists()) {
				sender.sendMessage(Component.text("§cNo polar file found for map: " + mapId));
				return;
			}

			InstanceContainer mapInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
			try {
				mapInstance.setChunkLoader(new PolarLoader(polarFile.toPath()));
			} catch (IOException e) {
				sender.sendMessage(Component.text("§cFailed to load map: " + mapId));
				return;
			}

			AutoSetupSession session = AutoSetupSession.getOrCreate(player.getUuid(), mapInstance);
			session.setMapId(mapId);

			if (selectedMap != null) {
				session.setMapName(selectedMap.getName());

				if (selectedMap.getConfiguration() != null) {
					session.loadFromMapEntry(selectedMap);
					sender.sendMessage(Component.text("§aLoaded existing configuration for: " + selectedMap.getName()));
				} else {
					sender.sendMessage(Component.text("§eSelected map: " + selectedMap.getName() + " §7(no existing config)"));
				}
			} else {
				session.setMapName(mapId);
				session.clear();
				session.setMapId(mapId);
				session.setMapName(mapId);
				sender.sendMessage(Component.text("§eLoaded unconfigured map: §f" + mapId + " §7(starting fresh)"));
				sender.sendMessage(Component.text("§7Use §b/autosetup §7to automatically configure the map, or set things manually."));
			}

			player.setInstance(mapInstance);
		}, mapArg);
	}

}
