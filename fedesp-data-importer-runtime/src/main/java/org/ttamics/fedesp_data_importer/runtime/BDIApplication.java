package org.ttamics.fedesp_data_importer.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.club.service.ClubImportService;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.player_results.service.PlayerAndResultsImportService;

@SpringBootApplication(scanBasePackages = {
        "org.ttamics.fedesp_data_importer.core",
        "org.ttamics.fedesp_data_importer.csvadapter",
        "org.ttamics.fedesp_data_importer.jpa"
})
@EnableJpaRepositories(basePackages = "org.ttamics.fedesp_data_importer.jpa")
@EntityScan(basePackages = "org.ttamics.fedesp_data_importer.jpa")
public class BDIApplication implements CommandLineRunner {

    @Autowired
    private ClubImportService clubImportService;

    @Autowired
    private PlayerAndResultsImportService playerAndResultsImportService;

    public static void main(String[] args) {
        SpringApplication.run(BDIApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String baseFolder = "C:\\git\\fedesp-data-csv\\resources\\match-results-details";

        //clubImportService.processClubNamesForSeason(baseFolder, "2024-2025");
        //clubImportService.processClubNamesForAllSeasons(baseFolder);

        //playerAndResultsImportService.processForSeason(baseFolder, "2024-2025");
        //playerAndResultsImportService.processForSeason(baseFolder, "2023-2024");
        //playerAndResultsImportService.processForSeason(baseFolder, "2022-2023");
        //playerAndResultsImportService.processForSeason(baseFolder, "2021-2022");
        //playerAndResultsImportService.processForSeason(baseFolder, "2020-2021");
        //playerAndResultsImportService.processForSeason(baseFolder, "2019-2020");
        playerAndResultsImportService.processForSeason(baseFolder, "2018-2019");

        System.out.println("Hello.");
    }
}
