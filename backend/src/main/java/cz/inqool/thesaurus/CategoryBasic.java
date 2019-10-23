package cz.inqool.thesaurus;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CategoryBasic {
    private String id;
    private String name;
    private CategoryType categoryType;
    private String url;
    private boolean isLeaf;
}
