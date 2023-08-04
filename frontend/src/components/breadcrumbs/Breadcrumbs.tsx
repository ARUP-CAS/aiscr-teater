import { FC } from 'react';
import { Link } from 'react-router-dom';

import useLocalizedUrl from 'hooks/useLocalizedUrl';
import useTranslation from 'hooks/useTranslation';

import { Breadcrumb } from './_types';
import { BreadcrumbItem } from './BreadcrumbItem';

export const Breadcrumbs: FC<{ data: Breadcrumb[] }> = ({ data = [] }) => {
	const makeUrl = useLocalizedUrl();
	const t = useTranslation({
		title: {
			cs: 'Vymazat vyhledávaní',
			en: 'Clear search',
			de: 'Suche löschen',
		},
	});

	return (
		<div className="breadcrumbs">
			<ul className="breadcrumbs-list">
				{data.map(breadcrumb => (
					<BreadcrumbItem key={breadcrumb.url} {...breadcrumb} />
				))}
			</ul>
			<Link to={makeUrl('')} className="remove-search" title={t.title}>
				{t.title}
			</Link>
		</div>
	);
};
