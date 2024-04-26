package net.swofty.types.generic.protocol.itemtracker;

import net.swofty.commons.TrackedItem;
import net.swofty.service.protocol.ProtocolSpecification;
import net.swofty.service.protocol.Serializer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolGetTrackedItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("item-uuid", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<>("tracked-item", true, new Serializer<TrackedItem>() {
                    @Override
                    public String serialize(TrackedItem value) {
                        return value.toDocument().toJson();
                    }

                    @Override
                    public TrackedItem deserialize(String json) {
                        return TrackedItem.fromDocument(Document.parse(json));
                    }

                    @Override
                    public TrackedItem clone(TrackedItem value) {
                        return value.clone();
                    }
                })
        ));
    }

    @Override
    public String getEndpoint() {
        return "get-tracked-item";
    }
}
