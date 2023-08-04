import { FC } from 'react';
import { Link } from 'react-router-dom';
import classNames from 'classnames';
import times from 'lodash/times';

import useLocalizedUrl from 'hooks/useLocalizedUrl';

import { ColumnItem } from './_types';

const ColumnResult: FC<ColumnItem> = ({ name, url, active, leaf }) => {
	const getUrl = useLocalizedUrl();

	return (
		<li>
			<Link
				to={getUrl(url)}
				title={name}
				className={classNames({
					active,
					leaf,
				})}
			>
				{name}
			</Link>
		</li>
	);
};

const ColumnsLoading = (): JSX.Element => (
	<>
		{times(
			7,
			(index): JSX.Element => (
				<li key={index} className="loading-background" />
			),
		)}
	</>
);

export const Column: FC<{
	title?: string;
	data?: ColumnItem[];
	noResults?: string;
	loading?: boolean;
}> = ({ title, data = [], noResults, loading }) => (
	<ul className="column">
		{title && <h4>{title}</h4>}
		{loading ? (
			<ColumnsLoading />
		) : data.length === 0 && noResults ? (
			<p>{noResults}</p>
		) : (
			data.map(item => <ColumnResult key={item.url} {...item} />)
		)}
	</ul>
);
