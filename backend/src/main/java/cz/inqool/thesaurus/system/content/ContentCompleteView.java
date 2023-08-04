package cz.inqool.thesaurus.system.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.thesaurus.dao.AbstractView;
import cz.inqool.thesaurus.system.Language;
import cz.inqool.thesaurus.system.category.Category;
import cz.inqool.thesaurus.system.category.CategoryCompleteView;
import cz.inqool.thesaurus.system.description.Description;
import cz.inqool.thesaurus.system.description.DescriptionCompleteView;
import cz.inqool.thesaurus.system.description.QDescription;
import cz.inqool.thesaurus.system.quote.Quote;
import cz.inqool.thesaurus.system.quote.QuoteCompleteView;
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
@Entity(name = "content_complete")
@Table(name = "content")
public class ContentCompleteView extends AbstractView<Content, ContentCompleteView, QContentCompleteView> {

    @JsonIgnore
    @ManyToOne
    private Description description;

    private String text;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "content", fetch = FetchType.EAGER)
    private List<QuoteCompleteView> quotes = new ArrayList<>();

    @Override
    public Content toEntity() {
        Content entity = super.toEntity(new Content());
        entity.setDescription(description);
        entity.setText(text);
        entity.setLanguage(language);
        entity.setQuotes(quotes.stream()
                .map(QuoteCompleteView::toEntity)
                .collect(Collectors.toList()));
        return entity;
    }
}
