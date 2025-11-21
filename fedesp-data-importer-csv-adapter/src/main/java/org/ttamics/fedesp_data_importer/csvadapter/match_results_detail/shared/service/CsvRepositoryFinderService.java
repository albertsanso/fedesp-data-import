package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service;

import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.service.SeasonRangeValidator;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.SeasonFolderInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
public class CsvRepositoryFinderService {
    public Optional<List<SeasonFolderInfo>> findAllSeasonsFoldersFrom(String baseFolder) throws IOException {

        Path baseFolderPath = Path.of(baseFolder);
        return Optional.of(Files.list(baseFolderPath)
                .filter(Files::isDirectory)
                .filter(seasonPath -> SeasonRangeValidator.isValidYearRange(seasonPath.getFileName().toString()))
                .map(seasonPath -> new SeasonFolderInfo(seasonPath.getFileName().toString(), seasonPath.toString()))
                .toList()
        ).filter(l -> !l.isEmpty());
    }
}
