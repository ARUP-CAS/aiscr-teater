package cz.inqool.thesaurus.system.source;

import cz.inqool.thesaurus.dao.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Source extends DomainObject {

    private String label;
    private String url;
}
