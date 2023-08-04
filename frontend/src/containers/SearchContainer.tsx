import { useState, FC, useEffect } from 'react';
import { useHistory } from 'react-router';
import gql from 'graphql-tag';
import { useQuery } from 'react-apollo-hooks';

import Search from 'components/search';
import { FormResult } from 'components/search/_types';
import { useDebounce } from 'hooks/useDebounce';
import { useCountdown } from 'hooks/useCountdown';
import useLanguage from 'hooks/useLanguage';
import useLocalizedUrl from 'hooks/useLocalizedUrl';

import {
	GetSearchResults,
	GetSearchResultsVariables,
} from '../generated/GetSearchResults';

const GET_SEARCH_RESULTS = gql`
	query GetSearchResults($value: String!, $limit: Int, $language: Language!) {
		search(value: $value, limit: $limit, language: $language) {
			name
			url
			searchType
		}
	}
`;

const useSuggestedData = (
	value: string,
	limit: number,
): { data: FormResult[]; loading: boolean; error: boolean } => {
	const language = useLanguage();

	const debouncedValue = useDebounce<string>(value, 150);
	const { data, loading, error } = useQuery<
		GetSearchResults,
		GetSearchResultsVariables
	>(GET_SEARCH_RESULTS, {
		variables: { value: debouncedValue, limit, language },
		skip: !debouncedValue,
	});

	return { data: data ? data.search : [], loading, error: !!error };
};

export const SearchContainer: FC<{ numberOfResults?: number }> = ({
	numberOfResults = 10,
}) => {
	const makeUrl = useLocalizedUrl();
	const history = useHistory();

	const [searchValue, setSearchValue] = useState('');
	const { data, loading, error } = useSuggestedData(
		searchValue,
		numberOfResults,
	);
	const [whispererVisible, setWhispererVisible] = useState(false);
	const {
		isCountdownActivate: visibleError,
		activate: displayError,
		deactivate: hideError,
	} = useCountdown(800);

	useEffect(() => {
		if (error) {
			displayError();
		}
	}, [error, displayError]);

	const onSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setSearchValue(event.target.value);
		setWhispererVisible(true);
		if (visibleError) {
			hideError();
		}
	};

	const hideWhisperer = () => {
		setWhispererVisible(false);
	};

	const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();
		if (!searchValue) {
			displayError();
			return false;
		}

		history.push(makeUrl(`search/${encodeURIComponent(searchValue)}`));
		hideWhisperer();
		return false;
	};

	const onFocus = () => {
		setWhispererVisible(true);
	};

	const onBlur = (relatedTargetIsChildren: boolean) => {
		if (!relatedTargetIsChildren) {
			setWhispererVisible(false);
		}
	};

	return (
		<Search
			data={data}
			error={visibleError}
			value={searchValue}
			onChange={onSearchChange}
			onSubmit={onSubmit}
			onFocus={onFocus}
			onBlur={onBlur}
			loading={loading}
			whispererVisible={whispererVisible}
			hideWhisperer={hideWhisperer}
		/>
	);
};
