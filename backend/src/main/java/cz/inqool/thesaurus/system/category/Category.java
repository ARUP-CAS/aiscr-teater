package cz.inqool.thesaurus.system.category;

import com.querydsl.core.annotations.QueryInit;
import cz.inqool.thesaurus.dao.DomainObject;
import cz.inqool.thesaurus.dao.Projection;
import cz.inqool.thesaurus.system.description.Description;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends DomainObject implements Projection<Category, Category, QCategory> {

    @AttributeOverrides(value = {
            @AttributeOverride(name = "cs", column = @Column(name = "name_cs")),
            @AttributeOverride(name = "en", column = @Column(name = "name_en")),
            @AttributeOverride(name = "de", column = @Column(name = "name_de"))
    })
    @Embedded
    private CategoryName name;

    private String dateFrom;
    private String dateTo;
    private String dateAccurate;

    private String url;

    private boolean leaf;

    @QueryInit("parent.parent.id")
    @ManyToOne
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Description> descriptions = new ArrayList<>();

}