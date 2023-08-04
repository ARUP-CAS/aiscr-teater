package cz.inqool.thesaurus.system.description;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.thesaurus.dao.AbstractView;
import cz.inqool.thesaurus.system.category.Category;
import cz.inqool.thesaurus.system.category.CategoryCompleteView;
import cz.inqool.thesaurus.system.category.QCategoryCompleteView;
import cz.inqool.thesaurus.system.content.Content;
import cz.inqool.thesaurus.system.content.ContentCompleteView;
import cz.inqool.thesaurus.system.quote.Quote;
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
@Entity(name = "description_complete")
@Table(name = "description")
public class DescriptionCompleteView extends AbstractView<Description, DescriptionCompleteView, QDescriptionCompleteView> {

    @JsonIgnore
    @ManyToOne
    private Category category;

    private String titlePropertyKey;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "description", fetch = FetchType.EAGER)
    private List<ContentCompleteView> contents = new ArrayList<>();

    @Override
    public Description toEntity() {
        Description entity = super.toEntity(new Description());
        entity.setCategory(category);
        entity.setTitlePropertyKey(titlePropertyKey);
        entity.setContents(contents.stream()
                .map(ContentCompleteView::toEntity)
                .collect(Collectors.toList()));
        return entity;
    }
}
