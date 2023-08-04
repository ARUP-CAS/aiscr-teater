import { FC } from 'react';

import SearchResults from 'components/searchResults';

import { SearchResultsContainerProps } from './_types';
import { convertSearchResultsToColumns } from './_actions';

export const SearchResultsContainer: FC<SearchResultsContainerProps> = ({
	data,
	loading,
}) => (
	<SearchResults
		loading={loading}
		columns={convertSearchResultsToColumns(data)}
	/>
);
