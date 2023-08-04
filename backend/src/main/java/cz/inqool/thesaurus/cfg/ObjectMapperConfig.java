package cz.inqool.thesaurus.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.TimeZone;

@Configuration
public class ObjectMapperConfig {

    /**
     * Produces Jackson {@link ObjectMapper} used for JSON serialization/deserialization.
     *
     * <p>
     * {@link ObjectMapper} is used behind the scenes in Spring MVC object mapping, but can also be used by developer if
     * serialization/deserialization is needed in place.
     * </p>
     *
     * @param prettyPrint    Should the JSON be pretty-printed
     * @param serializeNulls Should the nulls be serialized or skipped
     * @return Produced {@link ObjectMapper}
     */
    @Primary
    @Bean
    public ObjectMapper objectMapper(@Value("${json.pretty-print:true}") Boolean prettyPrint,
                                     @Value("${json.serialize-nulls:false}") Boolean serializeNulls,
                                     @Value("${json.use-afterburner:false}") Boolean useAfterburner,
                                     @Value("${system.timezone:Europe/Prague}") TimeZone timeZone) {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        if (useAfterburner) {
            // register Jackson Afterburner to use bytecode enhancement instead of Java Reflection API to speed-up JSON processing
            objectMapper.registerModule(new AfterburnerModule());
        }

        // Register Hibernate5Module to support lazy objects
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);

        objectMapper.setTimeZone(timeZone);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        if (prettyPrint != null && prettyPrint) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        if (serializeNulls != null && !serializeNulls) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return objectMapper;
    }

    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }
}

