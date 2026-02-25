package net.swofty.type.skyblockgeneric.elections;

import com.google.gson.Gson;
import lombok.Getter;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.election.CastVoteProtocolObject;
import net.swofty.commons.protocol.objects.election.GetCandidatesProtocolObject;
import net.swofty.commons.protocol.objects.election.GetElectionDataProtocolObject;
import net.swofty.commons.protocol.objects.election.SaveElectionDataProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ElectionManager {

    private static final Gson GSON = new Gson();
    private static ProxyService SERVICE;

    @Getter
    private static ElectionData electionData = new ElectionData();

    public static void loadFromService() {
        SERVICE = new ProxyService(ServiceType.ELECTION);
        try {
            GetElectionDataProtocolObject.GetElectionDataResponse response =
                    SERVICE.<GetElectionDataProtocolObject.GetElectionDataMessage,
                            GetElectionDataProtocolObject.GetElectionDataResponse>handleRequest(
                            new GetElectionDataProtocolObject.GetElectionDataMessage()
                    ).join();

            if (response.found() && response.serializedData() != null) {
                electionData = GSON.fromJson(response.serializedData(), ElectionData.class);
                if (electionData.getCurrentMayor() == null || electionData.getCurrentMinister() == null) {
                    electionData = new ElectionData();
                    initializeFirstElection();
                    saveToService();
                    Logger.info("Election data was missing mayor or minister info. Reinitialized first election.");
                }
            } else {
                electionData = new ElectionData();
                initializeFirstElection();
                saveToService();
                Logger.info("Election data was missing mayor or minister info.");
            }
        } catch (Exception e) {
            Logger.error(e, "Failed to load election data from service");
            electionData = new ElectionData();
            initializeFirstElection();
        }
    }

    public static void saveToService() {
        try {
            String serialized = GSON.toJson(electionData);
            SERVICE.<SaveElectionDataProtocolObject.SaveElectionDataMessage,
                    SaveElectionDataProtocolObject.SaveElectionDataResponse>handleRequest(
                    new SaveElectionDataProtocolObject.SaveElectionDataMessage(serialized)
            );
        } catch (Exception e) {
            Logger.error(e, "Failed to save election data to service");
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
        saveToService();
        Logger.info("Election started for Year {}", currentYear);
    }

    public static void onElectionEnd() {
        if (!electionData.isElectionOpen()) return;

        int currentYear = SkyBlockCalendar.getYear();
        electionData.resolveElection(currentYear);
        saveToService();
        Logger.info("Election resolved for Year {}. New Mayor: {}", currentYear, electionData.getCurrentMayor());
    }

    public static CompletableFuture<Void> castVote(UUID accountId, String candidateName) {
        if (!electionData.isElectionOpen()) return CompletableFuture.completedFuture(null);

        boolean validCandidate = electionData.getCandidates().stream()
                .anyMatch(c -> c.getMayorName().equals(candidateName));
        if (!validCandidate) return CompletableFuture.completedFuture(null);

        electionData.castVote(accountId, candidateName);

        return SERVICE.<CastVoteProtocolObject.CastVoteMessage,
                CastVoteProtocolObject.CastVoteResponse>handleRequest(
                new CastVoteProtocolObject.CastVoteMessage(accountId, candidateName)
        ).thenAccept(response -> {
            if (response.success() && response.serializedData() != null) {
                electionData = GSON.fromJson(response.serializedData(), ElectionData.class);
            }
        });
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

    public static CompletableFuture<List<GetCandidatesProtocolObject.CandidateInfo>> fetchCandidates() {
        return SERVICE.<GetCandidatesProtocolObject.GetCandidatesMessage,
                GetCandidatesProtocolObject.GetCandidatesResponse>handleRequest(
                new GetCandidatesProtocolObject.GetCandidatesMessage()
        ).thenApply(GetCandidatesProtocolObject.GetCandidatesResponse::candidates);
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
