import { FC } from 'react';
import { Helmet } from 'react-helmet';

import useTranslation from 'hooks/useTranslation';

import { HeaderProps } from './_types';

export const HeaderTags: FC<Pick<HeaderProps, 'title'>> = ({ title = '' }) => {
	const t = useTranslation({
		tezaurus: {
			cs: 'Tezaurus archeologické terminologie',
			en: 'Thesaurus of Archaeological Terminology',
			de: 'Thesaurus der archäologischen Terminologie',
		},
	});
	return (
		<Helmet>
			<title>{`${title}${title ? ' | ' : ''}${t.tezaurus}`}</title>
		</Helmet>
	);
};
