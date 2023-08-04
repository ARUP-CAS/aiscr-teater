package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends Store<Category, QCategory>, CustomCategoryRepository {

    Optional<Category> findByUrl(String url);
}
