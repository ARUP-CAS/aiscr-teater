package cz.inqool.thesaurus.system.quote;

import cz.inqool.thesaurus.dao.DomainObject;
import cz.inqool.thesaurus.system.source.Source;
import cz.inqool.thesaurus.system.content.Content;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Quote extends DomainObject {

    private String title;
    private String date;
    private String locationPage;
    private String locationUrl;

    @ManyToOne
    private Content content;
    @ManyToOne
    private Source source;
}
