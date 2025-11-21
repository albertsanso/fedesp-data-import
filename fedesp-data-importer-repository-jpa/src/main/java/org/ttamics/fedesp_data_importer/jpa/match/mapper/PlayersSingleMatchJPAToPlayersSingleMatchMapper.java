package org.ttamics.fedesp_data_importer.jpa.match.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.MatchContext;
import org.ttamics.fedesp_data_importer.core.model.PlayersSingleMatch;
import org.ttamics.fedesp_data_importer.jpa.season_player_result.mapper.SeasonPlayerResultJPAToSeasonPlayerResultMapper;
import org.ttamics.fedesp_data_importer.jpa.match.model.PlayersSingleMatchJPA;

import java.util.function.Function;

@Component
public class PlayersSingleMatchJPAToPlayersSingleMatchMapper implements Function<PlayersSingleMatchJPA, PlayersSingleMatch> {

    private final SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper;

    @Autowired
    public PlayersSingleMatchJPAToPlayersSingleMatchMapper(SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper) {
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public PlayersSingleMatch apply(PlayersSingleMatchJPA playersSingleMatchJPA) {
        MatchContext matchContext = new MatchContext(
                playersSingleMatchJPA.getSeason(),
                playersSingleMatchJPA.getCompetitionType(),
                playersSingleMatchJPA.getCompetition(),
                playersSingleMatchJPA.getGender(),
                playersSingleMatchJPA.getJornada(),
                playersSingleMatchJPA.getGroup()
        );

        return PlayersSingleMatch.createExisting(
                playersSingleMatchJPA.getId(),
                fromJPAMapper.apply(playersSingleMatchJPA.getSeasonPlayerResultAbc()),
                fromJPAMapper.apply(playersSingleMatchJPA.getSeasonPlayerResultXyz()),
                matchContext,
                playersSingleMatchJPA.getUniqueRowMatchId()
        );
    }
}