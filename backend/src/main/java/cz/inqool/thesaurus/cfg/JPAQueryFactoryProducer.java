package cz.inqool.thesaurus.cfg;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.inqool.thesaurus.dao.StoreImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JPAQueryFactoryProducer {

    /**
     * Produces QueryDSL {@link JPAQueryFactory} used in {@link StoreImpl}.
     *
     * @param entityManager Provided JPA {@link EntityManager}
     * @return produced {@link JPAQueryFactory}
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}

