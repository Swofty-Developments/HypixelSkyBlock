package net.swofty.service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIAdminDatabaseObject {
    public String sessionId;
    public String authCode;
    public String authenticatorName;
    public UUID authenticatorUUID;

    public Document toDocument() {
        return new Document(Map.of(
                "_id", sessionId,
                "authCode", authCode,
                "authenticatorName", authenticatorName,
                "authenticatorUUID", authenticatorUUID.toString()
        ));
    }

    public boolean isAuthenticated() {
        return authenticatorName != null && !authenticatorName.isEmpty();
    }

    public static APIAdminDatabaseObject fromDocument(Document document) {
        APIAdminDatabaseObject object = new APIAdminDatabaseObject();
        object.setSessionId(document.getString("_id"));
        object.setAuthCode(document.getString("authCode"));
        object.setAuthenticatorName(document.getString("authenticatorName"));
        object.setAuthenticatorUUID(UUID.fromString(document.getString("authenticatorUUID")));
        return object;
    }
}
