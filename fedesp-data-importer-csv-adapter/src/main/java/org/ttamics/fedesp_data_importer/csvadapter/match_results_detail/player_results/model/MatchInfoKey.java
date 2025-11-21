package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.player_results.model;

public record MatchInfoKey (String season,
                            String competition,
                            String jornada,
                            String group,
                            String teamNameAbc,
                            String teamNameXyz) {
}
