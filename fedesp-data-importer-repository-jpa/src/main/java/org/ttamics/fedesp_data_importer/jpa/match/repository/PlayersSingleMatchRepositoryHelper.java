package org.ttamics.fedesp_data_importer.jpa.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.fedesp_data_importer.jpa.match.model.PlayersSingleMatchJPA;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayersSingleMatchRepositoryHelper extends JpaRepository<PlayersSingleMatchJPA, UUID> {
    Optional<PlayersSingleMatchJPA> findBySeasonPlayerResultAbc_IdAndSeasonPlayerResultXyz_IdAndUniqueRowMatchId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueRowMatchId);
}
