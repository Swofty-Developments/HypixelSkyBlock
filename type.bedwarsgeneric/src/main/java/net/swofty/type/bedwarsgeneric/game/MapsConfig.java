package net.swofty.type.bedwarsgeneric.game;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings("unused")
public class MapsConfig {
	private List<MapEntry> maps;

	@Getter
	public static class MapEntry {
		private String id;
		private String name;
		private MapConfiguration configuration;

		@Getter
		public static class MapConfiguration {
			private List<String> types;
			private Map<String, TeamGeneratorConfig> generator;
			private MapBounds bounds;
			private List<MapTeam> teams;
			private MapLocations locations;
			private Map<String, GlobalGenerator> global_generator;

			@Getter
			public static class MapLocations {
				private Position waiting;
				private Position spectator;
			}

			@Getter
			public static class MapTeam {
				private String name;
				private String color;
				private String javacolor;
				private Shops shop;
				private PitchYawPosition spawn;
				private TwoBlockPosition bed;
				private Position generator;

				public record Shops(PitchYawPosition item, PitchYawPosition team) {
				}
			}

			@Getter
			public static class MapBounds {
				private MinMax x;
				private MinMax y;
				private MinMax z;
			}

			@Getter
			public static class TeamGeneratorConfig {
				private int delay;
				private int amount;
			}

			@Getter
			public static class GlobalGenerator {
				private int delay;
				private int amount;
				private int max;
				private List<Position> locations;
			}

		}
	}

	public record Position(double x, double y, double z) {
	}

	public record PitchYawPosition(double x, double y, double z, float pitch, float yaw) {
	}

	public record TwoBlockPosition(Position feet, Position head) {
	}

	public record MinMax(double min, double max) {
	}

}
