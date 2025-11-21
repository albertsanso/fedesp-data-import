package org.ttamics.fedesp_data_importer.jpa.season_player_result.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.LicenseType;
import org.ttamics.fedesp_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.fedesp_data_importer.core.repository.SeasonPlayerResultRepository;
import org.ttamics.fedesp_data_importer.jpa.season_player_result.mapper.SeasonPlayerResultJPAToSeasonPlayerResultMapper;
import org.ttamics.fedesp_data_importer.jpa.season_player_result.mapper.SeasonPlayerResultToSeasonPlayerResultJPAMapper;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class SeasonPlayerResultRepositoryImpl implements SeasonPlayerResultRepository {

    private final SeasonPlayerResultRepositoryHelper helper;

    private final SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper;

    private final SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper;

    @Autowired
    public SeasonPlayerResultRepositoryImpl(SeasonPlayerResultRepositoryHelper helper, SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper, SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper) {
        this.helper = helper;
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public Optional<SeasonPlayerResult> findById(UUID id) {
        return helper.findById(id).map(fromJPAMapper);
    }

    @Override
    public Optional<SeasonPlayerResult> findFor(
            String season, String competition, String jornada, String group, String playerLetter, String matchLinkageId, UUID clubId) {
        return helper.findBySeasonAndCompetitionAndJornadaAndGroupAndPlayerLetterAndMatchLinkageIdAndSeasonPlayer_ClubMember_Club_Id(
                        season, competition,  jornada, group, playerLetter, matchLinkageId, clubId)
                .map(fromJPAMapper);
    }

    @Override
    public void save(SeasonPlayerResult seasonPlayerResult) {
        helper.save(toJPAMapper.apply(seasonPlayerResult));
    }
}