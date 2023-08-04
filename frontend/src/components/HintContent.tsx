import { useMemo, useState } from 'react';

import useLanguage from 'hooks/useLanguage';
import CsHint from 'assets/hint/CsHint.md';
import EnHint from 'assets/hint/EnHint.md';
import DeHint from 'assets/hint/DeHint.md';

import { StyledContent } from './StyledContent';
import Markdown from './Markdown';

export const HintContent = () => {
	const language = useLanguage(true);

	const [text, setText] = useState('');
	useMemo(() => {
		fetch(language === 'cs' ? CsHint : language === 'en' ? EnHint : DeHint)
			.then(r => r.text())
			.then(setText);
	}, [language]);

	return (
		<StyledContent>
			<Markdown text={text} />
		</StyledContent>
	);
};
