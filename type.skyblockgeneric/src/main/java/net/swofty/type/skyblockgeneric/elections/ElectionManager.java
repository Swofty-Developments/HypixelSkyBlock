package net.swofty.type.skyblockgeneric.elections;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.server.attribute.SkyBlockServerAttributes;
import net.swofty.type.skyblockgeneric.server.attribute.attributes.AttributeString;
import org.tinylog.Logger;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Arrays;
import java.util.UUID;

public class ElectionManager {
    private static final JsonMapper MAPPER = JsonMapper.builder()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    @Getter
    private static ElectionData electionData = new ElectionData();

    public static void loadFromAttribute() {
        try {
            SkyBlockServerAttributes attributes = new SkyBlockServerAttributes();
            AttributeString attr = attributes.get(
                    SkyBlockServerAttributes.Attributes.ELECTION_DATA, AttributeString.class);
            String json = attr.getValue();
            if (json != null && !json.isEmpty() && !json.equals("\"\"")) {
                electionData = MAPPER.readValue(json, ElectionData.class);
                if (electionData.getCurrentMayor() == null || electionData.getCurrentMinister() == null) {
                    Logger.warn("Election data is missing mayor or minister. Reinitializing.");
                    electionData = new ElectionData();
                    initializeFirstElection();
                }
            } else {
                electionData = new ElectionData();
                initializeFirstElection();
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load election data");
            electionData = new ElectionData();
            initializeFirstElection();
        }
    }

    public static String serializeData() {
        try {
            return MAPPER.writeValueAsString(electionData);
        } catch (Exception e) {
            Logger.error(e, "Failed to serialize election data");
            return "{}";
        }
    }

    private static void initializeFirstElection() {
        int currentYear = SkyBlockCalendar.getYear();
        electionData.setCurrentMayor(SkyBlockMayor.COLE.name());
        electionData.setCurrentMayorPerks(
                Arrays.stream(SkyBlockMayor.COLE.getAllPerks())
                        .map(Enum::name).toList()
        );
        electionData.setMayorElectedYear(currentYear);
        electionData.setCurrentMinister(SkyBlockMayor.DIAZ.name());
        electionData.setMinisterPerk(SkyBlockMayor.Perk.STOCK_EXCHANGE.name());
    }

    public static void onElectionStart() {
        int currentYear = SkyBlockCalendar.getYear();
        if (electionData.isElectionOpen()) return;

        electionData.startNewElection(currentYear);
        Logger.info("Election started for Year {}", currentYear);
    }

    public static void onElectionEnd() {
        if (!electionData.isElectionOpen()) return;

        int currentYear = SkyBlockCalendar.getYear();
        electionData.resolveElection(currentYear);
        Logger.info("Election resolved for Year {}. New Mayor: {}", currentYear, electionData.getCurrentMayor());
    }

    public static void castVote(UUID accountId, String candidateName) {
        if (!electionData.isElectionOpen()) return;

        boolean validCandidate = electionData.getCandidates().stream()
                .anyMatch(c -> c.getMayorName().equals(candidateName));
        if (!validCandidate) return;

        electionData.castVote(accountId, candidateName);
    }

    public static String getPlayerVote(UUID accountId) {
        return electionData.getVote(accountId);
    }

    public static SkyBlockMayor getCurrentMayor() {
        return electionData.getMayorEnum();
    }

    public static SkyBlockMayor getCurrentMinister() {
        return electionData.getMinisterEnum();
    }

    public static boolean isMayorPerkActive(SkyBlockMayor.Perk perk) {
        return electionData.getCurrentMayorPerkEnums().contains(perk);
    }

    public static boolean isMinisterPerkActive(SkyBlockMayor.Perk perk) {
        SkyBlockMayor.Perk activePerk = electionData.getMinisterPerkEnum();
        return activePerk != null && activePerk == perk;
    }

    public static boolean isPerkActive(SkyBlockMayor.Perk perk) {
        return isMayorPerkActive(perk) || isMinisterPerkActive(perk);
    }

    public static void checkElectionCycle() {
        int currentYear = SkyBlockCalendar.getYear();
        int currentMonth = SkyBlockCalendar.getMonth();

        if (currentMonth >= 6 && currentMonth <= 8 && !electionData.isElectionOpen()) {
            if (electionData.getElectionYear() != currentYear) {
                onElectionStart();
            }
        }

        if (currentMonth >= 9 && electionData.isElectionOpen()) {
            onElectionEnd();
        }
    }
}
