package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.dao.AbstractView;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Immutable
@Entity(name = "category_export_simple")
@Table(name = "category")
public class CategoryExportSimpleView extends AbstractView<Category, CategoryExportSimpleView, QCategoryExportSimpleView> {

    @ManyToOne
    private CategoryExportSimpleView parent;

    @Override
    public Category toEntity() {
        Category entity = super.toEntity(new Category());
        return entity;
    }
}