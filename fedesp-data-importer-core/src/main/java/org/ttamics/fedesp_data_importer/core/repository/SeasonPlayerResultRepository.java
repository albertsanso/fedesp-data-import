package org.ttamics.fedesp_data_importer.core.repository;

import org.ttamics.fedesp_data_importer.core.model.LicenseType;
import org.ttamics.fedesp_data_importer.core.model.SeasonPlayerResult;

import java.util.Optional;
import java.util.UUID;

public interface SeasonPlayerResultRepository {
    Optional<SeasonPlayerResult> findById(UUID id);
    Optional<SeasonPlayerResult> findFor(
            String season,
            String competition,
            String jornada,
            String group,
            String playerLetter,
            String matchLinkageId,
            UUID clubId
    );
    void save(SeasonPlayerResult seasonPlayerResult);
}
