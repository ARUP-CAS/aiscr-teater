package cz.inqool.thesaurus.system.description;

import cz.inqool.thesaurus.dao.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository extends Store<Description, QDescription>, CustomDescriptionRepository {
    List<Description> findAllByCategoryId(String categoryId);

}
