package org.ttamics.fedesp_data_importer.jpa.practicioner.repository;

import org.springframework.data.repository.CrudRepository;
import org.ttamics.fedesp_data_importer.jpa.practicioner.model.PracticionerJPA;

import java.util.Optional;

public interface PracticionerRepositoryHelper extends CrudRepository<PracticionerJPA, String> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}