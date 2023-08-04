package cz.inqool.thesaurus.dao;

import com.querydsl.core.types.dsl.EntityPathBase;

public interface Projection<E extends DomainObject, P extends Projection<E, P, QP>, QP extends EntityPathBase<P>> {

    /**
     * Returns ID of projected entity
     */
    String getId();
}