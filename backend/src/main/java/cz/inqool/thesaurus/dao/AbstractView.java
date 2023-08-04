package cz.inqool.thesaurus.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Class representing a hibernate projection class for an entity
 *
 * @param <E>  type of entity class which data will be projected by this class
 * @param <P>  type of this projection (only for type checking)
 * @param <QP> type of Q-class for this projection to be used in DB queries
 */
@MappedSuperclass
@XmlTransient
abstract public class AbstractView<E extends DomainObject, P extends AbstractView<E, P, QP>, QP extends EntityPathBase<P>> implements EntityProjection<E, P, QP> {

    /**
     * @see DomainObject#id
     */
    @Id
    @Getter
    protected String id;


    protected AbstractView() {
    }

    protected E toEntity(E entity) {
        entity.setId(id);
        return entity;
    }

}
