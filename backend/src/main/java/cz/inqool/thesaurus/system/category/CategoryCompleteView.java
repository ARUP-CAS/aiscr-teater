package cz.inqool.thesaurus.system.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryInit;
import cz.inqool.thesaurus.dao.AbstractView;
import cz.inqool.thesaurus.dao.EntityProjection;
import cz.inqool.thesaurus.system.description.Description;
import cz.inqool.thesaurus.system.description.DescriptionCompleteView;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Immutable
@Entity(name = "category_complete")
@Table(name = "category")
public class CategoryCompleteView extends AbstractView<Category, CategoryCompleteView, QCategoryCompleteView> {

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

    @JsonIgnore
    @ManyToOne
    private Category parent;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<CategoryCompleteView> children = new ArrayList<>();

    @JsonIgnoreProperties(value = {"category"})
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<DescriptionCompleteView> descriptions = new ArrayList<>();

    @Override
    public Category toEntity() {
        Category entity = super.toEntity(new Category());
        entity.setName(name);
        entity.setDateFrom(dateFrom);
        entity.setDateTo(dateTo);
        entity.setDateAccurate(dateAccurate);
        entity.setUrl(url);
        entity.setLeaf(leaf);
        entity.setChildren(children.stream()
                .map(CategoryCompleteView::toEntity)
                .collect(Collectors.toList()));
        entity.setDescriptions(descriptions.stream()
                .map(DescriptionCompleteView::toEntity)
                .collect(Collectors.toList()));
        return entity;
    }
}
