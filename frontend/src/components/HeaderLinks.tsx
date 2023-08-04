import { Link, useLocation } from 'react-router-dom';
import Flag from 'react-flagkit';
import { MdArchive } from 'react-icons/md';

import useLocalizedUrl from 'hooks/useLocalizedUrl';
import useTranslation from 'hooks/useTranslation';
import useLanguage from 'hooks/useLanguage';

export const HeaderLinks = () => {
	const makeUrl = useLocalizedUrl();
	const { pathname } = useLocation();
	const language = useLanguage(true);

	const t = useTranslation({
		hint: { cs: 'Nápověda', en: 'Hint', de: 'Hinweise' },
		export: {
			cs: 'Exportovat vše',
			en: 'Export all',
			de: 'Alles exportieren',
		},
	});

	return (
		<div className="hint">
			<Link
				to={`${pathname}?view=cs`}
				className={language === 'cs' ? 'active' : undefined}
			>
				<Flag country="CZ" />
			</Link>
			<Link
				to={`${pathname}?view=en`}
				className={language === 'en' ? 'active' : undefined}
			>
				<Flag country="GB" />
			</Link>
			<Link
				to={`${pathname}?view=de`}
				className={language === 'de' ? 'active' : undefined}
			>
				<Flag country="DE" />
			</Link>
			<Link to={makeUrl('hint')}>{t.hint}</Link>
			<a href="/api/export" download title={t.export}>
				<MdArchive size={23} />
			</a>
		</div>
	);
};
