import { useMemo } from 'react';
import { useLocation } from 'react-router';

import { Language } from 'generated/globalTypes';

const useLanguage = <T extends boolean = false>(
	lowercase?: T,
): T extends false ? Language : Lowercase<Language> => {
	const { search } = useLocation<{ language: string }>();
	return useMemo(() => {
		const params = new URLSearchParams(search);
		const lng =
			params.get('view') ??
			navigator.languages.find(l => l.match(/(cs)|(en)|(de)/))?.slice(0, 2) ??
			'cs';
		return (
			lowercase ? lng.toLocaleLowerCase() : lng.toLocaleUpperCase()
		) as never;
	}, [search]);
};
export default useLanguage;
