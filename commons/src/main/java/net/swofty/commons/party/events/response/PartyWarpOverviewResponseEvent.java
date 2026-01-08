package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyWarpOverviewResponseEvent extends PartyResponseEvent {
    private final UUID warper;
    private final List<UUID> successfullyWarped;
    private final List<UUID> failedToWarp;
    private final Map<UUID, String> failureReasons;

    public PartyWarpOverviewResponseEvent(FullParty party, UUID warper, List<UUID> successfullyWarped, List<UUID> failedToWarp) {
        this(party, warper, successfullyWarped, failedToWarp, new HashMap<>());
    }

    public PartyWarpOverviewResponseEvent(FullParty party, UUID warper, List<UUID> successfullyWarped, List<UUID> failedToWarp, Map<UUID, String> failureReasons) {
        super(party);
        this.warper = warper;
        this.successfullyWarped = successfullyWarped;
        this.failedToWarp = failedToWarp;
        this.failureReasons = failureReasons != null ? failureReasons : new HashMap<>();
    }

    public UUID getWarper() { return warper; }
    public List<UUID> getSuccessfullyWarped() { return successfullyWarped; }
    public List<UUID> getFailedToWarp() { return failedToWarp; }
    public Map<UUID, String> getFailureReasons() { return failureReasons; }


    @Override
    public Serializer<PartyWarpOverviewResponseEvent> getSerializer() {
        return new Serializer<PartyWarpOverviewResponseEvent>() {
            @Override
            public String serialize(PartyWarpOverviewResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("warper", value.warper.toString());
                List<String> successfullyWarped = value.successfullyWarped.stream().map(UUID::toString).toList();
                json.put("successfullyWarped", successfullyWarped);
                List<String> failedToWarp = value.failedToWarp.stream().map(UUID::toString).toList();
                json.put("failedToWarp", failedToWarp);

                // Serialize failure reasons
                JSONObject failureReasonsJson = new JSONObject();
                for (Map.Entry<UUID, String> entry : value.failureReasons.entrySet()) {
                    failureReasonsJson.put(entry.getKey().toString(), entry.getValue());
                }
                json.put("failureReasons", failureReasonsJson);

                return json.toString();
            }

            @Override
            public PartyWarpOverviewResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID warper = UUID.fromString(jsonObject.getString("warper"));
                List<UUID> successfullyWarped = jsonObject.getJSONArray("successfullyWarped").toList().stream().map(object -> {
                    try {
                        return UUID.fromString(object.toString());
                    } catch (Exception e) {
                        return null;
                    }
                }).toList();
                List<UUID> failedToWarp = jsonObject.getJSONArray("failedToWarp").toList().stream().map(object -> {
                    try {
                        return UUID.fromString(object.toString());
                    } catch (Exception e) {
                        return null;
                    }
                }).toList();

                // Deserialize failure reasons
                Map<UUID, String> failureReasons = new HashMap<>();
                if (jsonObject.has("failureReasons")) {
                    JSONObject failureReasonsJson = jsonObject.getJSONObject("failureReasons");
                    for (String key : failureReasonsJson.keySet()) {
                        try {
                            failureReasons.put(UUID.fromString(key), failureReasonsJson.getString(key));
                        } catch (Exception ignored) {}
                    }
                }

                return new PartyWarpOverviewResponseEvent(party, warper, successfullyWarped, failedToWarp, failureReasons);
            }

            @Override
            public PartyWarpOverviewResponseEvent clone(PartyWarpOverviewResponseEvent value) {
                return new PartyWarpOverviewResponseEvent((FullParty) value.getParty(), value.warper, value.successfullyWarped, value.failedToWarp, new HashMap<>(value.failureReasons));
            }
        };
    }
}