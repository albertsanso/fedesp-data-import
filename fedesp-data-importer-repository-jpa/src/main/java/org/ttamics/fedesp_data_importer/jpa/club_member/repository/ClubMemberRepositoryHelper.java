package org.ttamics.fedesp_data_importer.jpa.club_member.repository;

import org.springframework.data.repository.CrudRepository;
import org.ttamics.fedesp_data_importer.jpa.club_member.model.ClubMemberJPA;

import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepositoryHelper extends CrudRepository<ClubMemberJPA, String> {
    Optional<ClubMemberJPA> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
}
