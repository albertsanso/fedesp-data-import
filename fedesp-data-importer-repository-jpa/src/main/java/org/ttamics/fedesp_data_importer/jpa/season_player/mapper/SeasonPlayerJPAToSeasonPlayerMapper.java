package org.ttamics.fedesp_data_importer.jpa.season_player.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.License;
import org.ttamics.fedesp_data_importer.core.model.SeasonPlayer;
import org.ttamics.fedesp_data_importer.jpa.club_member.mapper.ClubMemberJPAToClubMemberMapper;
import org.ttamics.fedesp_data_importer.jpa.season_player.model.SeasonPlayerJPA;

import java.util.function.Function;

@Component
public class SeasonPlayerJPAToSeasonPlayerMapper implements Function<SeasonPlayerJPA, SeasonPlayer> {

    private final ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper;

    @Autowired
    public SeasonPlayerJPAToSeasonPlayerMapper(ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper) {
        this.clubMemberJPAToClubMemberMapper = clubMemberJPAToClubMemberMapper;
    }

    @Override
    public SeasonPlayer apply(SeasonPlayerJPA seasonPlayerJPA) {
        return SeasonPlayer.createExisting(
                seasonPlayerJPA.getId(),
                clubMemberJPAToClubMemberMapper.apply(seasonPlayerJPA.getClubMember()),
                License.createCatalanaLicenseOf(seasonPlayerJPA.getLicenseRef()),
                seasonPlayerJPA.getYearRange()
        );
    }
}