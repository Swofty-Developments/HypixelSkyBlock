package net.swofty.commons.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class FullParty implements Party {
    private final UUID uuid;
    private final List<Member> members;

    @JsonCreator
    public FullParty(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("members") List<Member> members) {
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

        @JsonCreator
        public Member(
                @JsonProperty("uuid") UUID uuid,
                @JsonProperty("role") Role role,
                @JsonProperty("joined") boolean joined) {
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
