package net.swofty.type.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import net.swofty.PlayerField;
import net.swofty.codec.Codecs;
import net.swofty.commons.data.NameIndex;
import net.swofty.commons.data.SwoftyData;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public record ProfilesDatabase(String id) implements MongoDB {
    public static final PlayerField<String> DOCUMENT =
            PlayerField.create("skyblock", "_doc", Codecs.STRING, null);

    public static void connect(MongoClient client) {
    }

    private Document read() {
        String json = SwoftyData.profile().get(UUID.fromString(id), DOCUMENT);
        return json == null ? null : Document.parse(json);
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = read();
        return doc == null ? def : doc.getOrDefault(key, def);
    }

    public void insertOrUpdate(String key, Object value) {
        Document doc = read();
        if (doc == null) doc = new Document("_id", id);
        doc.put(key, value);
        saveDocument(doc);
    }

    public Document getDocument() {
        return read();
    }

    public void saveDocument(Document document) {
        SwoftyData.profile().set(UUID.fromString(id), DOCUMENT, document.toJson());
    }

    public boolean exists() {
        return SwoftyData.profile().get(UUID.fromString(id), DOCUMENT) != null;
    }

    @Override
    public boolean remove(String uniqueId) {
        SwoftyData.profile().set(UUID.fromString(uniqueId), DOCUMENT, null);
        return true;
    }

    public List<Document> getAll() {
        return List.of();
    }

    public static void replaceDocument(String uniqueId, Document document) {
        SwoftyData.profile().set(UUID.fromString(uniqueId), DOCUMENT, document.toJson());
    }

    public static UUID fetchUUID(String username) {
        return NameIndex.lookup(username);
    }

    public static Document fetchDocument(String uniqueId) {
        String json = SwoftyData.profile().get(UUID.fromString(uniqueId), DOCUMENT);
        return json == null ? null : Document.parse(json);
    }

    public static Document fetchDocument(UUID uniqueId) {
        return fetchDocument(uniqueId.toString());
    }

    public static void deleteDocument(String uniqueId) {
        SwoftyData.profile().set(UUID.fromString(uniqueId), DOCUMENT, null);
    }
}
