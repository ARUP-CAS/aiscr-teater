package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.system.Language;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomContentRepository {
    Map<String, List<Content>> findByDescriptionAndLanguageMap(Set<String> ids, Language language);
}
