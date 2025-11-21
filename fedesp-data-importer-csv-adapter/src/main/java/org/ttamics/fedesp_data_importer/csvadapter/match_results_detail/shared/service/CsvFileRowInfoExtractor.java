package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service;

import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;

@Component
public class CsvFileRowInfoExtractor {

    public String extractTeamNameFromRowInfo(MatchResultsDetailCsvFileRowInfo rowInfo) {
        return rowInfo.rowInfo()[3];
    }

    public MatchResultsDetailRowInfo extractMatchDetailsRowInfo(MatchResultsDetailCsvFileRowInfo rowInfo) {
        PlayerCsvInfo abcPlayer = parsePlayerABC(rowInfo);
        PlayerCsvInfo xyzPlayer = parsePlayerXYZ(rowInfo);
        int jornada = Integer.parseInt(rowInfo.rowInfo()[1]);
        String modalidad = rowInfo.rowInfo()[0];
        return new MatchResultsDetailRowInfo(abcPlayer, xyzPlayer, jornada, modalidad);
    }

    private PlayerCsvInfo parsePlayerABC(MatchResultsDetailCsvFileRowInfo rowInfo) {
        if (rowInfo.rowInfo().length > 23) {
            System.out.println();
        }
        return new PlayerCsvInfo(
                rowInfo.rowInfo()[3],
                rowInfo.rowInfo()[8],
                rowInfo.rowInfo()[7],
                rowInfo.rowInfo()[6],
                Integer.parseInt(rowInfo.rowInfo()[21].split("-")[0])
        );
    }

    private PlayerCsvInfo parsePlayerXYZ(MatchResultsDetailCsvFileRowInfo rowInfo) {
        return new PlayerCsvInfo(
                rowInfo.rowInfo()[5],
                rowInfo.rowInfo()[11],
                rowInfo.rowInfo()[10],
                rowInfo.rowInfo()[9],
                Integer.parseInt(rowInfo.rowInfo()[21].split("-")[1])
        );
    }
}
