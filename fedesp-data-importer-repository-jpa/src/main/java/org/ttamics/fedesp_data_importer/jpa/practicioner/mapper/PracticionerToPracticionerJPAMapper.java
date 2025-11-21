package org.ttamics.fedesp_data_importer.jpa.practicioner.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.fedesp_data_importer.core.model.Practicioner;
import org.ttamics.fedesp_data_importer.jpa.practicioner.model.PracticionerJPA;

import java.util.function.Function;

@Component
public class PracticionerToPracticionerJPAMapper implements Function<Practicioner, PracticionerJPA> {
    @Override
    public PracticionerJPA apply(Practicioner practicioner) {
        PracticionerJPA practicionerJPA = new PracticionerJPA();
        practicionerJPA.setId(practicioner.getId());
        practicionerJPA.setFirstName(practicioner.getFirstName());
        practicionerJPA.setSecondName(practicioner.getSecondName());
        practicionerJPA.setFullName(practicioner.getFullName());
        practicionerJPA.setBirthDate(practicioner.getBirthDate());
        return practicionerJPA;
    }
}
