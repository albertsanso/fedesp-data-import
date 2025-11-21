package org.ttamics.fedesp_data_importer.jpa.club.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.fedesp_data_importer.jpa.club.model.ClubJPA;

import java.util.Optional;

@Repository
public interface ClubRepositoryHelper extends CrudRepository<ClubJPA, String> {
    Optional<ClubJPA> findByName(String name);
    boolean existsByName(String name);
}
