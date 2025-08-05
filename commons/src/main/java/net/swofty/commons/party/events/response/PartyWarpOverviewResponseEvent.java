package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyWarpOverviewResponseEvent extends PartyResponseEvent {
    private final UUID warper;
    private final List<UUID> successfullyWarped;
    private final List<UUID> failedToWarp;

    public PartyWarpOverviewResponseEvent(FullParty party, UUID warper, List<UUID> successfullyWarped, List<UUID> failedToWarp) {
        super(party);
        this.warper = warper;
        this.successfullyWarped = successfullyWarped;
        this.failedToWarp = failedToWarp;
    }

    public UUID getWarper() { return warper; }
    public List<UUID> getSuccessfullyWarped() { return successfullyWarped; }
    public List<UUID> getFailedToWarp() { return failedToWarp; }


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
                return new PartyWarpOverviewResponseEvent(party, warper, successfullyWarped, failedToWarp);
            }

            @Override
            public PartyWarpOverviewResponseEvent clone(PartyWarpOverviewResponseEvent value) {
                return new PartyWarpOverviewResponseEvent((FullParty) value.getParty(), value.warper, value.successfullyWarped, value.failedToWarp);
            }
        };
    }
}