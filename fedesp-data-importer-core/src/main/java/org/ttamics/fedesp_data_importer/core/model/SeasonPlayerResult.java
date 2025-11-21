package org.ttamics.fedesp_data_importer.core.model;

import java.util.UUID;

public class SeasonPlayerResult {
    private final UUID id;
    private final String season;
    private final String competition;
    private final String jornada;
    private final String group;
    private final SeasonPlayer seasonPlayer;
    private final String playerLetter;
    private final int[] gamePoints;
    private final int gamesWon;
    private final String matchLinkageId;

    private SeasonPlayerResult(
            UUID id,
            String season,
            String competition,
            String jornada,
            String group,
            SeasonPlayer seasonPlayer,
            String playerLetter,
            int[] gamePoints,
            int gamesWon,
            String matchLinkageId) {
        this.id = id;
        this.season = season;
        this.competition = competition;
        this.jornada = jornada;
        this.group = group;
        this.seasonPlayer = seasonPlayer;
        this.playerLetter = playerLetter;
        this.gamePoints = gamePoints;
        this.gamesWon = gamesWon;
        this.matchLinkageId = matchLinkageId;
    }

    public static SeasonPlayerResult createNew(
            String season,
            String competition,
            String jornada,
            String group,
            SeasonPlayer seasonPlayer,
            String playerLetter,
            int[] gamePoints,
            int gamesWon,
            String matchLinkageId) {
        return new SeasonPlayerResult(
                UUID.randomUUID(),
                season,
                competition,
                jornada,
                group,
                seasonPlayer,
                playerLetter,
                gamePoints,
                gamesWon,
                matchLinkageId
        );
    }

    public static SeasonPlayerResult createExisting(
            UUID id,
            String season,
            String competition,
            String jornada,
            String group,
            SeasonPlayer seasonPlayer,
            String playerLetter,
            int[] gamePoints,
            int gamesWon,
            String matchLinkageId) {
        return new SeasonPlayerResult(
                id,
                season,
                competition,
                jornada,
                group,
                seasonPlayer,
                playerLetter,
                gamePoints,
                gamesWon,
                matchLinkageId);
    }

    public UUID getId() {
        return id;
    }

    public String getSeason() {
        return season;
    }

    public String getCompetition() {
        return competition;
    }

    public String getJornada() {
        return jornada;
    }

    public String getGroup() {
        return group;
    }

    public SeasonPlayer getSeasonPlayer() {
        return seasonPlayer;
    }

    public String getPlayerLetter() {
        return playerLetter;
    }

    public int[] getGamePoints() {
        return gamePoints;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public String getMatchLinkageId() {
        return matchLinkageId;
    }
}

