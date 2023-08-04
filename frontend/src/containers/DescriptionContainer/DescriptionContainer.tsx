import { FC, useEffect, useRef, useMemo } from 'react';
import { useQuery } from 'react-apollo-hooks';

import Breadcrumbs from 'components/breadcrumbs';
import Description from 'components/description';
import {
	GetDescriptionPage,
	GetDescriptionPageVariables,
} from 'generated/GetDescriptionPage';
import { HeaderTags } from 'components/header';
import { isOutOfViewport } from 'utils/isOutOfViewport';
import useTranslation from 'hooks/useTranslation';

import { Ooops404 } from '../../components/Ooops';

import { parseBreadcrumbsData, parseData } from './_actions';
import { GET_DESCRIPTION_PAGE } from './_gql';

export const DescriptionContainer: FC<GetDescriptionPageVariables> =
	variables => {
		// TODO: Fix caching bug
		const { data, loading, error } = useQuery<
			GetDescriptionPage,
			GetDescriptionPageVariables
		>(GET_DESCRIPTION_PAGE, { variables, fetchPolicy: 'no-cache' });

		const { breadcrumbs, neighbors, children } = useMemo(
			() =>
				error || loading
					? { breadcrumbs: [], neighbors: [], children: [] }
					: {
							breadcrumbs: parseBreadcrumbsData(data),
							neighbors: parseData('neighbors', data),
							children: parseData('children', data),
					  },
			[loading, error, data],
		);

		const titleRef = useRef<HTMLHeadingElement>(null);
		const t = useTranslation({
			loading: { cs: 'Načíta se', en: 'Loading', de: 'Laden' },
		});

		useEffect(() => {
			if (
				!loading &&
				titleRef.current &&
				isOutOfViewport(titleRef.current).any
			) {
				titleRef.current.scrollIntoView();
			}
		}, [loading, data]);

		if (error) {
			return <Ooops404 />;
		}

		return (
			<>
				<HeaderTags
					title={data ? data.singleCategoryWithUrl.name : t.loading}
				/>
				<Breadcrumbs data={breadcrumbs} />
				<Description
					id={data?.singleCategoryWithUrl.id}
					ref={titleRef}
					neighborsCol={neighbors}
					childrenCol={children}
					loading={loading}
					title={data?.singleCategoryWithUrl.name}
					descriptions={data?.singleCategoryWithUrl.descriptions}
				/>
			</>
		);
	};
