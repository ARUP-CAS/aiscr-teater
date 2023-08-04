import gql from 'graphql-tag';
import { useMemo } from 'react';
import { useQuery } from 'react-apollo-hooks';
import { useParams } from 'react-router';

import useTranslation from 'hooks/useTranslation';
import { GetLastImport } from 'generated/GetLastImport';

import 'scss/_footer.scss';

const getLocale = (lng: string) => (lng);

const GET_LAST_IMPORT = gql`
	query GetLastImport {
		lastImport
	}
`;

export const Footer = () => {
	const { language } = useParams<{ language: string }>();

	const t = useTranslation({
		lastImport: {
			cs: 'Poslední aktualizace',
			en: 'Last update',
			de: 'Letztes update',
		},
		beginDate: {
			cs: 'Datum spuštění',
			en: 'Launch date',
			de: 'Auflegungsdatum',
		},
		contact: {
			cs: 'Kontakt',
			en: 'Contact',
			de: 'Kontakt',
		},
	});

	const { data } = useQuery<GetLastImport>(GET_LAST_IMPORT, {
		fetchPolicy: 'no-cache',
	});
	const lastImport = useMemo(
		() => new Date(data?.lastImport).toLocaleDateString(getLocale(language)),
		[data?.lastImport, language],
	);
	const beginDate = useMemo(
		() => new Date('6/9/2019').toLocaleDateString(getLocale(language)),
		[language],
	);

	return (
		<footer className="footer">
			<div className="footer-column">
				© 2019 Archeologický ústav AV ČR, Brno, v. v. i.
			</div>
			<div className="footer-column">
				Archeologický ústav AV ČR, Brno, v. v. i., Čechyňská 363/19, 602 00 Brno
			</div>
			<div className="footer-column">
				{t.lastImport} {lastImport}
			</div>
			<div className="footer-column">
				{t.beginDate} {beginDate}
			</div>
			<div className="footer-column">
				{t.contact}: <a href="mailto:teater@arub.cz">teater@arub.cz</a>
			</div>
		</footer>
	);
};
