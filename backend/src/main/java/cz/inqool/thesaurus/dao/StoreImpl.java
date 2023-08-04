package cz.inqool.thesaurus.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


abstract public class StoreImpl<T extends DomainObject, Q extends EntityPathBase<T>>{


    /**
     * Entity class object
     */
    @Getter
    protected final Class<T> type;

    /**
     * QueryDSL meta class object
     */
    protected final Class<Q> qType;

    protected final Q qObject;

    /**
     * Entity manager used for JPA
     */
    protected EntityManager entityManager;
    /**
     * QueryDSL query factory
     */
    protected JPAQueryFactory queryFactory;


    public StoreImpl(Class<T> type, Class<Q> qType) {
        this.type = type;
        this.qType = qType;
        this.qObject = constructQObject(type, qType);
    }

    /**
     * Returns the single instance with provided ID.
     *
     * @param id ID of instance to return
     * @return single instance or {@code null} if not found
     */
    public T find(@NonNull String id) {
        StringPath idPath = propertyPath("id");

        JPAQuery<T> query = query().select(qObject)
                .where(idPath.eq(id));

        T entity = query.fetchFirst();

        if (entity != null) {
            detachAll();
        }
        return entity;
    }

    /**
     * Returns the single instance with provided ID. The entity is fetched using provided view constructor.
     *
     * @param id       ID of instance to return
     * @param qpObject query projection object
     * @param <QP>     type of Q-class for entity view
     * @param <P>      type of entity view
     * @return single instance or {@code null} if not found
     */
    public <QP extends EntityPathBase<P>, P extends Projection<T, P, QP>> P find(@NonNull String id, @NonNull QP qpObject) {

        StringPath idPath = propertyPath("id", qpObject);
        JPAQuery<P> query = queryFactory.select(qpObject)
                .from(qpObject)
                .where(idPath.eq(id));

        P entityProjection = query.fetchFirst();

        if (entityProjection != null) {
            detachAll();
        }

        return entityProjection;
    }

    /**
     * Returns the single instance with provided ID. The entity is fetched using provided view constructor and converted
     * back to {@link T} type via {@link EntityProjection#toEntity()}
     *
     * @param id       ID of instance to return
     * @param qpObject query projection object
     * @param <QP>     type of Q-class for entity view
     * @param <P>      type of entity view
     * @return single instance or {@code null} if not found
     */
    public <QP extends EntityPathBase<P>, P extends EntityProjection<T, P, QP>> T findWith(@NonNull String id, QP qpObject) {
        P projection = find(id, qpObject);
        return (projection != null) ? projection.toEntity() : null;
    }


    /**
     * Creates QueryDSL query object.
     */
    protected JPAQuery<?> query() {
        return queryFactory.from(qObject);
    }

    /**
     * Creates QueryDSL query object for other entity than the store one.
     */
    protected <C> JPAQuery<?> query(EntityPathBase<C> base) {
        return queryFactory.from(base);
    }

    protected void detach(T entity) {
        if (entity != null) {
            entityManager.detach(entity);
        }
    }

    protected void detachAll() {
        entityManager.clear();
    }

    /**
     * Creates meta object attribute.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name name of the attribute
     * @return meta object attribute
     */
    protected StringPath propertyPath(String name) {
        PathBuilder<T> builder = new PathBuilder<>(qObject.getType(), qObject.getMetadata().getName());
        return builder.getString(name);
    }

    /**
     * Creates meta object attribute.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name   name of the attribute
     * @param qClass entity path base class
     * @return meta object attribute
     */
    protected <C extends EntityPathBase<?>> StringPath propertyPath(String name, C qClass) {
        PathBuilder<?> builder = new PathBuilder<>(qClass.getType(), qClass.getMetadata().getName());
        return builder.getString(name);
    }

    /**
     * Creates meta object attribute for enum type.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name name of the attribute
     * @return meta object attribute
     */
    protected <X extends Enum<X>> EnumPath<X> propertyPathEnum(String name, Class<X> type) {
        PathBuilder<T> builder = new PathBuilder<>(qObject.getType(), qObject.getMetadata().getName());
        return builder.getEnum(name, type);
    }

    protected static <TC extends DomainObject, QC extends EntityPathBase<?>> QC constructQObject(Class<TC> type, Class<QC> qType) {
        char[] c = type.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String name = new String(c);

        try {
            Constructor<QC> constructor = qType.getConstructor(String.class);
            return constructor.newInstance(name);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PersistenceException("Error creating Q object for + " + type.getName());
        }
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setQueryFactory(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
}
