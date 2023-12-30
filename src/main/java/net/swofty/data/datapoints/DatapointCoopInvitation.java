package net.swofty.data.datapoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;
import net.swofty.serializer.Serializer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatapointCoopInvitation extends Datapoint<List<DatapointCoopInvitation.CoopInvitation>> {
    public DatapointCoopInvitation(String key, List<DatapointCoopInvitation.CoopInvitation> value) {
        super(key, value, new Serializer<>() {

            @Override
            public String serialize(List<CoopInvitation> value) throws JsonProcessingException {
                JSONObject json = new JSONObject();
                for (CoopInvitation invitation : value) {
                    JSONObject invitationJson = new JSONObject();
                    invitationJson.put("outgoing", invitation.outgoing());
                    invitationJson.put("target", invitation.target().toString());
                    invitationJson.put("accepted", invitation.accepted());
                    invitationJson.put("timestamp", invitation.timestamp());
                    json.put(invitation.target().toString(), invitationJson);
                }
                return json.toString();
            }

            @Override
            public List<CoopInvitation> deserialize(String json) throws JsonProcessingException {
                JSONObject jsonObject = new JSONObject(json);
                List<CoopInvitation> list = new ArrayList<>();
                for (String key : jsonObject.keySet()) {
                    JSONObject invitationJson = jsonObject.getJSONObject(key);
                    list.add(new CoopInvitation(
                            invitationJson.getBoolean("outgoing"),
                            UUID.fromString(invitationJson.getString("target")),
                            invitationJson.getBoolean("accepted"),
                            invitationJson.getLong("timestamp")));
                }
                return list;
            }
        });
    }

    public void add(CoopInvitation value) {
        List<CoopInvitation> current = getValue();
        current.add(value);
        setValue(current);
    }

    public void remove(CoopInvitation value) {
        List<CoopInvitation> current = getValue();
        current.remove(value);
        setValue(current);
    }

    public boolean has(CoopInvitation value) {
        return getValue().contains(value);
    }

    public record CoopInvitation(boolean outgoing, UUID target, boolean accepted, long timestamp) { }
}
