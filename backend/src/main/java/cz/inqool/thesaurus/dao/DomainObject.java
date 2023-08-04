package cz.inqool.thesaurus.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
abstract public class DomainObject {

    @Id
    @NotNull
    protected String id = UUID.randomUUID().toString();


    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + id;
    }
}
