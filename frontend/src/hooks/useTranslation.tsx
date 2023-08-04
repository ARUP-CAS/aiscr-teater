import { useMemo } from 'react';

import { Language } from 'generated/globalTypes';

import useLanguage from './useLanguage';

const useTranslation = <
	T extends Record<string, Record<Lowercase<Language>, string>>,
>(
	translation: T,
) => {
	const language = useLanguage(true);
	return useMemo(
		() =>
			Object.entries<Record<Lowercase<Language>, string>>(translation).reduce(
				(prev, [k, v]) => ({ ...prev, [k]: v[language] }),
				{} as Record<keyof T, string>,
			),
		[language],
	);
};

export default useTranslation;
