package org.ttamics.fedesp_data_importer.jpa.match.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.PlayersSingleMatch;
import org.ttamics.fedesp_data_importer.jpa.season_player_result.mapper.SeasonPlayerResultToSeasonPlayerResultJPAMapper;
import org.ttamics.fedesp_data_importer.jpa.match.model.PlayersSingleMatchJPA;

import java.util.function.Function;

@Component
public class PlayersSingleMatchToPlayersSingleMatchJPAMapper implements Function<PlayersSingleMatch, PlayersSingleMatchJPA> {

    private final SeasonPlayerResultToSeasonPlayerResultJPAMapper toJpaMapper;

    @Autowired
    public PlayersSingleMatchToPlayersSingleMatchJPAMapper(SeasonPlayerResultToSeasonPlayerResultJPAMapper toJpaMapper) {
        this.toJpaMapper = toJpaMapper;
    }

    @Override
    public PlayersSingleMatchJPA apply(PlayersSingleMatch playersSingleMatch) {
        PlayersSingleMatchJPA playersSingleMatchJPA = new PlayersSingleMatchJPA();
        playersSingleMatchJPA.setId(playersSingleMatch.getId());
        playersSingleMatchJPA.setSeasonPlayerResultAbc(toJpaMapper.apply(playersSingleMatch.getSeasonPlayerResultAbc()));
        playersSingleMatchJPA.setSeasonPlayerResultXyz(toJpaMapper.apply(playersSingleMatch.getSeasonPlayerResultXyz()));
        playersSingleMatchJPA.setSeason(playersSingleMatch.getMatchContext().getSeason());
        playersSingleMatchJPA.setCompetitionType(playersSingleMatch.getMatchContext().getCompetitionType());
        playersSingleMatchJPA.setCompetition(playersSingleMatch.getMatchContext().getCompetition());
        playersSingleMatchJPA.setJornada(playersSingleMatch.getMatchContext().getJornada());
        playersSingleMatchJPA.setGroup(playersSingleMatch.getMatchContext().getGroup());
        playersSingleMatchJPA.setUniqueRowMatchId(playersSingleMatch.getUniqueRowMatchId());
        playersSingleMatchJPA.setGender(playersSingleMatch.getMatchContext().getGender());
        return playersSingleMatchJPA;
    }
}