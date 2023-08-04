package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.dao.DomainObject;
import cz.inqool.thesaurus.system.description.Description;
import cz.inqool.thesaurus.system.quote.Quote;
import cz.inqool.thesaurus.system.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Content extends DomainObject {

    @ManyToOne
    private Description description;

    private String text;

    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToMany(mappedBy = "content")
    private List<Quote> quotes = new ArrayList<>();
}
