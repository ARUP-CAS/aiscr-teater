import { FC } from 'react';
import { Link } from 'react-router-dom';

import useLocalizedUrl from 'hooks/useLocalizedUrl';

import { Breadcrumb } from './_types';

export const BreadcrumbItem: FC<Breadcrumb> = ({
	name,
	url,
	active = false,
}) => {
	const makeUrl = useLocalizedUrl();
	return active ? (
		<li className="active">{name}</li>
	) : (
		<li>
			<Link to={makeUrl(url)} title={name}>
				{name}
			</Link>
		</li>
	);
};
