package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.dao.AbstractView;
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
@Entity(name = "category_export_main")
@Table(name = "category")
public class CategoryExportMainView extends AbstractView<Category, CategoryExportMainView, QCategoryExportMainView> {

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

    private boolean leaf;

    @ManyToOne
    private CategoryExportSimpleView parent;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<CategoryExportSimpleView> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<DescriptionCompleteView> descriptions = new ArrayList<>();

    @Override
    public Category toEntity() {
        Category entity = super.toEntity(new Category());
        entity.setName(name);
        entity.setDateFrom(dateFrom);
        entity.setDateTo(dateTo);
        entity.setDateAccurate(dateAccurate);
        entity.setLeaf(leaf);
        if (parent != null) {
            entity.setParent(parent.toEntity());
        }
        entity.setChildren(children.stream()
                .map(CategoryExportSimpleView::toEntity)
                .collect(Collectors.toList()));
        entity.setDescriptions(descriptions.stream()
                .map(DescriptionCompleteView::toEntity)
                .collect(Collectors.toList()));
        return entity;
    }
}
