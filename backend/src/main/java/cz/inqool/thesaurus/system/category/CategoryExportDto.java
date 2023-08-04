package cz.inqool.thesaurus.system.category;

import cz.inqool.thesaurus.system.Language;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CategoryExportDto {
    private String id;
    private CategoryName name;
    private String dateFrom;
    private String dateTo;
    private String dateAccurate;
    private String url;
    private CategoryExportDto parent;
    private List<CategoryExportDto> children;
    private List<DescriptionExportDto> descriptions;

    @Builder
    @Getter
    @Setter
    public static class DescriptionExportDto {
        private DescriptionTitle title;
        private List<ContentExportDto> contents;

        @Builder
        @Getter
        @Setter
        public static class ContentExportDto {
            private String text;
            private Language language;
            private List<QuoteExportDto> quotes;

            @Builder
            @Getter
            @Setter
            public static class QuoteExportDto {
                private String title;
                private String date;
                private String locationPage;
                private String locationUrl;
                private SourceExportDto source;

                @Builder
                @Getter
                @Setter
                public static class SourceExportDto {
                    private String label;
                    private String url;
                }
            }
        }

        @Builder
        @Getter
        @Setter
        public static class DescriptionTitle {
            private String cz;
            private String en;
            private String de;
        }
    }
}
