import { FC } from 'react';

import useTranslation from 'hooks/useTranslation';

const Ooops: FC<{ text: string; subtext?: string }> = ({
	text,
	subtext = '',
}) => (
	<div className="oops-wrapper">
		<div className="oops-container">
			<div className="oops">
				{text}
				{subtext && <small>{subtext}</small>}
			</div>
		</div>
	</div>
);

export const Ooops404 = () => {
	const t = useTranslation({
		text: {
			cs: 'Stránku se nepodařilo najít.',
			en: 'The page could not be found.',
			de: 'Die Seite konnte nicht gefunden werden.',
		},
		subtext: {
			cs: 'Buď neexistuje nebo je momentálně nedostupná.',
			en: 'It either doesn\'t exist or is currently unavailable.',
			de: 'Es existiert entweder nicht oder ist derzeit nicht verfügbar.',
		},
	});
	return <Ooops {...t} />;
};

export const OoopsError = () => {
	const t = useTranslation({
		text: {
			cs: 'Nastala chyba :(',
			en: 'An error occurred :(',
			de: 'Ein Fehler ist aufgetreten :(',
		},
		subtext: {
			cs: 'Zkuste prosím později.',
			en: 'Please try again later.',
			de: 'Bitte versuchen Sie es später erneut.',
		},
	});
	return <Ooops {...t} />;
};
