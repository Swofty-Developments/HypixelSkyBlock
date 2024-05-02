package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoopDatabase {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("coop");
    }

    public void save(Coop coop) {
        if (coop.members.isEmpty() && coop.memberInvites.isEmpty()) {
            collection.deleteOne(Filters.eq("_id", coop.coopUUID.toString()));
            return;
        }

        Document document = coop.serialize();
        if (collection.find(Filters.eq("_id", coop.coopUUID.toString())).first() != null) {
            collection.replaceOne(Filters.eq("_id", coop.coopUUID.toString()), document);
        } else {
            collection.insertOne(document);
        }
    }

    public static Coop getFromMember(UUID member) {
        // Search through all coop documents and find the one that contains the UUID the list Members or MembersInvited
        for (Document document : collection.find()) {
            List<String> members = (List<String>) document.get("members");
            List<String> memberInvites = (List<String>) document.get("memberInvites");

            if (members.contains(member.toString()) || memberInvites.contains(member.toString())) {
                return Coop.deserialize(document);
            }
        }

        return null;
    }

    public static Coop getFromMemberProfile(UUID memberProfile) {
        // Search through all coop documents and find the one that contains the UUID in the memberProfiles list
        for (Document document : collection.find()) {
            List<String> memberProfiles = (List<String>) document.get("memberProfiles");

            if (memberProfiles.contains(memberProfile.toString())) {
                return Coop.deserialize(document);
            }
        }

        return null;
    }

    public static Coop getClean(UUID originator) {
        return new Coop(UUID.randomUUID(), originator, new ArrayList<>(), new ArrayList<>(List.of(originator)), new ArrayList<>());
    }

    public record Coop(UUID coopUUID, UUID originator, List<UUID> members, List<UUID> memberInvites,
                       List<UUID> memberProfiles) {
        public ProxyPlayerSet getMembersAsProxyPlayerSet(UUID... toExclude) {
            List<UUID> members = new ArrayList<>(this.members);
            members.removeAll(List.of(toExclude));
            return new ProxyPlayerSet(members);
        }

        public boolean isOriginator(UUID uuid) {
            return uuid.equals(originator);
        }

        public void addInvite(UUID uuid) {
            memberInvites.add(uuid);
        }

        public void removeInvite(UUID uuid) {
            memberInvites.remove(uuid);
        }

        public Document serialize() {
            Document document = new Document("_id", coopUUID.toString());
            document.put("originator", originator.toString());

            // Convert UUIDs to strings
            List<String> members = new ArrayList<>();
            this.members.forEach(uuid -> members.add(uuid.toString()));
            document.put("members", members);
            List<String> memberInvites = new ArrayList<>();
            this.memberInvites.forEach(uuid -> memberInvites.add(uuid.toString()));
            document.put("memberInvites", memberInvites);
            List<String> memberProfiles = new ArrayList<>();
            this.memberProfiles.forEach(uuid -> memberProfiles.add(uuid.toString()));
            document.put("memberProfiles", memberProfiles);

            return document;
        }

        public List<SkyBlockPlayer> getOnlineInvitedPlayers() {
            return SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player -> memberInvites.contains(player.getUuid())).toList();
        }

        public List<SkyBlockPlayer> getOnlineMembers() {
            return SkyBlockGenericLoader.getLoadedPlayers().stream()
                    .filter(player -> members.contains(player.getUuid()))
                    .filter(player -> memberProfiles.contains(player.getProfiles().getCurrentlySelected()))
                    .toList();
        }

        public void save() {
            CoopDatabase database = new CoopDatabase();
            database.save(this);
        }

        public Boolean isSameAs(Coop coop) {
            return coop.coopUUID.equals(coopUUID);
        }

        public static Coop deserialize(Document document) {
            UUID coopUUID = UUID.fromString(document.getString("_id"));
            UUID originator = UUID.fromString(document.getString("originator"));

            List<UUID> members = new ArrayList<>();
            List<String> membersString = (List<String>) document.get("members");
            membersString.forEach(uuid -> members.add(UUID.fromString(uuid)));
            List<UUID> memberInvites = new ArrayList<>();
            List<String> memberInvitesString = (List<String>) document.get("memberInvites");
            memberInvitesString.forEach(uuid -> memberInvites.add(UUID.fromString(uuid)));
            List<UUID> memberProfiles = new ArrayList<>();
            List<String> memberProfilesString = (List<String>) document.get("memberProfiles");
            memberProfilesString.forEach(uuid -> memberProfiles.add(UUID.fromString(uuid)));

            return new Coop(coopUUID, originator, members, memberInvites, memberProfiles);
        }
    }
}
