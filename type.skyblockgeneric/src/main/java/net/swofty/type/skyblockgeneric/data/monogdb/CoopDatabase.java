package net.swofty.type.skyblockgeneric.data.monogdb;

import com.mongodb.client.MongoClient;
import net.swofty.commons.data.SwoftyData;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoopDatabase {
    private static final String COOP_PREFIX = "hsb:coop:";
    private static final String BY_MEMBER = "hsb:coop:bymember";
    private static final String BY_PROFILE = "hsb:coop:byprofile";

    public static void connect(MongoClient client) {
    }

    public void save(Coop coop) {
        try (Jedis jedis = SwoftyData.jedisPool().getResource()) {
            String existing = jedis.get(COOP_PREFIX + coop.coopUUID);
            if (existing != null) {
                Coop old = Coop.deserialize(Document.parse(existing));
                old.members.forEach(uuid -> jedis.hdel(BY_MEMBER, uuid.toString()));
                old.memberInvites.forEach(uuid -> jedis.hdel(BY_MEMBER, uuid.toString()));
                old.memberProfiles.forEach(uuid -> jedis.hdel(BY_PROFILE, uuid.toString()));
            }

            if (coop.members.isEmpty() && coop.memberInvites.isEmpty()) {
                jedis.del(COOP_PREFIX + coop.coopUUID);
                return;
            }

            jedis.set(COOP_PREFIX + coop.coopUUID, coop.serialize().toJson());
            coop.members.forEach(uuid -> jedis.hset(BY_MEMBER, uuid.toString(), coop.coopUUID.toString()));
            coop.memberInvites.forEach(uuid -> jedis.hset(BY_MEMBER, uuid.toString(), coop.coopUUID.toString()));
            coop.memberProfiles.forEach(uuid -> jedis.hset(BY_PROFILE, uuid.toString(), coop.coopUUID.toString()));
        }
    }

    public static Coop getFromMember(UUID member) {
        return lookup(BY_MEMBER, member);
    }

    public static Coop getFromMemberProfile(UUID memberProfile) {
        return lookup(BY_PROFILE, memberProfile);
    }

    private static Coop lookup(String index, UUID key) {
        try (Jedis jedis = SwoftyData.jedisPool().getResource()) {
            String coopId = jedis.hget(index, key.toString());
            if (coopId == null) return null;
            String json = jedis.get(COOP_PREFIX + coopId);
            return json == null ? null : Coop.deserialize(Document.parse(json));
        }
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
