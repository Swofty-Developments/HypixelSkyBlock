package net.swofty.commons.party;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class FullParty implements Party {
    private final UUID uuid;
    private final List<Member> members;

    private FullParty(UUID uuid, List<Member> members) {
        this.uuid = uuid;
        this.members = members;
    }

    public Member getFromUuid(UUID uuid) {
        return members.stream().filter(member -> member.getUuid().equals(uuid)).findFirst().orElseThrow();
    }

    public Member getLeader() {
        return members.stream().filter(member -> member.getRole() == Role.LEADER).findFirst().orElseThrow();
    }

    public static FullParty create(UUID leader, UUID invitee) {
        Member leaderMember = new Member(leader, Role.LEADER, true);
        Member inviteeMember = new Member(invitee, Role.MEMBER, true);

        return new FullParty(UUID.randomUUID(), new ArrayList<>(List.of(leaderMember, inviteeMember)));
    }

    public static Serializer<FullParty> getStaticSerializer() {
        FullParty party = create(UUID.randomUUID(), UUID.randomUUID());
        return party.getSerializer();
    }

    @Override
    public Serializer<FullParty> getSerializer() {
        return new Serializer<FullParty>() {
            @Override
            public String serialize(FullParty value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid.toString());
                json.put("members", value.members.stream()
                        .map(member -> new JSONObject()
                                .put("uuid", member.uuid.toString())
                                .put("role", member.role.name())
                                .put("joined", member.joined)
                        ).toList());
                return json.toString();
            }

            @Override
            public FullParty deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID uuid = UUID.fromString(jsonObject.getString("uuid"));
                List<Member> members = jsonObject.getJSONArray("members").toList().stream()
                        .map(member -> {
                            Map<String, Object> memberObject = (Map<String, Object>) member;

                            return new Member(
                                    UUID.fromString(memberObject.get("uuid").toString()),
                                    Role.valueOf(memberObject.get("role").toString()),
                                    memberObject.get("joined").toString().equals("true")
                            );
                        }).toList();
                return new FullParty(uuid, members);
            }

            @Override
            public FullParty clone(FullParty value) {
                return new FullParty(value.uuid, value.members);
            }
        };
    }

    @Override
    public List<UUID> getParticipants() {
        return members.stream().map(Member::getUuid).toList();
    }

    @Setter
    @Getter
    public static class Member {
        private final UUID uuid;
        private Role role;
        private boolean joined;

        public Member(UUID uuid, Role role, boolean joined) {
            this.uuid = uuid;
            this.role = role;
            this.joined = joined;
        }
    }

    public enum Role {
        LEADER,
        MODERATOR,
        MEMBER,
        ;

        @Override
        public String toString() {
            return StringUtility.toNormalCase(name());
        }
    }
}
