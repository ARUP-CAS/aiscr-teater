package cz.inqool.thesaurus.system.history;

import cz.inqool.thesaurus.dao.DomainObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class HistoryEvent extends DomainObject {
    private String label;
    private ZonedDateTime timestamp;
}
