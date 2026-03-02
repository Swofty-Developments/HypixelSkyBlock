package net.swofty.type.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONObject;

public class DatapointReplaySettings extends Datapoint<DatapointReplaySettings.ReplaySettings> {
	private static final Serializer<ReplaySettings> serializer = new Serializer<>() {
		@Override
		public String serialize(ReplaySettings value) {
			JSONObject json = new JSONObject();
			return json.toString();
		}

		@Override
		public ReplaySettings deserialize(String json) {
			if (json == null || json.isEmpty()) {
				return ReplaySettings.createDefault();
			}

			try {
				JSONObject obj = new JSONObject(json);
				ReplaySettings data = new ReplaySettings();
				data.chatMessages = obj.optBoolean("chatMessages", true);
				data.chatTimeline = obj.optBoolean("chatTimeline", false);
				data.showSpectators = obj.optBoolean("showSpectators", false);
				data.nightVision = obj.optBoolean("nightVision", true);
				data.showParticles = obj.optBoolean("showParticles", true);
				data.advanceTime = obj.optBoolean("advanceTime", true);
				data.flySpeed = (short) obj.optInt("flySpeed", 1);
				data.skipIntervals = (short) obj.optInt("skipIntervals", 30);
				return data;
			} catch (Exception e) {
				return ReplaySettings.createDefault();
			}
		}

		@Override
		public ReplaySettings clone(ReplaySettings value) {
			ReplaySettings cloned = new ReplaySettings();
			cloned.chatMessages = value.chatMessages;
			cloned.chatTimeline = value.chatTimeline;
			cloned.showSpectators = value.showSpectators;
			cloned.nightVision = value.nightVision;
			cloned.showParticles = value.showParticles;
			cloned.advanceTime = value.advanceTime;
			cloned.flySpeed = value.flySpeed;
			cloned.skipIntervals = value.skipIntervals;
			return cloned;
		}
	};

	public DatapointReplaySettings(String key, ReplaySettings value) {
		super(key, value, serializer);
	}

	public DatapointReplaySettings(String key) {
		super(key, ReplaySettings.createDefault(), serializer);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReplaySettings {
		private boolean chatMessages;
		private boolean chatTimeline;
		private boolean showSpectators;
		private boolean nightVision;
		private boolean showParticles;
		private boolean advanceTime;
		private short flySpeed;
		private short skipIntervals;

		public static ReplaySettings createDefault() {
			ReplaySettings data = new ReplaySettings();
			data.chatMessages = true;
			data.chatTimeline = false;
			data.showSpectators = false;
			data.nightVision = true;
			data.showParticles = true;
			data.advanceTime = true;
			data.flySpeed = 1;
			data.skipIntervals = 30;
			return data;
		}
	}
}

