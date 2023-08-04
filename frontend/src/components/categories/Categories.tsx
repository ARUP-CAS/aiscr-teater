import { FC } from 'react';
import times from 'lodash/times';
import { Link } from 'react-router-dom';

import useLocalizedUrl from 'hooks/useLocalizedUrl';

import { Category } from './_types';

const Loading = () => (
	<>
		{times(15, index => (
			<li key={index} className="loading-background" />
		))}
	</>
);

export const Categories: FC<{ data?: Category[]; loading?: boolean }> = ({
	data = [],
	loading = false,
}) => {
	const makeUrl = useLocalizedUrl();
	return (
		<ul className="categories">
			{loading ? (
				<Loading />
			) : (
				data.map(category => (
					<li key={`category-${category.url}`}>
						<Link to={makeUrl(category.url)} title={category.name}>
							{category.name}
						</Link>
					</li>
				))
			)}
		</ul>
	);
};
