package net.swofty.type.skyblockgeneric.elections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class ElectionData {
    private int electionYear;

    private String currentMayor;
    private List<String> currentMayorPerks = new ArrayList<>();
    private int mayorElectedYear;

    private String currentMinister;
    private String ministerPerk;

    private List<CandidateData> candidates = new ArrayList<>();
    private Map<String, String> votes = new HashMap<>();
    private boolean electionOpen;

    private Map<String, Integer> lastElectedYear = new HashMap<>();
    private Map<String, List<String>> candidateActivePerks = new HashMap<>();
    private Map<String, Boolean> failedPerkGainLastTime = new HashMap<>();
    private int specialCandidateIndex;

    public ElectionData() {}

    public SkyBlockMayor getMayorEnum() {
        if (currentMayor == null) return null;
        try {
            return SkyBlockMayor.valueOf(currentMayor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public SkyBlockMayor getMinisterEnum() {
        if (currentMinister == null) return null;
        try {
            return SkyBlockMayor.valueOf(currentMinister);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public SkyBlockMayor.Perk getMinisterPerkEnum() {
        if (ministerPerk == null) return null;
        try {
            return SkyBlockMayor.Perk.valueOf(ministerPerk);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<SkyBlockMayor.Perk> getCurrentMayorPerkEnums() {
        List<SkyBlockMayor.Perk> result = new ArrayList<>();
        for (String perkName : currentMayorPerks) {
            try {
                result.add(SkyBlockMayor.Perk.valueOf(perkName));
            } catch (IllegalArgumentException ignored) {}
        }
        return result;
    }

    public void castVote(UUID accountId, String candidateName) {
        votes.put(accountId.toString(), candidateName);
    }

    public String getVote(UUID accountId) {
        return votes.get(accountId.toString());
    }

    public Map<String, Long> tallyVotes() {
        Map<String, Long> tally = new HashMap<>();
        for (CandidateData candidate : candidates) {
            tally.put(candidate.getMayorName(), 0L);
        }
        for (String candidateName : votes.values()) {
            tally.merge(candidateName, 1L, Long::sum);
        }
        return tally;
    }

    public void resolveElection(int currentYear) {
        Map<String, Long> tally = tallyVotes();
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(tally.entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        if (sorted.isEmpty()) return;

        long topVotes = sorted.getFirst().getValue();
        List<Map.Entry<String, Long>> tied = sorted.stream()
                .filter(e -> e.getValue() == topVotes).toList();

        String winnerName;
        if (tied.size() > 1) {
            winnerName = tied.get(ThreadLocalRandom.current().nextInt(tied.size())).getKey();
        } else {
            winnerName = sorted.getFirst().getKey();
        }

        SkyBlockMayor winner = SkyBlockMayor.valueOf(winnerName);

        CandidateData winnerCandidate = candidates.stream()
                .filter(c -> c.getMayorName().equals(winnerName))
                .findFirst().orElse(null);

        currentMayor = winnerName;
        currentMayorPerks = winnerCandidate != null
                ? new ArrayList<>(winnerCandidate.getActivePerks())
                : Arrays.stream(winner.getAllPerks()).map(Enum::name).toList();
        mayorElectedYear = currentYear;
        lastElectedYear.put(winnerName, currentYear);

        if (winner.isSpecial()) {
            currentMinister = null;
            ministerPerk = null;
        } else {
            String secondPlace = sorted.stream().filter(entry -> !entry.getKey().equals(winnerName)).findFirst().map(Map.Entry::getKey).orElse(null);
            if (secondPlace != null) {
                SkyBlockMayor ministerMayor = SkyBlockMayor.valueOf(secondPlace);
                currentMinister = secondPlace;

                CandidateData ministerCandidate = candidates.stream()
                        .filter(c -> c.getMayorName().equals(secondPlace))
                        .findFirst().orElse(null);

                if (ministerCandidate != null && !ministerCandidate.getActivePerks().isEmpty()) {
                    List<String> perks = ministerCandidate.getActivePerks();
                    ministerPerk = perks.get(ThreadLocalRandom.current().nextInt(perks.size()));
                } else {
                    SkyBlockMayor.Perk[] allPerks = ministerMayor.getAllPerks();
                    ministerPerk = allPerks[ThreadLocalRandom.current().nextInt(allPerks.length)].name();
                }
            }
        }

        for (CandidateData candidate : candidates) {
            if (candidate.getMayorName().equals(winnerName)) {
                candidateActivePerks.remove(winnerName);
                failedPerkGainLastTime.remove(winnerName);
                continue;
            }

            SkyBlockMayor mayor = SkyBlockMayor.valueOf(candidate.getMayorName());
            if (mayor.isSpecial()) continue;

            List<String> currentPerks = new ArrayList<>(candidate.getActivePerks());
            if (currentPerks.size() < mayor.getAllPerks().length) {
                boolean failedLast = Boolean.TRUE.equals(failedPerkGainLastTime.get(candidate.getMayorName()));
                boolean gainPerk = failedLast || ThreadLocalRandom.current().nextBoolean();

                if (gainPerk) {
                    List<String> available = new ArrayList<>();
                    for (SkyBlockMayor.Perk perk : mayor.getAllPerks()) {
                        if (!currentPerks.contains(perk.name())) {
                            available.add(perk.name());
                        }
                    }
                    if (!available.isEmpty()) {
                        currentPerks.add(available.get(ThreadLocalRandom.current().nextInt(available.size())));
                    }
                    failedPerkGainLastTime.put(candidate.getMayorName(), false);
                } else {
                    failedPerkGainLastTime.put(candidate.getMayorName(), true);
                }
            }
            candidateActivePerks.put(candidate.getMayorName(), currentPerks);
        }

        SkyBlockMayor winnerEnum = SkyBlockMayor.valueOf(winnerName);
        if (!winnerEnum.isSpecial()) {
            SkyBlockMayor.Perk[] allPerks = winnerEnum.getAllPerks();
            SkyBlockMayor.Perk randomPerk = allPerks[ThreadLocalRandom.current().nextInt(allPerks.length)];
            candidateActivePerks.put(winnerName, List.of(randomPerk.name()));
        }

        candidates.clear();
        votes.clear();
        electionOpen = false;
    }

    public void startNewElection(int year) {
        this.electionYear = year;
        this.electionOpen = true;
        this.votes.clear();
        this.candidates.clear();

        boolean isSpecialYear = (year % 8 == 0);

        List<SkyBlockMayor> regularPool = new ArrayList<>(SkyBlockMayor.getRegularMayors());
        if (currentMayor != null) {
            regularPool.removeIf(m -> m.name().equals(currentMayor));
        }

        Collections.shuffle(regularPool);

        int regularCount = isSpecialYear ? 4 : 5;
        List<SkyBlockMayor> selected = new ArrayList<>(regularPool.subList(0, Math.min(regularCount, regularPool.size())));

        for (SkyBlockMayor mayor : selected) {
            CandidateData candidate = new CandidateData();
            candidate.setMayorName(mayor.name());

            List<String> perks = candidateActivePerks.get(mayor.name());
            if (perks == null || perks.isEmpty()) {
                SkyBlockMayor.Perk[] allPerks = mayor.getAllPerks();
                candidate.setActivePerks(List.of(allPerks[ThreadLocalRandom.current().nextInt(allPerks.length)].name()));
            } else {
                candidate.setActivePerks(new ArrayList<>(perks));
            }
            candidates.add(candidate);
        }

        if (isSpecialYear) {
            List<SkyBlockMayor> specials = SkyBlockMayor.getSpecialMayors();
            SkyBlockMayor specialCandidate = specials.get(specialCandidateIndex % specials.size());
            specialCandidateIndex++;

            CandidateData candidate = new CandidateData();
            candidate.setMayorName(specialCandidate.name());
            candidate.setActivePerks(Arrays.stream(specialCandidate.getAllPerks()).map(Enum::name).toList());
            candidates.add(candidate);
        }

        boolean diazLongTerm = currentMinister != null
                && currentMinister.equals(SkyBlockMayor.DIAZ.name())
                && ministerPerk != null
                && ministerPerk.equals(SkyBlockMayor.Perk.LONG_TERM_INVESTMENT.name());

        if (diazLongTerm) {
            boolean alreadyInElection = candidates.stream()
                    .anyMatch(c -> c.getMayorName().equals(SkyBlockMayor.DIAZ.name()));
            if (!alreadyInElection) {
                CandidateData diazCandidate = new CandidateData();
                diazCandidate.setMayorName(SkyBlockMayor.DIAZ.name());
                diazCandidate.setActivePerks(Arrays.stream(SkyBlockMayor.DIAZ.getAllPerks()).map(Enum::name).toList());
                candidates.add(diazCandidate);
            }
        }
    }

    public int getYearsSinceLastElected(String mayorName, int currentYear) {
        Integer lastYear = lastElectedYear.get(mayorName);
        if (lastYear == null) return -1;
        return currentYear - lastYear;
    }

    @Getter
    @Setter
    public static class CandidateData {
        private String mayorName;
        private List<String> activePerks = new ArrayList<>();

        public CandidateData() {}

        public SkyBlockMayor getMayorEnum() {
            try {
                return SkyBlockMayor.valueOf(mayorName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public List<SkyBlockMayor.Perk> getActivePerkEnums() {
            List<SkyBlockMayor.Perk> result = new ArrayList<>();
            for (String perkName : activePerks) {
                try {
                    result.add(SkyBlockMayor.Perk.valueOf(perkName));
                } catch (IllegalArgumentException ignored) {}
            }
            return result;
        }

        public boolean hasMinisterPerkMarker(SkyBlockMayor.Perk perk) {
            return activePerks.contains(perk.name());
        }
    }
}
