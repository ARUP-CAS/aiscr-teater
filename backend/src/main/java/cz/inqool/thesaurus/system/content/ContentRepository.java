package cz.inqool.thesaurus.system.content;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends Store<Content, QContent>, CustomContentRepository {



//    @Query("SELECT c FROM Content c WHERE c.description.id = :descriptionId AND (c.language is null OR c.language = :language)")
//    List<Content> findAllByDescriptionIdAndLanguage(@Param("descriptionId") String descriptionId,
//                                                    @Param("language") Language language);
//
//    @Query("SELECT c FROM Content c WHERE c.description.id in :descriptionIds AND (c.language is null OR c.language = :language)")
//    List<Content> findAllByDescriptionIdsAndLanguage(@Param("descriptionIds") Set<String> descriptionIds,
//                                                    @Param("language") Language language);
}
