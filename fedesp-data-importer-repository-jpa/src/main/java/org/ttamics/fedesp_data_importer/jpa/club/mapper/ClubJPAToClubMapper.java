package org.ttamics.fedesp_data_importer.jpa.club.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.Club;
import org.ttamics.fedesp_data_importer.jpa.club.model.ClubJPA;

import java.util.Comparator;
import java.util.function.Function;

@Component
public class ClubJPAToClubMapper implements Function<ClubJPA, Club> {
    @Override
    public Club apply(ClubJPA clubJPA) {
        Club club = Club.createExisting(clubJPA.getId(), clubJPA.getName());

        clubJPA.getYearRanges().stream()
                .sorted(Comparator.comparing(r -> r.split("-")[0]))
                .forEach(club::addYearRange);

        return club;
    }
}