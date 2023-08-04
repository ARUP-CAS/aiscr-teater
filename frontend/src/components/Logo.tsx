import { Link } from 'react-router-dom';
import { FC } from 'react';

import LogoImage from 'assets/logo.png';
import useLocalizedUrl from 'hooks/useLocalizedUrl';
import useTranslation from 'hooks/useTranslation';

export const Logo: FC = ({ children }) => {
	const getUrl = useLocalizedUrl();
	const t = useTranslation({
		title: {
			cs: 'Archeologický informačný systém',
			en: 'Archaeological information system',
			de: 'Archäologisches Informationssystem',
		},
	});

	return (
		<Link className="logo" to={getUrl('')} title={t.title}>
			<img src={LogoImage} className="logo-image" alt="Logo" title="Logo" />
			{children}
		</Link>
	);
};
