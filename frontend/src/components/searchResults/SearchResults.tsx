import { FC } from 'react';

import Column from 'components/column';
import useTranslation from 'hooks/useTranslation';

import { SearchResultsProps } from './_types';

const Loading = () => (
	<>
		<div className="column-wrapper">
			<Column loading />
		</div>
		<div className="column-wrapper">
			<Column loading />
		</div>
	</>
);

export const SearchResults: FC<SearchResultsProps> = ({ columns, loading }) => {
	const t = useTranslation({
		empty: {
			cs: 'Žádné výsledky nebyly nalezeny',
			en: 'No results were found',
			de: 'Keine Ergebnisse werden aufgefunden',
		},
	});
	if (loading) {
		return (
			<div className="search-results">
				<Loading />
			</div>
		);
	}

	if (!columns.length) {
		return <div className="search-results empty-results">{t.empty}</div>;
	}

	return (
		<div className="search-results">
			{columns.map(column => (
				<div className="column-wrapper" key={column.key}>
					<Column title={column.title} data={column.data} noResults={t.empty} />
				</div>
			))}
		</div>
	);
};
