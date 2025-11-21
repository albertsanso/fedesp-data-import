package org.ttamics.fedesp_data_importer.jpa.season_player.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.ttamics.fedesp_data_importer.core.model.LicenseType;
import org.ttamics.fedesp_data_importer.jpa.club_member.model.ClubMemberJPA;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="SeasonPlayer",
        schema="fedespadata",
        indexes = {
                @Index(name="idx_club_member_id", columnList="club_member_id")
        }
)
public class SeasonPlayerJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "club_member_id")
    private ClubMemberJPA clubMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type")
    private LicenseType licenseType;

    @Column(name = "license_ref", length = 100)
    private String licenseRef;

    @Column(name = "season", length = 9)
    private String yearRange;
}
