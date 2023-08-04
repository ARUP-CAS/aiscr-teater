import { FC } from 'react';
import { Link } from 'react-router-dom';

import useLocalizedUrl from 'hooks/useLocalizedUrl';
import useTranslation from 'hooks/useTranslation';
import {
	getSearchTypeToString,
	searchTypeTranslations,
} from 'utils/utilizeSearchTypes';

import { FormResult } from './_types';

export const Whisperer: FC<{ data: FormResult[]; hide?: () => void }> = ({
	data = [],
	hide,
}) => {
	const makeUrl = useLocalizedUrl();
	const t = useTranslation(searchTypeTranslations);

	return data.length ? (
		<div className="whisperer-container">
			<ul className="whisperer">
				{data.map(item => (
					<li key={item.url + item.searchType + item.name}>
						<Link
							to={makeUrl(item.url)}
							title={item.name}
							onClick={hide}
						>
							<span className="title">{item.name}</span>
							<span className="meta">
								{t[getSearchTypeToString(item.searchType)]}
							</span>
						</Link>
					</li>
				))}
			</ul>
		</div>
	) : null;
};
