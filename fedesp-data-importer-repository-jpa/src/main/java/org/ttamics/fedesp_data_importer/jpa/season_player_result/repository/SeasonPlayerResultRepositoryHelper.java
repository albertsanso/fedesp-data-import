package org.ttamics.fedesp_data_importer.jpa.season_player_result.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.fedesp_data_importer.core.model.LicenseType;
import org.ttamics.fedesp_data_importer.jpa.season_player_result.model.SeasonPlayerResultJPA;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerResultRepositoryHelper extends JpaRepository<SeasonPlayerResultJPA, UUID>
{
    Optional<SeasonPlayerResultJPA> findBySeasonAndCompetitionAndJornadaAndGroupAndPlayerLetterAndMatchLinkageIdAndSeasonPlayer_ClubMember_Club_Id(
            String season,
            String competition,
            String jornada,
            String group,
            String playerLetter,
            String matchLinkageId,
            UUID clubId);
}
