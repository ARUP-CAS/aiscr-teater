package cz.inqool.thesaurus.system.description;

import cz.inqool.thesaurus.dao.DomainObject;
import cz.inqool.thesaurus.system.category.Category;
import cz.inqool.thesaurus.system.content.Content;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Description extends DomainObject {

    @ManyToOne
    private Category category;

    private String titlePropertyKey;

    @OneToMany(mappedBy = "description")
    private List<Content> contents = new ArrayList<>();

}
