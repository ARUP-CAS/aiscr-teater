import { FC } from 'react';
import gql from 'graphql-tag';
import { useQuery } from 'react-apollo-hooks';
import { RouteComponentProps } from 'react-router-dom';

import 'scss/pages/_page_search.scss';
import { Page } from 'components/Page';
import { Content } from 'components/Content';
import { SearchResultsContainer } from 'containers/SearchResultsContainer';
import { GetSearchPage, GetSearchPageVariables } from 'generated/GetSearchPage';
import Categories from 'components/categories';
import useTranslation from 'hooks/useTranslation';
import useLanguage from 'hooks/useLanguage';

import { OoopsError } from '../components/Ooops';
import { SmallHeader } from '../components/SmallHeader';

const GET_SEARCH_PAGE = gql`
	query GetSearchPage($searchValue: String!, $language: Language!) {
		search(value: $searchValue, language: $language) {
			name
			url
			searchType
		}
		listRootCategories(language: $language) {
			id
			name
			url
		}
	}
`;

const SearchPage: FC<RouteComponentProps<{ searchValue?: string }>> = p => {
	const language = useLanguage();
	const { searchValue = '' } = p.match.params;
	const utilizedSearchValue = decodeURIComponent(searchValue);
	const { data, loading, error } = useQuery<
		GetSearchPage,
		GetSearchPageVariables
	>(GET_SEARCH_PAGE, {
		variables: { searchValue, language },
	});

	const t = useTranslation({
		results: {
			cs: 'Výsledky pro výraz:',
			en: 'Results for:',
			de: 'Ergebnisse für:',
		},
		explore: {
			cs: 'Prozkoumejte další oblasti',
			en: 'Explore other areas',
			de: 'Erkunden Sie andere Bereiche',
		},
	});

	if (error) {
		return (
			<Page>
				<SmallHeader title={searchValue} />
				<OoopsError />
			</Page>
		);
	}

	return (
		<Page>
			<SmallHeader title={searchValue} />
			<Content className="content-search">
				<h3>
					{t.results} <span className="meta">{utilizedSearchValue}</span>
				</h3>
				<SearchResultsContainer
					data={data ? data.search : []}
					loading={loading}
				/>
				<div>
					<h4>{t.explore}</h4>
					<Categories
						data={data ? data.listRootCategories : []}
						loading={loading}
					/>
				</div>
			</Content>
		</Page>
	);
};

export default SearchPage;
