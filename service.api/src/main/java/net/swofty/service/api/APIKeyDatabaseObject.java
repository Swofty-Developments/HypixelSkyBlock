package net.swofty.service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIKeyDatabaseObject {
    public String key;
    public String description;
    public long requestsPerDay;

    public Document toDocument() {
        return new Document(Map.of(
                "_id", key,
                "description", description,
                "requestsPerDay", requestsPerDay
        ));
    }

    public static APIKeyDatabaseObject fromDocument(Document document) {
        APIKeyDatabaseObject object = new APIKeyDatabaseObject();
        object.setKey(document.getString("_id"));
        object.setDescription(document.getString("description"));
        object.setRequestsPerDay(document.getLong("requestsPerDay"));
        return object;
    }
}
