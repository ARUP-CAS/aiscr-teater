import { FC } from 'react';

import { SearchContainer } from 'containers/SearchContainer';
import useTranslation from 'hooks/useTranslation';

import { Header } from './header';
import { HeaderProps } from './header/_types';

export const LargeHeader: FC<
	{
		numberOfSearchResults?: number;
	} & HeaderProps
> = ({ numberOfSearchResults, ...props }) => {
	const t = useTranslation({
		title: {
			cs: 'Zadejte hledaný výraz',
			en: 'Enter search term',
			de: 'Geben sie einen suchbegriff ein',
		},
	});

	return (
		<Header className="large-header" {...props}>
			<div className="large-search">
				<h1>{t.title}</h1>
				<div className="search-content">
					<SearchContainer numberOfResults={numberOfSearchResults} />
				</div>
			</div>
		</Header>
	);
};
