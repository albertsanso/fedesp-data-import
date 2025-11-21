package org.ttamics.fedesp_data_importer.jpa.season_player_result.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.ttamics.fedesp_data_importer.jpa.season_player.model.SeasonPlayerJPA;
import org.ttamics.fedesp_data_importer.shared.StringListConverter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="SeasonPlayerResult",
        schema="fedespadata",
        indexes = {
                @Index(name="idx_season_player_id", columnList = "season_player_id")
        }
)
public class SeasonPlayerResultJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "season", length = 9)
    private String season;

    @Column(name = "competition")
    private String competition;

    @Column
    private String jornada;

    @Column(name = "competition_group")
    private String group;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "season_player_id")
    private SeasonPlayerJPA seasonPlayer;

    @Column
    private String playerLetter;

    @Convert(converter = StringListConverter.class)
    @Column(name = "game_points", length = 500)
    private List<String> gamePoints;

    @Column
    private int gamesWon;

    @Column
    private String matchLinkageId;
}
