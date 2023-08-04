package cz.inqool.thesaurus.dao;

import com.querydsl.core.types.dsl.EntityPathBase;

public interface EntityProjection<E extends DomainObject, P extends EntityProjection<E, P, QP>, QP extends EntityPathBase<P>> extends Projection<E, P, QP> {

    /**
     * Converts this instance to appropriate entity instance of type {@link E}. This method should set all available
     * properties on resulting instance.
     */
    E toEntity();
}

