package cz.inqool.thesaurus.system.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.thesaurus.dao.AbstractView;
import cz.inqool.thesaurus.system.content.Content;
import cz.inqool.thesaurus.system.content.ContentCompleteView;
import cz.inqool.thesaurus.system.content.QContent;
import cz.inqool.thesaurus.system.source.Source;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.stream.Collectors;

@Getter
@Immutable
@Entity(name = "quote_complete")
@Table(name = "quote")
public class QuoteCompleteView extends AbstractView<Quote, QuoteCompleteView, QQuoteCompleteView> {

    private String title;
    private String date;
    private String locationPage;
    private String locationUrl;

    @JsonIgnore
    @ManyToOne
    private Content content;
    @ManyToOne
    private Source source;

    @Override
    public Quote toEntity() {
        Quote entity = super.toEntity(new Quote());
        entity.setTitle(title);
        entity.setDate(date);
        entity.setLocationPage(locationPage);
        entity.setLocationUrl(locationUrl);
        entity.setContent(content);
        entity.setSource(source);
        return entity;
    }
}
