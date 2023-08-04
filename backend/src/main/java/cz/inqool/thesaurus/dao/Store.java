package cz.inqool.thesaurus.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface Store<T extends DomainObject, Q extends EntityPathBase<T>> extends JpaRepository<T, String>, QuerydslPredicateExecutor<Q>{
}
