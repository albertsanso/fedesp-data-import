package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model;

public record MatchResultsDetailRowInfo(
        PlayerCsvInfo acbPlayer,
        PlayerCsvInfo xyzPlayer,
        int jornada,
        String modalidad) {
}
