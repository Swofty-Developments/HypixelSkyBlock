package net.swofty.type.thepark;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.SkyBlockTypeLoader;
import net.swofty.type.generic.entity.BlockDisplayEntity;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.entity.TextDisplayEntity;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle.MissionPlaceTraps;
import net.swofty.type.skyblockgeneric.tabmodules.AccountInformationModule;
import net.swofty.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.thepark.tab.TheParkServerModule;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeTheParkLoader implements SkyBlockTypeLoader {

	@Override
	public ServerType getType() {
		return ServerType.SKYBLOCK_THE_PARK;
	}

	@Override
	public void onInitialize(MinecraftServer server) {
		Logger.info("TypeTheParkLoader initialized!");
	}

	@Override
	public void afterInitialize(MinecraftServer server) {
		List<Point> trapBlocks = List.of(
				new BlockVec(-466, 119, -54),
				new BlockVec(-449, 120, -65),
				new BlockVec(-440, 122, -92)
		);
		int index = 0;
		for (Point trap : trapBlocks) {
			int finalIndex = index;
			InteractionEntity hitbox = new InteractionEntity(1f, 1f, (p, event) -> {
				SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
				if (!player.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) {
					return;
				}

				MissionData.ActiveMission activeMission = player.getMissionData().getMission(MissionPlaceTraps.class).getKey();
				List<Integer> placedTraps = (List<Integer>) activeMission.getCustomData().getOrDefault("placedTraps", new ArrayList<Integer>());
				if (placedTraps.contains(finalIndex)) {
					player.sendMessage("§cThis trap has already been placed.");
					return;
				}
				placedTraps.add(finalIndex);
				activeMission.setCustomData(Map.of("placedTraps", placedTraps));
				player.sendMessage("§aPlaced trap §7(§e" + placedTraps.size() + "§7/§a3§7)");
			});

			TextDisplayEntity text = new TextDisplayEntity(Component.text("Place Trap Here", NamedTextColor.GREEN), meta -> {});
			hitbox.setInstance(HypixelConst.getInstanceContainer(), trap);
			text.setInstance(HypixelConst.getInstanceContainer(), trap.add(0, 1, 0));

			text.updateViewerRule(player -> {
				if (!(player instanceof SkyBlockPlayer p)) return false;
				if (!p.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) {
					return false;
				}

				MissionData.ActiveMission activeMission = p.getMissionData().getMission(MissionPlaceTraps.class).getKey();
				List<Integer> placedTraps = (List<Integer>) activeMission.getCustomData().getOrDefault("placedTraps", new ArrayList<Integer>());
				if (placedTraps.contains(finalIndex)) {
					return false;
				}

				return p.getPosition().distance(trap) <= 25;
			});

			float size = 0.6f;
			TrapdoorSide[] sides = {
					new TrapdoorSide(-size / 2, 0f, -size / 2 + 0.1f, 0f, -90f, 0f), // NORTH
					new TrapdoorSide(-size / 2 + 0.1f, 0f, size / 2, 0f, -90f, 90f), // WEST
					new TrapdoorSide(size / 2, 0f, size / 2, 0f, -90f, 90f), // EAST
					new TrapdoorSide(-size / 2, 0.3f, size / 2, 0f, -90f, 0f), // SOUTH
					new TrapdoorSide(-size / 2, 0.5f, -size / 2, 0f, 0f, 0f), // TOP
			};

			for (TrapdoorSide side : sides) {
				BlockDisplayEntity trapdoor = new BlockDisplayEntity(Block.OAK_TRAPDOOR, meta -> {
					meta.setScale(new Vec(size, size, size));
					Vec hingeOffset = new Vec(side.x(), side.y(), side.z());
					meta.setLeftRotation(rotation(side.tiltX(), side.rotY(), side.tiltZ()));
					meta.setTranslation(hingeOffset);
				});

				trapdoor.setInstance(HypixelConst.getInstanceContainer(), trap);
				trapdoor.updateViewerRule((p) -> {
					if (!(p instanceof SkyBlockPlayer player)) return false;
					if (!player.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) {
						return false;
					}

					MissionData.ActiveMission activeMission = player.getMissionData().getMission(MissionPlaceTraps.class).getKey();
					List<Integer> placedTraps = (List<Integer>) activeMission.getCustomData().getOrDefault("placedTraps", new ArrayList<Integer>());
					if (!placedTraps.contains(finalIndex)) {
						return false;
					}

					return player.getPosition().distance(trap) <= 25;
				});
			}


			index++;
		}

		MinecraftServer.getSchedulerManager().submitTask(() -> {
			for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
				if (!player.getMissionData().isCurrentlyActive(MissionPlaceTraps.class)) continue;
				int i = 0;
				for (Point position : trapBlocks) {
					if (!(player.getPosition().distance(position) <= 25)) continue;

					MissionData.ActiveMission activeMission = player.getMissionData().getMission(MissionPlaceTraps.class).getKey();
					List<Integer> placedTraps = (List<Integer>) activeMission.getCustomData().getOrDefault("placedTraps", new ArrayList<Integer>());
					if (placedTraps.contains(i)) {
						i++;
						continue;
					}

					List<SendablePacket> packets = TrapParticles.getParticlePackets(position);
					player.sendPackets(packets);
					i++;
				}
			}
			return TaskSchedule.millis(400);
		}, ExecutionType.TICK_START);

		TrialOfFire.init();
	}

	private static float[] rotation(float xDeg, float yDeg, float zDeg) {
		Quaternionf q = new Quaternionf()
				.rotationXYZ(
						(float) Math.toRadians(xDeg),
						(float) Math.toRadians(yDeg),
						(float) Math.toRadians(zDeg)
				);

		return new float[]{
				q.x, q.y, q.z, q.w
		};
	}

	record TrapdoorSide(float x, float y, float z, float rotY, float tiltX, float tiltZ) {
	}


	@Override
	public LoaderValues getLoaderValues() {
		return new LoaderValues(
				(type) -> switch (type) {
					case SKYBLOCK_GALATEA -> new Pos(-483.5, 117, -41.5, -120, 0);
					default -> new Pos(-265.5, 79, -17.5, 90, 0);
				}, // Spawn position
				true // Announce death messages
		);
	}

	public TablistManager getTablistManager() {
		return new TablistManager() {
			@Override
			public List<TablistModule> getModules() {
				return new ArrayList<>(List.of(
						new SkyBlockPlayersOnlineModule(1),
						new SkyBlockPlayersOnlineModule(2),
						new TheParkServerModule(),
						new AccountInformationModule()
				));
			}
		};
	}

	@Override
	public List<HypixelEventClass> getTraditionalEvents() {
		return SkyBlockGenericLoader.loopThroughPackage(
				"net.swofty.type.thepark.events",
				HypixelEventClass.class
		).collect(Collectors.toList());
	}

	@Override
	public List<HypixelEventClass> getCustomEvents() {
		return new ArrayList<>();
	}

	@Override
	public List<HypixelNPC> getNPCs() {
		return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
				"net.swofty.type.thepark.npcs",
				HypixelNPC.class
		).toList());
	}

	@Override
	public List<ServiceToClient> getServiceRedisListeners() {
		return List.of();
	}

	@Override
	public List<ProxyToClient> getProxyRedisListeners() {
		return List.of();
	}

	@Override
	public List<ServiceType> getRequiredServices() {
		return new ArrayList<>(List.of(ServiceType.DATA_MUTEX));
	}

	@Override
	public @Nullable CustomWorlds getMainInstance() {
		return CustomWorlds.SKYBLOCK_THE_PARK;
	}
}
