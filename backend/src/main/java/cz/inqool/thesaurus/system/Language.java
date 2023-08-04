package cz.inqool.thesaurus.system;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Language {
    CS(new Locale("cs","CZ")),
    EN(new Locale("en", "GB")),
    DE(new Locale("de", "DE"));

    Language(Locale locale) {
        this.locale = locale;
    }

    private Locale locale;

    public static Language valueOf(Object obj) {
        return valueOf((String) obj);
    }
}
