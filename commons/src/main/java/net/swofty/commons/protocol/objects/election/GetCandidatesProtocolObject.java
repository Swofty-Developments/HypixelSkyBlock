package net.swofty.commons.protocol.objects.election;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;

public class GetCandidatesProtocolObject
        extends ProtocolObject<GetCandidatesProtocolObject.GetCandidatesMessage,
        GetCandidatesProtocolObject.GetCandidatesResponse> {

    @Override
    public Serializer<GetCandidatesMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetCandidatesMessage value) {
                return new JSONObject().put("_", true).toString();
            }

            @Override
            public GetCandidatesMessage deserialize(String json) {
                return new GetCandidatesMessage();
            }

            @Override
            public GetCandidatesMessage clone(GetCandidatesMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<GetCandidatesResponse> getReturnSerializer() {
        return new Serializer<>() {
            private final Gson gson = new Gson();

            @Override
            public String serialize(GetCandidatesResponse value) {
                JSONObject json = new JSONObject();
                json.put("electionOpen", value.electionOpen());
                json.put("candidates", gson.toJson(value.candidates()));
                return json.toString();
            }

            @Override
            public GetCandidatesResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                boolean open = obj.getBoolean("electionOpen");
                List<CandidateInfo> candidates = List.of();
                if (!obj.isNull("candidates")) {
                    candidates = gson.fromJson(
                            obj.getString("candidates"),
                            new TypeToken<List<CandidateInfo>>() {}.getType()
                    );
                }
                return new GetCandidatesResponse(open, candidates);
            }

            @Override
            public GetCandidatesResponse clone(GetCandidatesResponse value) {
                return value;
            }
        };
    }

    public record GetCandidatesMessage() {}

    public record GetCandidatesResponse(
            boolean electionOpen,
            List<CandidateInfo> candidates
    ) {}

    public record CandidateInfo(
            String mayorName,
            List<String> activePerks,
            long votes,
            double votePercentage
    ) {}
}
