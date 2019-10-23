package cz.inqool.thesaurus;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SearchResultItem {
    private String id;
    private String name;
    private CategoryType categoryType;
    private String url;
    private SearchType searchType;
}
