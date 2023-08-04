package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.system.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class CategoryName {
    private String cs;
    private String en;
    private String de;

    public String getLocalizedName(Language language) {
        if (Language.EN == language) return en;
        if (Language.DE == language) return de;
        return cs;
    }
}
