package org.ttamics.fedesp_data_importer.core.model;

public class MatchContext {
    private final String season;

    private final String competitionType;

    private final String competition;

    private final String gender;

    private final String jornada;

    private final String group;

    public MatchContext(String season, String competitionType, String competition, String gender, String jornada, String group) {
        this.season = season;
        this.competitionType = competitionType;
        this.competition = competition;
        this.gender = gender;
        this.jornada = jornada;
        this.group = group;
    }

    public String getSeason() {
        return season;
    }

    public String getCompetitionType() {
        return competitionType;
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

    public String getGender() {
        return gender;
    }
}
