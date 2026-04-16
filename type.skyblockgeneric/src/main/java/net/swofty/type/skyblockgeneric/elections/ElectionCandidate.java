package net.swofty.type.skyblockgeneric.elections;

import net.swofty.commons.protocol.objects.election.GetCandidatesProtocolObject;

import java.util.List;

public record ElectionCandidate(
        SkyBlockMayor mayor,
        List<SkyBlockMayor.Perk> activePerks,
        long votes,
        double votePercentage
) {

    public static ElectionCandidate fromProtocol(GetCandidatesProtocolObject.CandidateInfo info) {
        SkyBlockMayor mayor;
        try {
            mayor = SkyBlockMayor.valueOf(info.mayorName());
        } catch (IllegalArgumentException e) {
            return null;
        }

        List<SkyBlockMayor.Perk> perks = info.activePerks().stream()
                .map(name -> {
                    try {
                        return SkyBlockMayor.Perk.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(p -> p != null)
                .toList();

        return new ElectionCandidate(mayor, perks, info.votes(), info.votePercentage());
    }

    public static List<ElectionCandidate> fromProtocolList(List<GetCandidatesProtocolObject.CandidateInfo> infos) {
        return infos.stream()
                .map(ElectionCandidate::fromProtocol)
                .filter(c -> c != null)
                .toList();
    }
}
