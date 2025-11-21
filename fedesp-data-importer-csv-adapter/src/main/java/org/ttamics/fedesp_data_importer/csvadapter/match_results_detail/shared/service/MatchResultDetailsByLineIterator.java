package org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.fedesp_data_importer.csvadapter.match_results_detail.shared.model.SeasonFolderInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatchResultDetailsByLineIterator implements Iterator<MatchResultsDetailCsvFileRowInfo>, AutoCloseable {

    private static final String CSV_FILE_MATCHES_FOR_SEASON_GENDER_CATEGORY_GROUP_PATTERN_STRING = "rfetm-%s-(\\w+)-([a-zA-Z0-9-]+?)-group-(\\d+)-teamid-(\\d+)_matches\\.csv";

    private final CsvRepositoryFinderService csvRepositoryFinderService;

    private Queue<MatchResultsDetailCsvFileInfo> fileQueue = new LinkedList<>();
    private CSVReader currentReader;
    private String[] nextLine;
    private MatchResultsDetailCsvFileInfo currentInfo;

    @Autowired
    public MatchResultDetailsByLineIterator(CsvRepositoryFinderService csvRepositoryFinderService) {
        this.csvRepositoryFinderService = csvRepositoryFinderService;
    }

    @Override
    public void close() throws Exception {
        try {
            if (currentReader != null) currentReader.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public MatchResultsDetailCsvFileRowInfo next() {
        if (!hasNext()) throw new NoSuchElementException();
        String[] lineToReturn = nextLine;
        MatchResultsDetailCsvFileRowInfo rowInfoToReturn = new MatchResultsDetailCsvFileRowInfo(currentInfo, lineToReturn, UUID.randomUUID());
        advanceReader();
        return rowInfoToReturn;
    }

    public void resetAndLoadTextFilesForSeason(File dir, String seasonRange) throws IOException {
        fileQueue.clear();
        if (currentReader != null) currentReader.close();
        loadTextFilesForSeason(dir, seasonRange);
        advanceReader();
    }

    public void resetAndLoadTextFilesForAllSeasons(File dir) throws IOException {
        fileQueue.clear();
        if (currentReader != null) currentReader.close();
        loadTextFilesForAllSeasons(dir);
        advanceReader();
    }

    private void advanceReader() {
        try {
            while (currentReader == null || (nextLine = currentReader.readNext()) == null) {
                if (currentReader != null) currentReader.close();
                if (fileQueue.isEmpty()) {
                    nextLine = null;
                    return;
                }
                currentInfo = fileQueue.poll();
                currentReader = getReaderFromBufferedReader(new BufferedReader(new FileReader(currentInfo.csvFilepath().toFile())));

            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private CSVReader getReaderFromBufferedReader(BufferedReader bufferedReader) throws FileNotFoundException {
        return new CSVReaderBuilder(bufferedReader)
                .withSkipLines(1)
                .build();
    }

    private void loadTextFilesForSeason(File baseNatchesDetailsCsvFilesFolder, String seasonRange) throws IOException {
        processMatchesDetailsForSeason(baseNatchesDetailsCsvFilesFolder.toString(), seasonRange);
    }

    private void loadTextFilesForAllSeasons(File baseNatchesDetailsCsvFilesFolder) throws IOException {
        processMatchesDetailsForAllSeasons(baseNatchesDetailsCsvFilesFolder.toString());
    }

    private void processMatchesDetailsForAllSeasons(String baseNatchesDetailsCsvFilesFolder) throws IOException {
        csvRepositoryFinderService.findAllSeasonsFoldersFrom(baseNatchesDetailsCsvFilesFolder)
                .ifPresent(seasonFolderInfos ->
                        seasonFolderInfos.stream()
                                .forEach(this::processSeasonFolder)
                );
    }

    private void processMatchesDetailsForSeason(String baseNatchesDetailsCsvFilesFolder, String seasonRange) throws IOException {
        csvRepositoryFinderService.findAllSeasonsFoldersFrom(baseNatchesDetailsCsvFilesFolder)
                .ifPresent(seasonFolderInfos ->
                        seasonFolderInfos.stream()
                                .filter(seasonFolderInfo -> seasonFolderInfo.season().equals(seasonRange))
                                .forEach(this::processSeasonFolder)
                );
    }

    private void processSeasonFolder(SeasonFolderInfo seasonFolderInfo) {

        String fileNamePatternString = CSV_FILE_MATCHES_FOR_SEASON_GENDER_CATEGORY_GROUP_PATTERN_STRING.formatted(seasonFolderInfo.season());
        final Pattern csvFilePattern = Pattern.compile(fileNamePatternString);

        try {
            Files.list(Path.of(seasonFolderInfo.folder())).forEach(csvFilePath -> {
                Matcher matcher = csvFilePattern.matcher(csvFilePath.getFileName().toString());
                if (matcher.matches()) {
                    String gender = matcher.group(1);
                    String categoria = matcher.group(2);
                    String groupNumber = matcher.group(3);

                    MatchResultsDetailCsvFileInfo info = new MatchResultsDetailCsvFileInfo(
                            csvFilePath,
                            seasonFolderInfo.season(),
                            gender,
                            categoria,
                            groupNumber);
                    fileQueue.add(info);
                } else {
                    throw new RuntimeException("Wrong file name format for match results details: %s".formatted(csvFilePath));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String fileNamePatternString = "rfetm-%s-(\\w+)-([a-zA-Z0-9-]+?)-group-(\\d+)_matches\\.csv".formatted("2024-2025");
        System.out.println(fileNamePatternString);
        Pattern pattern = Pattern.compile(fileNamePatternString);
        Matcher matcher = pattern.matcher("rfetm-2024-2025-female-divisio-honor-group-1_matches.csv");
        if (matcher.matches()) {
            System.out.println("SII");
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
    }
}
