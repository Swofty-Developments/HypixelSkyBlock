package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.UUID;

public class DatapointMuseum extends Datapoint<DatapointMuseum.MuseumData> {
    private static final Serializer<MuseumData> serializer = new Serializer<>() {
        @Override
        public String serialize(MuseumData value) {
            return value.serialize().toString();
        }

        @Override
        public MuseumData deserialize(String json) {
            return MuseumData.deserialize(new JSONObject(json));
        }

        @Override
        public MuseumData clone(MuseumData value) {
            return MuseumData.deserialize(new JSONObject(value.serialize().toString()));
        }
    };

    public DatapointMuseum(String key, MuseumData value) {
        super(key, value, serializer);
    }

    public DatapointMuseum(String key) {
        super(key, new MuseumData(), serializer);
    }

    @Getter
    @Setter
    public static class MuseumData {
        public UUID currentlyViewing;
        public boolean hasBoughtAppraisalService = false;

        public JSONObject serialize() {
            JSONObject json = new JSONObject();
            if (currentlyViewing != null)
                json.put("currentlyViewing", currentlyViewing.toString());
            else json.put("currentlyViewing", JSONObject.NULL);
            json.put("hasBoughtAppraisalService", hasBoughtAppraisalService);
            return json;
        }

        public static MuseumData deserialize(JSONObject json) {
            MuseumData data = new MuseumData();
            if (json.isNull("currentlyViewing"))
                data.currentlyViewing = null;
            else
                data.currentlyViewing = UUID.fromString(json.getString("currentlyViewing"));
            data.hasBoughtAppraisalService = json.getBoolean("hasBoughtAppraisalService");
            return data;
        }
    }
}
