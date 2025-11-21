package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model;

import java.nio.file.Path;

public record MatchResultsDetailCsvFileInfo(Path csvFilepath,
                                            String season,
                                            String gender,
                                            String competition,
                                            String group) {
}
