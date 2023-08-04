import useTranslation from 'hooks/useTranslation';

import { SearchResultsColumn } from '../../components/searchResults/_types';
import {
	getSearchTypeToString,
	searchTypeTranslations,
} from '../../utils/utilizeSearchTypes';
import { SearchType } from '../../generated/globalTypes';

import { SearchResult } from './_types';

export const convertSearchResultsToColumns = (
	data: SearchResult[],
): SearchResultsColumn[] => {
	const t = useTranslation({
		keysTitle: {
			cs: 'Výsledky v názvech hesel',
			en: 'Results in key names',
			de: 'Ergebnisse im Stichwörterverzeichnis',
		},
		metaTitle: {
			cs: 'Výsledky v metadatech',
			en: 'Results in metadata',
			de: 'Ergebnisse in Metadaten',
		},
		...searchTypeTranslations,
	});

	const initData = [
		{
			data: [],
			title: t.keysTitle,
			key: getSearchTypeToString(SearchType.NAME),
		},
		{
			data: [],
			title: t.metaTitle,
			key: getSearchTypeToString(SearchType.METADATA),
		},
	];

	return data.reduce<SearchResultsColumn[]>((columns, current) => {
		const searchType = getSearchTypeToString(current.searchType);
		const keyInColumns = columns.find(value => value.key === searchType);

		if (keyInColumns) {
			keyInColumns.data.push(current);
		} else {
			columns.push({
				data: [current],
				title: t[getSearchTypeToString(current.searchType)],
				key: searchType,
			});
		}

		return columns;
	}, initData);
};
