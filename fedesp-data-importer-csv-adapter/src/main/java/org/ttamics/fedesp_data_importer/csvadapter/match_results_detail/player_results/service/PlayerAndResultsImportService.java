package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.player_results.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.Club;
import org.ttamics.fedesp_data_importer.core.model.ClubMember;
import org.ttamics.fedesp_data_importer.core.model.License;
import org.ttamics.fedesp_data_importer.core.model.MatchContext;
import org.ttamics.fedesp_data_importer.core.model.PlayersSingleMatch;
import org.ttamics.fedesp_data_importer.core.model.Practicioner;
import org.ttamics.fedesp_data_importer.core.model.SeasonPlayer;
import org.ttamics.fedesp_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.fedesp_data_importer.core.repository.ClubMemberRepository;
import org.ttamics.fedesp_data_importer.core.repository.ClubRepository;
import org.ttamics.fedesp_data_importer.core.repository.PlayersSingleMatchRepository;
import org.ttamics.fedesp_data_importer.core.repository.PracticionerRepository;
import org.ttamics.fedesp_data_importer.core.repository.SeasonPlayerRepository;
import org.ttamics.fedesp_data_importer.core.repository.SeasonPlayerResultRepository;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.player_results.model.MatchInfoKey;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service.CsvFileRowInfoExtractor;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service.LineByLineInitialImportService;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service.MatchResultDetailsByLineIterator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PlayerAndResultsImportService extends LineByLineInitialImportService {

    private final ClubRepository clubRepository;

    private final CsvFileRowInfoExtractor rowInfoExtractor;

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    private final ClubMemberRepository clubMemberRepository;

    private final PracticionerRepository practicionerRepository;

    private final SeasonPlayerRepository seasonPlayerRepository;

    private final SeasonPlayerResultRepository seasonPlayerResultRepository;

    private final PlayersSingleMatchRepository playersSingleMatchRepository;

    @Autowired
    public PlayerAndResultsImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator, ClubRepository clubRepository, CsvFileRowInfoExtractor rowInfoExtractor, ClubMemberRepository clubMemberRepository, PracticionerRepository practicionerRepository, SeasonPlayerRepository seasonPlayerRepository, SeasonPlayerResultRepository seasonPlayerResultRepository, PlayersSingleMatchRepository playersSingleMatchRepository) {
        super(matchResultDetailsByLineIterator);
        this.clubRepository = clubRepository;
        this.rowInfoExtractor = rowInfoExtractor;
        this.clubMemberRepository = clubMemberRepository;
        this.practicionerRepository = practicionerRepository;
        this.seasonPlayerRepository = seasonPlayerRepository;
        this.seasonPlayerResultRepository = seasonPlayerResultRepository;
        this.playersSingleMatchRepository = playersSingleMatchRepository;
    }

    public void processForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        resetAndLoadTextFilesForSeason(baseSeasonsFolder, seasonRange);
        importMatchResultsDetailsInfo();
    }

    public void processForAllSeasons(String baseSeasonsFolder) throws IOException {
        resetAndLoadTextFilesForAllSeasons(baseSeasonsFolder);
        importMatchResultsDetailsInfo();
    }

    public void importMatchResultsDetailsInfo() {
        List<MatchResultsDetailCsvFileRowInfo> rowInfowsList = fetchCsvRowInfos();
        processMatchResultsDetailsInfo(rowInfowsList);
    }

    private void processMatchResultsDetailsInfo(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        List<Club> allClubsList = clubRepository.findAll();
        Map<MatchInfoKey, List<SeasonPlayerResult>> mapOfMatchesList = new HashMap<>();

        matchResultsDetailCsvFileRowInfoList.forEach(matchResultsDetailCsvFileRowInfo -> {

            String season = matchResultsDetailCsvFileRowInfo.fileInfo().season();
            String gender = matchResultsDetailCsvFileRowInfo.fileInfo().gender();
            String competition = matchResultsDetailCsvFileRowInfo.fileInfo().competition();
            String group = matchResultsDetailCsvFileRowInfo.fileInfo().group();

            MatchResultsDetailRowInfo rowInfo = rowInfoExtractor.extractMatchDetailsRowInfo(matchResultsDetailCsvFileRowInfo);

            String teamNameABC = rowInfo.acbPlayer().teamName();
            String teamNameXYZ = rowInfo.xyzPlayer().teamName();
            int jornada = rowInfo.jornada();
            String fullTeamABCId = season + competition + jornada + group + teamNameABC; // NOT REALLY USED
            String fullTeamXYZId = season + competition + jornada + group + teamNameXYZ; // NOT REALLY USED

            //String gameId = rowInfo.acbPlayer().playerLetter()+"-"+rowInfo.xyzPlayer().playerLetter();
            String gameId = season+"-"+competition+"-"+jornada+"-"+group+"-"+rowInfo.acbPlayer().playerLicense()+"-"+rowInfo.xyzPlayer().playerLicense();
            MatchInfoKey matchInfoKey = new MatchInfoKey(
                    season,
                    competition,
                    ""+jornada,
                    group,
                    rowInfo.acbPlayer().teamName(),
                    rowInfo.xyzPlayer().teamName());

            //checkScores(rowInfo.acbPlayer(), rowInfo.xyzPlayer(), matchResultsDetailCsvFileRowInfo.fileInfo().csvFilepath());

            SeasonPlayerResult seasonPlayerResultAbc = createSeasonPlayerAndResults(rowInfo.acbPlayer(), allClubsList, season, gameId, fullTeamABCId, matchInfoKey, mapOfMatchesList, matchResultsDetailCsvFileRowInfo);
            SeasonPlayerResult seasonPlayerResultXyz = createSeasonPlayerAndResults(rowInfo.xyzPlayer(), allClubsList, season, gameId, fullTeamXYZId, matchInfoKey, mapOfMatchesList, matchResultsDetailCsvFileRowInfo);

            String uniqueRowId = "%s-%s-%s-%s-%s-%s-%s-%s-%s-%s".formatted(
                    competition.strip(),
                    season.strip(),
                    group,
                    String.valueOf(jornada),
                    teamNameABC.strip(),
                    seasonPlayerResultAbc.getSeasonPlayer().getLicense().getLicenseId().strip(),
                    seasonPlayerResultAbc.getPlayerLetter().strip(),
                    teamNameXYZ.strip(),
                    seasonPlayerResultXyz.getSeasonPlayer().getLicense().getLicenseId().strip(),
                    seasonPlayerResultXyz.getPlayerLetter().strip()
            );

            Optional<PlayersSingleMatch> optPlayersSingleMatch = playersSingleMatchRepository.findBySeasonPlayerResultAbcIdAndSeasonPlayerResultXyzIdAndUniqueId(seasonPlayerResultAbc.getId(), seasonPlayerResultXyz.getId(), uniqueRowId);
            if (optPlayersSingleMatch.isEmpty()) {
                MatchContext matchContext = new MatchContext(season, "", competition, gender, ""+jornada, group);
                PlayersSingleMatch playersSingleMatch = PlayersSingleMatch.createNew(
                        seasonPlayerResultAbc,
                        seasonPlayerResultXyz,
                        matchContext,
                        uniqueRowId
                );
                playersSingleMatchRepository.save(playersSingleMatch);
            }

        });
    }

    private void checkScores(PlayerCsvInfo abcPlayer, PlayerCsvInfo xyzPlayer, Path path) {
        if (abcPlayer.playerScore() != 3 && xyzPlayer.playerScore() != 3) {
            throw new IllegalStateException(String.format("Wrong scores: one must reach 3 at least! %s, %s, %s", abcPlayer.playerName(), xyzPlayer.playerName(), path.getFileName()));
        }
    }

    private SeasonPlayerResult createSeasonPlayerAndResults(PlayerCsvInfo playerInfo, List<Club> allClubsList, String seasonRange, String uniqueRowId, String fullTeamId, MatchInfoKey matchInfoKey, Map<MatchInfoKey, List<SeasonPlayerResult>> mapOfMatchesList, MatchResultsDetailCsvFileRowInfo matchResultsDetailCsvFileRowInfo) {
        Optional<Club> optInferredClub = inferClubByTeamName(playerInfo.teamName(), allClubsList);
        SeasonPlayerResult seasonPlayerResult = null;
        if (optInferredClub.isPresent()) {
            Club club = optInferredClub.get();
            seasonPlayerResult = createSeasonPlayerAndResultsForClub(club, playerInfo, seasonRange, uniqueRowId, fullTeamId, matchInfoKey, mapOfMatchesList, matchResultsDetailCsvFileRowInfo);
        } else {

            System.out.println("UNABLE TO INFER CLUB BY TEAM NAME: "+playerInfo.teamName());
            System.out.println("  > "+matchResultsDetailCsvFileRowInfo.fileInfo().csvFilepath());
        }
        return seasonPlayerResult;
    }

    private Optional<Club> inferClubByTeamName(String teamName, List<Club> allClubsList) {
        String normalizedInput = normalize(teamName);
        return allClubsList.stream()
                .min(Comparator.comparingInt(club -> levenshtein.apply(normalizedInput, normalize(club.getName()))));
    }

    private SeasonPlayerResult createSeasonPlayerAndResultsForClub(Club inferredClub, PlayerCsvInfo playerInfo, String seasonRange, String uniqueRowId, String fullTeamId, MatchInfoKey matchInfoKey, Map<MatchInfoKey, List<SeasonPlayerResult>> mapOfMatchesList, MatchResultsDetailCsvFileRowInfo matchResultsDetailCsvFileRowInfo) {
        SeasonPlayerResult seasonPlayerResult = null;
        Optional<Club> optClub = clubRepository.findByName(inferredClub.getName());
        if (optClub.isPresent()) {
            Club club = optClub.get();

            Practicioner practicioner = getOrCreatePracticionerFromPlayerInfo(playerInfo);
            ClubMember clubMember = getOrCreateClubMember(club, practicioner, seasonRange);
            SeasonPlayer seasonPlayer = getOrCreateSeasonPlayer(playerInfo, club, clubMember, seasonRange);


            seasonPlayerResult = getOrCreateSeasonPlayerResult(seasonRange, matchInfoKey, playerInfo, seasonPlayer, uniqueRowId);
            addSeasonPlayerResultToMap(seasonPlayerResult, matchInfoKey.teamNameAbc(), matchInfoKey.teamNameXyz(), mapOfMatchesList);

        } else {
            System.out.println("UNABLE TO FIND CLUB BY TEAM NAME: "+inferredClub.getName());
            System.out.println("  > "+matchResultsDetailCsvFileRowInfo.fileInfo().csvFilepath());

        }
        return seasonPlayerResult;
    }

    private SeasonPlayerResult getOrCreateSeasonPlayerResult(String seasonRange, MatchInfoKey matchInfoKey, PlayerCsvInfo playerInfo, SeasonPlayer seasonPlayer, String uniqueRowId) {
        SeasonPlayerResult seasonPlayerResult = seasonPlayerResultRepository
                .findFor(
                        seasonRange,
                        matchInfoKey.competition(),
                        matchInfoKey.jornada(),
                        matchInfoKey.group(),
                        playerInfo.playerLetter(),
                        uniqueRowId,
                        seasonPlayer.getClub().getId()
                )
                /*.map(existing -> {
                    System.out.println("--------------> Existing: "+existing);
                    return existing;
                })*/
                .orElseGet(() -> SeasonPlayerResult.createNew(
                            seasonRange,
                            matchInfoKey.competition(),
                            matchInfoKey.jornada(),
                            matchInfoKey.group(),
                            seasonPlayer,
                            playerInfo.playerLetter(),
                            new int[]{},
                            playerInfo.playerScore(),
                            uniqueRowId
                    ));
        seasonPlayerResultRepository.save(seasonPlayerResult);
        return seasonPlayerResult;
    }

    private void addSeasonPlayerResultToMap(SeasonPlayerResult seasonPlayerResult, String abcTeamName, String xyzTeamName,Map<MatchInfoKey, List<SeasonPlayerResult>> mapOfMatchesList) {
        MatchInfoKey matchInfoKey = new MatchInfoKey(
                seasonPlayerResult.getSeason(),
                seasonPlayerResult.getCompetition(),
                seasonPlayerResult.getJornada(),
                seasonPlayerResult.getGroup(),
                abcTeamName,
                xyzTeamName
        );

        List<SeasonPlayerResult> seasonPlayerResultsList;
        if (!mapOfMatchesList.keySet().contains(matchInfoKey)) {
            seasonPlayerResultsList = new ArrayList<SeasonPlayerResult>();
            mapOfMatchesList.put(matchInfoKey, seasonPlayerResultsList);
        } else {
            seasonPlayerResultsList = mapOfMatchesList.get(matchInfoKey);
        }
        seasonPlayerResultsList.add(seasonPlayerResult);
    }

    private Practicioner getOrCreatePracticionerFromPlayerInfo(PlayerCsvInfo playerInfo) {
        String[] firstAndSecondNames = splitIntoFirstNameAndSecondName(playerInfo.playerName());
        String firstName = firstAndSecondNames[0];
        String secondName = firstAndSecondNames[1];

        Optional<Practicioner> optPracticionerFromPlayer = practicionerRepository.findByFullName(playerInfo.playerName());
        Practicioner practicionerFromPlayer = optPracticionerFromPlayer.orElseGet(() -> Practicioner.createNew(
                firstName,
                secondName,
                playerInfo.playerName(),
                null));
        practicionerRepository.save(practicionerFromPlayer);
        return practicionerFromPlayer;
    }

    private ClubMember getOrCreateClubMember(Club club, Practicioner practicioner, String seasonRange) {
        Optional<ClubMember> optClubMember = clubMemberRepository.findByPracticionerIdAndClubId(practicioner.getId(), club.getId());
        ClubMember clubMember = optClubMember.orElseGet(() -> ClubMember.createNew(
                club,
                practicioner
        ));
        clubMember.addYearRange(seasonRange);
        clubMemberRepository.save(clubMember);
        return clubMember;
    }

    private SeasonPlayer getOrCreateSeasonPlayer(PlayerCsvInfo playerInfo, Club club, ClubMember clubMember, String seasonRange) {
        SeasonPlayer seasonPlayer = seasonPlayerRepository
                //.findByPracticionerIdClubIdSeason(practicionerFromPlayer.getId(), clubMember.getClub().getId(), seasonRange)
                .findByPracticionerNameAndClubNameAndSeason(playerInfo.playerName(), club.getName(), seasonRange)
                .orElseGet(() -> SeasonPlayer.createNew(
                        clubMember,
                        License.createCatalanaLicenseOf(playerInfo.playerLicense()),
                        seasonRange
                ));
        seasonPlayerRepository.save(seasonPlayer);
        return seasonPlayer;
    }

    private String[] splitIntoFirstNameAndSecondName(String input) {
        String[] words = input.split("\\s+");
        List<String> upperWords = new ArrayList<>();
        List<String> lowerWords = new ArrayList<>();

        for (String word : words) {
            if (word.equals(word.toUpperCase())) {
                // Entire word is uppercase
                upperWords.add(word);
            } else {
                // Mixed or lowercase word
                lowerWords.add(word);
            }
        }

        String secondName = String.join(" ", upperWords);
        String firstName = String.join(" ", lowerWords);
        return new String[] {firstName, secondName};
    }
    private String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]", "") // remove spaces/punctuation
                .replace("fc", "");          // remove 'fc' if needed
    }
}
