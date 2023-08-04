import { FC, useEffect } from 'react';
import gql from 'graphql-tag';
import { useQuery } from 'react-apollo-hooks';
import { RouteComponentProps, useHistory } from 'react-router';

import { GetPermalink, GetPermalinkVariables } from 'generated/GetPermalink';
import { Ooops404 } from 'components/Ooops';
import useLocalizedUrl from 'hooks/useLocalizedUrl';
import useLanguage from 'hooks/useLanguage';
import { Page } from 'components/Page';
import { SmallHeader } from 'components/SmallHeader';
import { DimmerLoading } from 'components/DimmerLoading';

const GET_PERMALINK = gql`
	query GetPermalink($id: ID!, $language: Language!) {
		singleCategoryWithId(id: $id, language: $language) {
			id
			url
		}
	}
`;

const PermalinkPage: FC<RouteComponentProps<{ id: string }>> = p => {
	const { replace } = useHistory();
	const makeUrl = useLocalizedUrl();
	const language = useLanguage();

	const { data, loading, error } = useQuery<
		GetPermalink,
		GetPermalinkVariables
	>(GET_PERMALINK, {
		variables: {
			id: p.match.params.id,
			language,
		},
		fetchPolicy: 'no-cache',
	});

	useEffect(() => {
		if (loading || error) return;
		replace(makeUrl(data?.singleCategoryWithId.url ?? ''));
	}, [loading, error, data]);

	if (error) {
		return (
			<Page>
				<SmallHeader />
				<Ooops404 />
			</Page>
		);
	}

	return <DimmerLoading />;
};
export default PermalinkPage;
