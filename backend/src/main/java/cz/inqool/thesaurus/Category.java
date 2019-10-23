package cz.inqool.thesaurus;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Category{
    @Id
    private String id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private CategoryType categoryType;

    @Column(length = 16383)
    private String description;

    private String url;

    private Long dateFrom;

    private Long dateTo;

    private Long date_accurate;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;
}