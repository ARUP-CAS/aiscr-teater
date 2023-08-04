import { GetSearchPage_search } from 'generated/GetSearchPage';

export type SearchResult = GetSearchPage_search;

export type SearchResultsContainerProps = {
	data: SearchResult[];
	loading: boolean;
};
