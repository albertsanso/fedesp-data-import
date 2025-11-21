package org.ttamics.fedesp_data_importer.jpa.practicioner.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.Practicioner;
import org.ttamics.fedesp_data_importer.jpa.practicioner.model.PracticionerJPA;

import java.util.function.Function;

@Component
public class PracticionerJPAToPracticionerMapper implements Function<PracticionerJPA, Practicioner> {
    @Override
    public Practicioner apply(PracticionerJPA practicionerJPA) {
        return Practicioner.createExisting(
                practicionerJPA.getId(),
                practicionerJPA.getFirstName(),
                practicionerJPA.getSecondName(),
                practicionerJPA.getFullName(),
                practicionerJPA.getBirthDate()
        );
    }
}
